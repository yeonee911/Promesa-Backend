#!/bin/bash
# scripts/clean_env.sh

if [ -d "/home/ubuntu/app/.env" ]; then
    echo "[🧹] 기존 .env 디렉토리를 삭제합니다."
    rm -rf /home/ubuntu/app/.env
fi