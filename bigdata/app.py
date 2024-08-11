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
        host=os.getenv('DB_HOST'),         # MySQL 서버 호스트명
        user=os.getenv('DB_USER'),         # MySQL 사용자 이름
        password=os.getenv('DB_PASSWORD'), # MySQL 사용자 비밀번호
        database=os.getenv('DB_NAME')      # MySQL 데이터베이스 이름
    )
    return conn

def fetch_problems():
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute('SELECT * FROM problem')
    problems = cursor.fetchall()
    conn.close()
    problems_df = pd.DataFrame(problems)
    return problems_df

@app.route('/flask', methods=['POST'])
def recommend_route():
    content = request.json
    study_id = content['study_id']
    
    
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute('''
        SELECT problem.representative, COUNT(*) as count FROM problem_solution
        JOIN problem ON problem_solution.problem_id = problem.problem_id
        WHERE problem_solution.study_id = %s
        GROUP BY problem.representative
    ''', (study_id,))
    problem_counts = cursor.fetchall()
    conn.close()
    
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
    
    # 현재 스터디가 이미 푼 문제 ID 목록 가져오기
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute('SELECT problem_id FROM problem_solution WHERE study_id = %s', (study_id,))
    solved_problems = cursor.fetchall()
    conn.close()
    
    solved_problem_ids = set(pd.DataFrame(solved_problems)['problem_id'])
    df_problems_unsolved = df_problems[~df_problems['problem_id'].isin(solved_problem_ids)]
    
    vectorizer = TfidfVectorizer(tokenizer=lambda x: x.split(';'))
    X = vectorizer.fit_transform(df_problems_unsolved['representative'])
    model = NearestNeighbors(n_neighbors=5, algorithm='auto').fit(X)
    
    def recommend_by_representative(representative):
        vec = vectorizer.transform([representative])
        distances, indices = model.kneighbors(vec)
        recommended = df_problems_unsolved.iloc[indices[0]].sort_values(by='level', ascending=False).head(5)
        
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
    cursor.execute('SELECT study_id, problem_id FROM problem_solution')
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
                SELECT * FROM problem WHERE problem_id IN ({format_strings})
            ''', tuple(unsolved_problems))
            recommended_problems = cursor.fetchall()
            conn.close()
            
            df_recommended = pd.DataFrame(recommended_problems)
            df_recommended_unsolved = df_recommended[~df_recommended['problem_id'].isin(solved_problem_ids)]
            recommendations_collaborative.extend(df_recommended_unsolved.sort_values(by='level', ascending=False).head(5).to_dict(orient='records'))
        
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



