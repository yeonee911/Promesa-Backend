#!/bin/bash
# scripts/switch.sh

INC_FILE=/home/ubuntu/app/service_url.inc

# 현재 포트 읽어오기
CURRENT=$(grep -Eo '127\.0\.0\.1:808(1|2)' "$INC_FILE" | awk -F: '{print $3}')

# 반대편 포트를 대상 포트로 선택
if [ "$CURRENT" == "8081" ]; then
  # Blue(8081) → Green(8082)
  TARGET_PORT=8082
else
  # Green(8082) → Blue(8081)
  TARGET_PORT=8081
fi

echo "[INFO] 현재 포트: $CURRENT → 대상 포트: $TARGET_PORT"

# 대상 포트에 새 버전 컨테이너 실행
bash /home/ubuntu/app/scripts/start_app.sh "$TARGET_PORT"

# 헬스 체크 (Green:8082가 200을 줄 때까지 최대 30초 대기)
for i in {1..6}; do
  STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://127.0.0.1:${TARGET_PORT}/actuator/health)
  if [ "$STATUS" == "200" ]; then
    echo "Green(8082) is healthy."
    break
  fi
  echo "Waiting for Green(8082) to become healthy..."
  sleep 5
done

# nginx 설정 전환
echo "server 127.0.0.1:${TARGET_PORT};" | sudo tee "$INC_FILE" > /dev/null

# Nginx 문법 검사 후 reload
if sudo nginx -t; then
  sudo systemctl reload nginx
  echo "Nginx reloaded successfully."
else
  echo "ERROR: nginx 설정 문법 오류. reload 취소합니다."
  exit 1
fi

# 이전 포트 컨테이너 정리
if [ "$CURRENT" == "8081" ]; then
	OLD_PORT=8081
else
	OLD_PORT=8082
fi

docker rm -f promesa-${OLD_PORT}
echo "[INFO] 이전 서버 promesa-${OLD_PORT} 중지 및 삭제 완료"