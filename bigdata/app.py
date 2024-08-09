from flask import Flask, request, jsonify
import mysql.connector
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.neighbors import NearestNeighbors
from sklearn.metrics.pairwise import cosine_similarity
from dotenv import load_dotenv
import os

# .env 파일에서 환경 변수 로드
load_dotenv()

app = Flask(__name__)

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
        SELECT problem.tag, COUNT(*) as count FROM problem_solution
        JOIN problem ON problem_solution.problem_id = problem.problem_id
        WHERE problem_solution.study_id = %s
        GROUP BY problem.tag
    ''', (study_id,))
    problem_counts = cursor.fetchall()
    conn.close()
    
    if not problem_counts:
        return jsonify({
            'most_common_tag': [],
            'least_common_tag': [],
            'collaborative': []
        }), 200
    
    tag_counts = pd.DataFrame(problem_counts)
    
    most_common_tag = tag_counts.loc[tag_counts['count'].idxmax()]['tag']
    least_common_tag = tag_counts.loc[tag_counts['count'].idxmin()]['tag']
    
    df_problems = fetch_problems()
    
    vectorizer = TfidfVectorizer(tokenizer=lambda x: x.split(';'))
    X = vectorizer.fit_transform(df_problems['tag'])
    model = NearestNeighbors(n_neighbors=5, algorithm='auto').fit(X)
    
    def recommend_by_tag(tag):
        vec = vectorizer.transform([tag])
        distances, indices = model.kneighbors(vec)
        return df_problems.iloc[indices[0]].head(5).to_dict(orient='records')
    
    # 1. 가장 많이 풀린 태그에 맞는 문제 5가지
    recommendations_most_common = recommend_by_tag(most_common_tag)
    
    # 2. 가장 적게 풀린 태그에 맞는 문제 5가지
    recommendations_least_common = recommend_by_tag(least_common_tag)
    
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
        recommendations_collaborative.extend(list(similar_study_problems - study_problems))
        if len(recommendations_collaborative) >= 5:
            break
    
    recommendations_collaborative = recommendations_collaborative[:5]
    
    if recommendations_collaborative:
        conn = get_db_connection()
        cursor = conn.cursor(dictionary=True)
        format_strings = ','.join(['%s'] * len(recommendations_collaborative))
        cursor.execute(f'''
            SELECT * FROM problem WHERE problem_id IN ({format_strings})
        ''', tuple(recommendations_collaborative))
        recommended_problems = cursor.fetchall()
        conn.close()
        
        recommendations_collaborative = pd.DataFrame(recommended_problems).to_dict(orient='records')
    else:
        recommendations_collaborative = []
    
    return jsonify({
        'most_common_tag': recommendations_most_common,
        'least_common_tag': recommendations_least_common,
        'collaborative': recommendations_collaborative
    })

if __name__ == '__main__':
    app.run(debug=True)








