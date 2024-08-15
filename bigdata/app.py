from flask import Flask, request, jsonify, abort
import mysql.connector
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.neighbors import NearestNeighbors
from flask_cors import CORS
from pymongo import MongoClient
from dotenv import load_dotenv
import os

# .env 파일에서 환경 변수 로드
load_dotenv()

app = Flask(__name__)
CORS(app, supports_credentials=True)

# MySQL 데이터베이스 연결 함수
def get_db_connection():
    conn = mysql.connector.connect(
        host=os.getenv('DB_HOST'),
        user=os.getenv('DB_USER'),
        password=os.getenv('DB_PASSWORD'),
        database=os.getenv('DB_NAME')
    )
    return conn

# MongoDB 연결 설정 (환경 변수에서 가져옴)
mongo_uri = os.getenv('MONGO_URI')
db_name = os.getenv('MONGO_DB_NAME')
collection_name = os.getenv('MONGO_COLLECTION_NAME')

client = MongoClient(mongo_uri)
db = client[db_name]
collection = db[collection_name]

# 추천 관련 함수들
def fetch_study_scores():
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute('SELECT study_id, study_point + solve_count as score FROM study')
    study_scores = cursor.fetchall()
    conn.close()
    return pd.DataFrame(study_scores)

def fetch_study_problems(study_id):
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute('''
        SELECT p.problem_id, p.title, p.level, p.tag
        FROM study_problem sp
        JOIN problem p ON sp.problem_id = p.problem_id
        WHERE sp.study_id = %s
    ''', (study_id,))
    problems = cursor.fetchall()
    conn.close()

    df_problems = pd.DataFrame(problems)
    
    return df_problems

def recommend_by_representative(tag, solved_problem_ids, df_problems_unsolved):
    if df_problems_unsolved.empty:
        return []
    
    vectorizer = TfidfVectorizer(tokenizer=lambda x: x.split(';'))
    X = vectorizer.fit_transform(df_problems_unsolved['tag'])
    model = NearestNeighbors(n_neighbors=5, algorithm='auto').fit(X)

    vec = vectorizer.transform([tag])
    distances, indices = model.kneighbors(vec)
    recommended = df_problems_unsolved.iloc[indices[0]]

    recommended = recommended[~recommended['problem_id'].isin(solved_problem_ids)]

    if recommended.empty:
        return []

    return recommended.head(5).to_dict(orient='records')

def recommend_opposite_of_least_common_tag(least_common_tag, df_problems_unsolved):
    if df_problems_unsolved.empty:
        return []

    vectorizer = TfidfVectorizer(tokenizer=lambda x: x.split(';'))
    X = vectorizer.fit_transform(df_problems_unsolved['tag'])
    model = NearestNeighbors(n_neighbors=5, algorithm='auto').fit(X)

    vec = vectorizer.transform([least_common_tag])
    distances, indices = model.kneighbors(vec)
    
    recommended = df_problems_unsolved.iloc[indices[0][::-1]]  # Reverse the indices to get the least similar ones

    if recommended.empty:
        return []

    return recommended.head(5).to_dict(orient='records')

