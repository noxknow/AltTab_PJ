import mysql.connector
import random
from dotenv import load_dotenv
import os

# .env 파일에서 환경 변수 로드
load_dotenv()

def get_db_connection():
    conn = mysql.connector.connect(
        host=os.getenv('DB_HOST'),
        user=os.getenv('DB_USER'),
        password=os.getenv('DB_PASSWORD'),
        database=os.getenv('DB_NAME')
    )
    return conn

def update_representative():
    conn = get_db_connection()
    cursor = conn.cursor()

    # 8개의 대표자 값 중 하나를 무작위로 선택
    representatives = ['수학', '구현', '다이나믹 프로그래밍', '자료구조', '그래프 이론', '문자열', '정렬', '탐색']

    # problem 테이블에서 모든 problem_id를 가져옴
    cursor.execute("SELECT problem_id FROM problem")
    problem_ids = cursor.fetchall()

    # 각 problem_id에 대해 무작위 representative 값을 업데이트
    for problem_id in problem_ids:
        random_representative = random.choice(representatives)
        cursor.execute('''
            UPDATE problem
            SET representative = %s
            WHERE problem_id = %s
        ''', (random_representative, problem_id[0]))

    conn.commit()
    conn.close()
    print("모든 행의 representative 값이 무작위로 업데이트되었습니다.")

if __name__ == '__main__':
    update_representative()
