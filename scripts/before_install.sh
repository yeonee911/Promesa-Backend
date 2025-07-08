#!/bin/bash
# scripts/before_install.sh

# .env 백업
if [ -f "/home/ubuntu/app/.env" ]; then
  cp /home/ubuntu/app/.env /home/ubuntu/.env.backup
fi

# 사용하지 않는 Docker 이미지 정리
echo "> 사용하지 않는 Docker 이미지 정리 시작"
docker image prune -af
echo "> 이미지 정리 완료"