from flask import Flask, request, jsonify
import mysql.connector
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.neighbors import NearestNeighbors
from sklearn.metrics.pairwise import cosine_similarity
from dotenv import load_dotenv
import os
from flask_cors import CORS
import json

# .env 파일에서 환경 변수 로드
load_dotenv()

app = Flask(__name__)
CORS(app, supports_credentials=True)

def get_db_connection():
    conn = mysql.connector.connect(
        host=os.getenv('DB_HOST'),
        user=os.getenv('DB_USER'),
        password=os.getenv('DB_PASSWORD'),
        database=os.getenv('DB_NAME')
    )
    return conn

def fetch_study_scores():
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute('SELECT study_id, study_point + solve_count as score FROM Study')
    study_scores = cursor.fetchall()
    conn.close()
    return pd.DataFrame(study_scores)

def fetch_study_problems(study_id):
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute('''
        SELECT p.problem_id, p.title, p.level, p.tag, p.representative
        FROM StudyProblem sp
        JOIN Problem p ON sp.problem_id = p.problem_id
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
        FROM Problem
        WHERE level BETWEEN %s AND %s
        ORDER BY RAND()
        LIMIT %s
    ''', (min_level, max_level, limit))
    problems = cursor.fetchall()
    conn.close()
    return pd.DataFrame(problems)

def recommend_by_representative(representative, solved_problem_ids, df_problems_unsolved):
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

@app.route('/flask', methods=['POST'])
def recommend_route():
    content = request.json
    study_id = content['study_id']
    
    # 현재 스터디의 점수 및 모든 스터디의 점수 불러오기
    df_study_scores = fetch_study_scores()
    study_score = df_study_scores[df_study_scores['study_id'] == study_id]['score'].values[0]
    
    # 추천 문제 목록 초기화
    recommendations = pd.DataFrame(columns=['problem_id', 'title', 'level', 'tag', 'representative'])
    
    df_problems = fetch_study_problems(study_id)
    solved_problem_ids = df_problems['problem_id'].values
    df_problems_unsolved = df_problems[~df_problems['problem_id'].isin(solved_problem_ids)]
    
    if study_score <= 2000:
        # 5 이하의 스터디인 경우
        recommendations = pd.concat([
            recommendations,
            fetch_problems_by_level_range(15, 20, 1),
            fetch_problems_by_level_range(10, 15, 2),
            fetch_problems_by_level_range(5, 10, 1),
            fetch_problems_by_level_range(1, 5, 1)
        ])
    elif study_score <= 4000:
        # 2000 이하의 스터디인 경우
        recommendations = pd.concat([
            recommendations,
            fetch_problems_by_level_range(10, 15, 2),
            fetch_problems_by_level_range(5, 10, 2),
            fetch_problems_by_level_range(1, 5, 1)
        ])
    else:
        # 2000 이상의 스터디인 경우 (기본 로직)
        study_scores_matrix = df_study_scores.drop(columns=['study_id'])
        study_index = df_study_scores[df_study_scores['study_id'] == study_id].index[0]
        cosine_sim = cosine_similarity(study_scores_matrix)
        similarity_scores = pd.Series(cosine_sim[study_index]).sort_values(ascending=False)
        similar_studies = df_study_scores.iloc[similarity_scores.index[1:6]]['study_id'].values
        
        solved_problems = fetch_study_problems(study_id)['problem_id'].values
        
        for similar_study_id in similar_studies:
            similar_study_problems = fetch_study_problems(similar_study_id)
            unsolved_problems = similar_study_problems[~similar_study_problems['problem_id'].isin(solved_problems)]
            recommendations = pd.concat([recommendations, unsolved_problems])
        
        recommendations = recommendations.drop_duplicates(subset='problem_id').sort_values(by=['level'], ascending=False).head(5)
    
    # 대표자별 문제 추천 추가
    most_common_representative = df_problems['representative'].mode().iloc[0]
    least_common_representative = df_problems['representative'].value_counts().idxmin()

    recommendations_most_common = recommend_by_representative(most_common_representative, solved_problem_ids, df_problems_unsolved)
    recommendations_least_common = recommend_by_representative(least_common_representative, solved_problem_ids, df_problems_unsolved)
    
    return jsonify({
        'recommendations': recommendations.to_dict(orient='records'),
        'most_common_representative': recommendations_most_common,
        'least_common_representative': recommendations_least_common
    })

# 새로운 엔드포인트: 문제 ID를 받아서 해당 문제의 상세 정보를 반환
@app.route('/flask/problem/<int:problem_id>', methods=['GET'])
def get_problem_details(problem_id):
    for problem in problem_data:
        if problem["problem_id"] == str(problem_id):
            return jsonify(problem)
    return jsonify({"error": "Problem not found"}), 404

if __name__ == '__main__':
    app.run(debug=True)
