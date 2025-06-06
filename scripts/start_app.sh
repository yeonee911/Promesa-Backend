#!/bin/bash
# scripts/start_app.sh

# 1) 환경 변수 설정
AWS_REGION=ap-northeast-2
AWS_ACCOUNT_ID=966821290601
ECR_REPOSITORY=promesa-repo
ECR_REGISTRY=${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com

# 2) ECR에서 'latest' 이미지를 pull
aws ecr get-login-password --region ${AWS_REGION} | \
  docker login --username AWS --password-stdin ${ECR_REGISTRY}

docker pull ${ECR_REGISTRY}/${ECR_REPOSITORY}:latest

# 3) 기존에 같은 이름의 컨테이너가 있으면 삭제
if docker ps -a --format '{{.Names}}' | grep -q '^promesa-container$'; then
  docker rm -f promesa-container
fi

# 4) 새 컨테이너 실행 (Green:8082)

echo ">> About to run Docker with SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE, RDS_URL=$RDS_URL"

docker run -d \
  --name promesa-container \
  --network host \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e RDS_URL="jdbc:mysql://${RDS_URL}/promesa_db" \
  -e RDS_USERNAME="${RDS_USERNAME}" \
  -e RDS_PASSWORD="${RDS_PASSWORD}" \
  -e REDIS_HOST="${REDIS_HOST}" \
  -e REDIS_PORT="${REDIS_PORT}" \
  ${ECR_REGISTRY}/${ECR_REPOSITORY}:latest

# 4) 헬스 체크 (Green:8082가 200을 줄 때까지 최대 30초 대기)
for i in {1..6}; do
  STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://127.0.0.1:8082/actuator/health)
  if [ "$STATUS" == "200" ]; then
    echo "Green(8082) is healthy."
    break
  fi
  echo "Waiting for Green(8082) to become healthy..."
  sleep 5
done

# 6) switch.sh 호출
bash /home/ubuntu/app/scripts/switch.sh