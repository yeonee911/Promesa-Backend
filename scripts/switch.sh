#!/bin/bash
# scripts/switch.sh

INC_FILE=/home/ubuntu/app/service_url.inc

# 현재 service_url.inc의 포트(8081 또는 8082)를 토글해서 덮어쓴다
CURRENT=$(grep -Eo '127\.0\.0\.1:808(1|2)' "$INC_FILE" | awk -F: '{print $3}')

if [ "$CURRENT" == "8081" ]; then
  # Blue(8081) → Green(8082)
  echo "server 127.0.0.1:8082;" | sudo tee "$INC_FILE" > /dev/null
  echo "Switched to Green (8082)."
else
  # Green(8082) → Blue(8081)
  echo "server 127.0.0.1:8081;" | sudo tee "$INC_FILE" > /dev/null
  echo "Switched to Blue (8081)."
fi

# nginx 설정 문법 검사 후 reload
if sudo nginx -t; then
  sudo systemctl reload nginx
  echo "Nginx reloaded successfully."
else
  echo "ERROR: nginx 설정 문법 오류. reload 취소합니다."
  exit 1
fi