def recommend_by_collaborative_filtering(study_id, study_point, solved_problem_ids):
    df_study_scores = fetch_study_scores()

    if len(df_study_scores) == 0:
        return pd.DataFrame(columns=['problem_id', 'title', 'level', 'tag'])

    study_scores_matrix = df_study_scores.drop(columns=['study_id'])
    study_index = df_study_scores[df_study_scores['study_id'] == study_id].index[0]
    
    cosine_sim = cosine_similarity(study_scores_matrix)
    similarity_scores = pd.Series(cosine_sim[study_index]).sort_values(ascending=False)
    similar_studies = df_study_scores.iloc[similarity_scores.index[1:6]]['study_id'].values
    
    recommendations = pd.DataFrame(columns=['problem_id', 'title', 'level', 'tag'])

    if len(similar_studies) == 0:
        random_studies = df_study_scores.sample(n=5)['study_id'].values
        for random_study_id in random_studies:
            random_study_problems = fetch_study_problems(int(random_study_id))
            if 'problem_id' in random_study_problems.columns:
                problem_counts = random_study_problems['problem_id'].value_counts().head(5)
                recommended_problems = random_study_problems[random_study_problems['problem_id'].isin(problem_counts.index)]
                recommendations = pd.concat([recommendations, recommended_problems])
    else:
        for similar_study_id in similar_studies:
            similar_study_problems = fetch_study_problems(int(similar_study_id))
            if 'problem_id' in similar_study_problems.columns:
                unsolved_problems_from_similar_study = similar_study_problems[~similar_study_problems['problem_id'].isin(solved_problem_ids)]
                num_samples = min(len(unsolved_problems_from_similar_study), 5)  # 샘플 크기 조정
                if num_samples > 0:
                    sampled_problems = unsolved_problems_from_similar_study.sample(n=num_samples, random_state=42)
                    recommendations = pd.concat([recommendations, sampled_problems])
    
    return recommendations.drop_duplicates(subset='problem_id').sort_values(by='level', ascending=False).head(5)

@app.route('/flask', methods=['POST'])
def recommend_route():
    content = request.json
    study_id = content['study_id']
    
    # 스터디의 점수 및 유사한 스터디 찾기
    df_study_scores = fetch_study_scores()
    study_point = df_study_scores[df_study_scores['study_id'] == study_id]['score'].values[0]
    
    # 현재 스터디에서 푼 문제들을 가져오기
    df_problems = fetch_study_problems(study_id)
    solved_problem_ids = df_problems['problem_id'].values
    
    # 전체 문제에서 사용자가 풀었던 문제들을 제거
    conn = get_db_connection()
    df_problems_all = pd.read_sql('SELECT * FROM problem', conn)
    conn.close()

    if study_point <= 5000:
        # 스터디 점수가 5000 이하인 경우, 레벨 15 이하의 문제만 필터링
        unsolved_problems = df_problems_all[(~df_problems_all['problem_id'].isin(solved_problem_ids)) & (df_problems_all['level'] <= 15)]
    else:
        # 스터디 점수가 5000 이상인 경우, 모든 레벨의 문제에서 필터링
        unsolved_problems = df_problems_all[~df_problems_all['problem_id'].isin(solved_problem_ids)]

    # 협업 필터링 기반 추천 로직
    recommendations = recommend_by_collaborative_filtering(study_id, study_point, solved_problem_ids)
    
    # 대표 태그 기반 문제 추천 추가
    most_common_representative = df_problems['tag'].mode().iloc[0] if not df_problems.empty else None
    least_common_representative = df_problems['tag'].value_counts().idxmin() if not df_problems.empty else None

    recommendations_most_common = recommend_by_representative(most_common_representative, solved_problem_ids, unsolved_problems) if most_common_representative else []
    recommendations_least_common = recommend_opposite_of_least_common_tag(least_common_representative, unsolved_problems) if least_common_representative else []
    
    # 모든 추천 목록을 레벨에 따라 정렬
    recommendations_most_common = sorted(recommendations_most_common, key=lambda x: x['level'], reverse=True)
    recommendations_least_common = sorted(recommendations_least_common, key=lambda x: x['level'], reverse=True)
    
    # 결과를 원하는 형식으로 반환
    return jsonify({
        "least_common_representative_opposite": recommendations_least_common,
        "most_common_representative": recommendations_most_common,
        "recommendations": recommendations.to_dict(orient='records')
    })

@app.route('/flask/problem/<int:problem_id>', methods=['GET'])
def get_problem_html(problem_id):
    try:
        # MongoDB에서 문제 ID로 HTML을 검색
        problem = collection.find_one({"problem_id": str(problem_id)})
        if not problem:
            abort(404, description="Problem not found")

        # HTML 데이터를 반환
        return problem['html'], 200, {'Content-Type': 'text/html'}
    except Exception as e:
        abort(500, description="Internal Server Error")

if __name__ == '__main__':
    app.run(debug=True)
