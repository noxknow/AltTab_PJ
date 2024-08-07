from flask import Flask, request, jsonify
import sqlite3
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.neighbors import NearestNeighbors
from sklearn.metrics.pairwise import cosine_similarity

app = Flask(__name__)

def get_db_connection():
    conn = sqlite3.connect('alttab_db.db')
    conn.row_factory = sqlite3.Row
    return conn

def fetch_problems():
    conn = get_db_connection()
    problems = conn.execute('SELECT * FROM problem').fetchall()
    conn.close()
    problems_df = pd.DataFrame(problems, columns=['problem_id', 'title', 'tag', 'level'])
    return problems_df

@app.route('/')
def home():
    return "AI 문제 추천 시스템이 실행 중입니다."

@app.route('/recommend', methods=['POST'])
def recommend_route():
    content = request.json
    study_id = content['study_id']
    
    conn = get_db_connection()
    problem_counts = conn.execute('''
        SELECT tag, COUNT(*) as count FROM problem_solution
        JOIN problem ON problem_solution.problem_id = problem.problem_id
        WHERE study_id = ?
        GROUP BY tag
    ''', (study_id,)).fetchall()
    conn.close()
    
    if not problem_counts:
        return jsonify({
            'most_common_tag': [],
            'least_common_tag': [],
            'collaborative': []
        }), 200
    
    tag_counts = pd.DataFrame(problem_counts, columns=['tag', 'count'])
    
    most_common_tag = tag_counts.loc[tag_counts['count'].idxmax()]['tag']
    least_common_tag = tag_counts.loc[tag_counts['count'].idxmin()]['tag']
    
    df_problems = fetch_problems()
    
    vectorizer = TfidfVectorizer()
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
    solutions = conn.execute('SELECT study_id, problem_id FROM problem_solution').fetchall()
    conn.close()
    
    df_solutions = pd.DataFrame(solutions, columns=['study_id', 'problem_id'])
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
        recommended_problems = conn.execute(f'''
            SELECT * FROM problem WHERE problem_id IN ({','.join('?' for _ in recommendations_collaborative)})
        ''', recommendations_collaborative).fetchall()
        conn.close()
        
        recommendations_collaborative = pd.DataFrame(recommended_problems, columns=['problem_id', 'title', 'tag', 'level']).to_dict(orient='records')
    else:
        recommendations_collaborative = []
    
    return jsonify({
        'most_common_tag': recommendations_most_common,
        'least_common_tag': recommendations_least_common,
        'collaborative': recommendations_collaborative
    })

if __name__ == '__main__':
    app.run(debug=True)










