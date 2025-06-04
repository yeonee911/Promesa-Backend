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

# 3) Docker 컨테이너 실행 (Green 환경: 호스트 8082 → 컨테이너 8080)
docker run -d \
  --name promesa-container \
  -p 8082:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://prod-db.ap-northeast-2.rds.amazonaws.com:3306/promesa \
  -e SPRING_DATASOURCE_USERNAME=promesa_user \
  -e SPRING_DATASOURCE_PASSWORD=promesa_pass \
  -e SPRING_REDIS_HOST=prod-redis.apne1.cache.amazonaws.com:6379 \
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

# 5) nginx 포트 전환 (Blue 8081 → Green 8082)
bash /home/ubuntu/app/scripts/switch.sh