#!/bin/bash
# scripts/before_install.sh

# .env 백업
if [ -f "/home/ubuntu/app/.env" ]; then
  cp /home/ubuntu/app/.env /home/ubuntu/.env.backup
fi

CONTAINER_NAME=promesa-container

# 기존에 실행 중인 "promesa-container"가 있으면 중지 후 제거
EXISTS=$(docker ps -q --filter "name=${CONTAINER_NAME}")
if [ -n "$EXISTS" ]; then
  docker stop ${CONTAINER_NAME}
  docker rm ${CONTAINER_NAME}
fi