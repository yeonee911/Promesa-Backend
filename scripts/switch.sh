#!/bin/bash
# scripts/switch.sh

# /home/ubuntu/app/service_url.inc 파일을 Blue(8081) → Green(8082)으로 교체
echo "set \$service_url 127.0.0.1:8082;" | sudo tee /home/ubuntu/app/service_url.inc

# nginx 설정 재적용
sudo systemctl reload nginx