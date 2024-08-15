import logging
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

# 로깅 설정
logging.basicConfig(level=logging.DEBUG)

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
        SELECT p.problem_id, p.title, p.level, p.tag, p.representative
        FROM study_problem sp
        JOIN problem p ON sp.problem_id = p.problem_id
        WHERE sp.study_id = %s
    ''', (study_id,))
    problems = cursor.fetchall()
    conn.close()
    return pd.DataFrame(problems)

def fetch_problems_by_level_range(min_level, max_level, limit=2):
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute('''
        SELECT problem_id, title, level, tag, representative
        FROM problem
        WHERE level BETWEEN %s AND %s
        ORDER BY RAND()
        LIMIT %s
    ''', (min_level, max_level, limit))
    problems = cursor.fetchall()
    conn.close()
    return pd.DataFrame(problems)

def recommend_by_representative(representative, solved_problem_ids, df_problems_unsolved):
    logging.debug(f"Representative: {representative}")
    logging.debug(f"Unsolved problems count: {len(df_problems_unsolved)}")
    
    if df_problems_unsolved.empty or representative is None:
        return []

    vectorizer = TfidfVectorizer(tokenizer=lambda x: x.split(';'), token_pattern=None)
    
    try:
        X = vectorizer.fit_transform(df_problems_unsolved['representative'])
        model = NearestNeighbors(n_neighbors=5, algorithm='auto').fit(X)
    
        vec = vectorizer.transform([representative])
        distances, indices = model.kneighbors(vec)
        recommended = df_problems_unsolved.iloc[indices[0]]
    
        recommended = recommended[~recommended['problem_id'].isin(solved_problem_ids)]
    
        if recommended.empty:
            logging.debug("No recommendations found after filtering solved problems.")
            return []
    
        return recommended.head(5).to_dict(orient='records')
    except ValueError as e:
        logging.error(f"Error in TF-IDF Vectorization: {e}")
        return []

def recommend_by_representative(representative, solved_problem_ids, df_problems_unsolved):
    if df_problems_unsolved.empty:
        return []
    
    vectorizer = TfidfVectorizer(tokenizer=lambda x: x.split(';'))
    X = vectorizer.fit_transform(df_problems_unsolved['representative'])
    model = NearestNeighbors(n_neighbors=5, algorithm='auto').fit(X)

    vec = vectorizer.transform([representative])
    distances, indices = model.kneighbors(vec)
    recommended = df_problems_unsolved.iloc[indices[0]]

    recommended = recommended[~recommended['problem_id'].isin(solved_problem_ids)]

    if recommended.empty:
        return []

    return recommended.head(5).to_dict(orient='records')

def recommend_opposite_of_least_common_representative(least_common_representative, df_problems_unsolved):
    if df_problems_unsolved.empty:
        return []

    vectorizer = TfidfVectorizer(tokenizer=lambda x: x.split(';'))
    X = vectorizer.fit_transform(df_problems_unsolved['representative'])
    model = NearestNeighbors(n_neighbors=5, algorithm='auto').fit(X)

    vec = vectorizer.transform([least_common_representative])
    distances, indices = model.kneighbors(vec)
    
    recommended = df_problems_unsolved.iloc[indices[0][::-1]]  # Reverse the indices to get the least similar ones

    if recommended.empty:
        return []

    return recommended.head(5).to_dict(orient='records')

@app.route('/flask', methods=['POST'])
def recommend_route():
    content = request.json
    study_id = content['study_id']
    
    # 현재 스터디에서 푼 문제들을 가져오기
    df_problems = fetch_study_problems(study_id)
    solved_problem_ids = df_problems['problem_id'].values
    
    # 전체 문제에서 사용자가 풀었던 문제들을 제거하고, 레벨이 15 이하인 문제만 필터링
    all_problems = pd.read_sql('SELECT * FROM problem', get_db_connection())
    unsolved_problems = all_problems[(~all_problems['problem_id'].isin(solved_problem_ids)) & (all_problems['level'] <= 15)]
    
    # 추천 문제 목록 초기화
    recommendations = pd.DataFrame(columns=['problem_id', 'title', 'level', 'tag', 'representative'])
    
    # 대표자별 문제 추천 추가
    most_common_representative = df_problems['representative'].mode().iloc[0] if not df_problems.empty else None
    least_common_representative = df_problems['representative'].value_counts().idxmin() if not df_problems.empty else None

    recommendations_most_common = recommend_by_representative(most_common_representative, solved_problem_ids, unsolved_problems) if most_common_representative else []
    recommendations_least_common = recommend_opposite_of_least_common_representative(least_common_representative, unsolved_problems) if least_common_representative else []
    
    # 모든 추천 목록을 레벨에 따라 정렬
    recommendations = unsolved_problems.sort_values(by='level', ascending=False).head(5)
    recommendations_most_common = sorted(recommendations_most_common, key=lambda x: x['level'], reverse=True)
    recommendations_least_common = sorted(recommendations_least_common, key=lambda x: x['level'], reverse=True)
    
    return jsonify({
        'recommendations': recommendations.to_dict(orient='records'),
        'most_common_representative': recommendations_most_common,
        'least_common_representative_opposite': recommendations_least_common
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
        logging.error(f"Error fetching problem: {e}")
        abort(500, description="Internal Server Error")

if __name__ == '__main__':
    app.run(debug=True)
