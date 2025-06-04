#!/bin/bash
# scripts/stop_app.sh

CONTAINER_NAME=promesa-container

# 실행 중인 컨테이너가 있으면 중지 후 삭제
EXISTS=$(docker ps -q --filter "name=${CONTAINER_NAME}")
if [ -n "$EXISTS" ]; then
  docker stop ${CONTAINER_NAME}
  docker rm ${CONTAINER_NAME}
fi