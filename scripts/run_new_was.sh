#!/bin/bash

echo "> Start run_new_was.sh"

if [ -f "/home/ubuntu/app/.env" ]; then
  echo "> .env 파일을 발견하여 환경변수로 등록합니다."
  export $(grep -v '^#' /home/ubuntu/app/.env | xargs)
else
  echo "> 경고: /home/ubuntu/app/.env 파일을 찾을 수 없습니다."
fi

# Parse port number from 'service_url.inc'
CURRENT_PORT=$(cat /home/ubuntu/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Current port of running WAS is ${CURRENT_PORT}."

# Find target port to switch
if [ ${CURRENT_PORT} -eq 8081 ]; then
    TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
    TARGET_PORT=8081
else
    echo "> No WAS is connected to nginx"
fi

echo "> Kill WAS running at ${TARGET_PORT}."
sudo kill $(sudo lsof -t -i:${TARGET_PORT})

nohup sudo java -jar -Dspring.profiles.active=dev -Dserver.port=${TARGET_PORT} /home/ubuntu/app/build/libs/backend-0.0.1-SNAPSHOT.jar > /home/ubuntu/nohup.out 2>&1 &
echo "> Now new WAS runs at ${TARGET_PORT}."
exit 0

