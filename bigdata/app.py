from flask import Flask, request, jsonify
import mysql.connector
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.neighbors import NearestNeighbors
from sklearn.metrics.pairwise import cosine_similarity
from dotenv import load_dotenv
import os
from flask_cors import CORS

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

def fetch_problems():
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute('SELECT * FROM problem')
    problems = cursor.fetchall()
    conn.close()
    return pd.DataFrame(problems)

def fetch_study_stats():
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute('SELECT study_id, view_count, like_count FROM study')
    study_stats = cursor.fetchall()
    conn.close()
    return pd.DataFrame(study_stats)

@app.route('/flask', methods=['POST'])
def recommend_route():
    content = request.json
    study_id = content['study_id']
    
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    
    # 대표자별 문제 풀린 횟수 집계
    cursor.execute('''
        SELECT problem.representative, COUNT(*) as count 
        FROM study_problem
        JOIN problem ON study_problem.problem_id = problem.problem_id
        WHERE study_problem.study_id = %s
        GROUP BY problem.representative
    ''', (study_id,))
    problem_counts = cursor.fetchall()
    
    if not problem_counts:
        return jsonify({
            'most_common_representative': [],
            'least_common_representative': [],
            'collaborative': []
        }), 200

    representative_counts = pd.DataFrame(problem_counts)
    most_common_representative = representative_counts.loc[representative_counts['count'].idxmax()]['representative']
    least_common_representative = representative_counts.loc[representative_counts['count'].idxmin()]['representative']
    
    df_problems = fetch_problems()
    study_stats = fetch_study_stats()
    
    # 현재 스터디가 이미 푼 문제 ID 목록 가져오기
    cursor.execute('SELECT problem_id FROM study_problem WHERE study_id = %s', (study_id,))
    solved_problems = cursor.fetchall()
    conn.close()
    
    solved_problem_ids = set(pd.DataFrame(solved_problems)['problem_id'])
    df_problems_unsolved = df_problems[~df_problems['problem_id'].isin(solved_problem_ids)]
    
    # TF-IDF와 Nearest Neighbors를 사용한 문제 추천
    vectorizer = TfidfVectorizer(tokenizer=lambda x: x.split(';'))
    X = vectorizer.fit_transform(df_problems_unsolved['representative'])
    model = NearestNeighbors(n_neighbors=5, algorithm='auto').fit(X)
    
    def recommend_by_representative(representative):
        conn = get_db_connection()
        cursor = conn.cursor(dictionary=True)
        
        vec = vectorizer.transform([representative])
        distances, indices = model.kneighbors(vec)
        recommended = df_problems_unsolved.iloc[indices[0]]
        
        # 다른 스터디들이 많이 푼 문제 우선 추천
        cursor.execute('''
            SELECT sp.problem_id, COUNT(*) as count
            FROM study_problem sp
            JOIN study s ON sp.study_id = s.study_id
            WHERE sp.problem_id IN (%s)
            GROUP BY sp.problem_id
            ORDER BY count DESC
        ''', (','.join(map(str, recommended['problem_id'].tolist())),))
        frequent_problems = cursor.fetchall()
        cursor.close()
        conn.close()
        
        if frequent_problems:
            frequent_problem_ids = [fp['problem_id'] for fp in frequent_problems]
            recommended = recommended.set_index('problem_id').loc[frequent_problem_ids].reset_index()
        
        recommended = recommended.sort_values(by='level', ascending=False).head(5)
        
        # 만약 추천할 문제가 5개 미만일 경우, 마지막 문제로 5개를 채움
        while len(recommended) < 5:
            recommended = pd.concat([recommended, recommended.iloc[[-1]]]).head(5)
        
        return recommended.to_dict(orient='records')
    
    # 1. 가장 많이 풀린 대표자에 맞는 문제 5가지
    recommendations_most_common = recommend_by_representative(most_common_representative)
    
    # 2. 가장 적게 풀린 대표자에 맞는 문제 5가지
    recommendations_least_common = recommend_by_representative(least_common_representative)
    
    # 3. 유사한 스터디 그룹이 푼 문제 중 현재 스터디가 풀지 않은 문제 5가지
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute('SELECT study_id, problem_id FROM study_problem')
    solutions = cursor.fetchall()
    conn.close()
    
    df_solutions = pd.DataFrame(solutions)
    study_problem_matrix = pd.pivot_table(df_solutions, index='study_id', columns='problem_id', aggfunc='size', fill_value=0)
    
    cosine_sim = cosine_similarity(study_problem_matrix)
    study_indices = pd.Series(study_problem_matrix.index)
    study_index = study_indices[study_indices == study_id].index[0]
    similarity_scores = pd.Series(cosine_sim[study_index]).sort_values(ascending=False)
    similar_studies = list(similarity_scores.iloc[1:].index)
    
    study_problems = set(study_problem_matrix.loc[study_id][study_problem_matrix.loc[study_id] > 0].index)
    recommendations_collaborative = []
    for idx in similar_studies:
        similar_study_problems = set(study_problem_matrix.iloc[idx][study_problem_matrix.iloc[idx] > 0].index)
        unsolved_problems = list(similar_study_problems - study_problems)
        if unsolved_problems:
            conn = get_db_connection()
            cursor = conn.cursor(dictionary=True)
            format_strings = ','.join(['%s'] * len(unsolved_problems))
            cursor.execute(f'''
                SELECT p.*, sp.study_id FROM problem p
                JOIN study_problem sp ON p.problem_id = sp.problem_id
                WHERE p.problem_id IN ({format_strings})
            ''', tuple(unsolved_problems))
            recommended_problems = cursor.fetchall()
            conn.close()
            
            df_recommended = pd.DataFrame(recommended_problems)
            df_recommended_unsolved = df_recommended[~df_recommended['problem_id'].isin(solved_problem_ids)]
            
            # 점수 부여 로직
            df_recommended_unsolved = df_recommended_unsolved.merge(study_stats, on='study_id', how='left')
            df_recommended_unsolved['score'] = df_recommended_unsolved['level'] * (
                1 + df_recommended_unsolved['view_count'] / 1000 + df_recommended_unsolved['like_count'] / 500
            )
            recommendations_collaborative.extend(
                df_recommended_unsolved.sort_values(by='score', ascending=False).head(5).to_dict(orient='records')
            )
        
        if len(recommendations_collaborative) >= 5:
            recommendations_collaborative = recommendations_collaborative[:5]
            break
    
    # 만약 5개 미만이면 같은 문제 반복하여 5개 채우기
    if len(recommendations_collaborative) < 5:
        recommendations_collaborative = recommendations_collaborative * (5 // len(recommendations_collaborative)) + recommendations_collaborative[:5 % len(recommendations_collaborative)]
    
    return jsonify({
        'most_common_representative': recommendations_most_common,
        'least_common_representative': recommendations_least_common,
        'collaborative': recommendations_collaborative
    })

if __name__ == '__main__':
    app.run(debug=True)

