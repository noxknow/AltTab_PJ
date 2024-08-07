# 베이스 이미지 설정
FROM python:3.9

# 작업 디렉토리 설정
WORKDIR /app

# 필요 패키지 복사 및 설치
COPY requirements.txt requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

# 애플리케이션 소스 코드 복사
COPY . .

# Gunicorn으로 애플리케이션 실행
CMD ["gunicorn", "-b", "0.0.0.0:8000", "app:app"]
