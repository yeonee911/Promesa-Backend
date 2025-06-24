#!/bin/bash
# scripts/start_app.sh

# 전달된 포트 인자 받기
TARGET_PORT=${1:-8082}

# .env 파일 로드
source /home/ubuntu/app/.env

# 환경 변수 설정
AWS_REGION=ap-northeast-2
AWS_ACCOUNT_ID=966821290601
ECR_REPOSITORY=promesa-repo
ECR_REGISTRY=${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com

DB_ENDPOINT="promesa-rds.cdse0qo4km1e.ap-northeast-2.rds.amazonaws.com"
DB_PORT=3306
DB_NAME="promesa_db"

CONTAINER_NAME=promesa-${TARGET_PORT}

# ECR에서 'latest' 이미지를 pull
aws ecr get-login-password --region ${AWS_REGION} | \
  docker login --username AWS --password-stdin ${ECR_REGISTRY}

# 기존 latest image 삭제 → 강제 최신 pull 가능
docker rmi -f ${ECR_REGISTRY}/${ECR_REPOSITORY}:latest || true
docker pull ${ECR_REGISTRY}/${ECR_REPOSITORY}:latest

# 기존 동일 포트 컨테이너가 있다면 삭제
if docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
	docker rm -f ${CONTAINER_NAME}
fi

# 4) 새 컨테이너 실행
docker run -d \
  --name ${CONTAINER_NAME} \
  -p ${TARGET_PORT}:8081 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e RDS_URL="jdbc:mysql://${DB_ENDPOINT}:${DB_PORT}/${DB_NAME}?characterEncoding=UTF-8&serverTimezone=Asia/Seoul" \
  -e RDS_USERNAME="${RDS_USERNAME}" \
  -e RDS_PASSWORD="${RDS_PASSWORD}" \
  -e REDIS_HOST="${REDIS_HOST}" \
  -e REDIS_PORT="${REDIS_PORT}" \
  ${ECR_REGISTRY}/${ECR_REPOSITORY}:latest
