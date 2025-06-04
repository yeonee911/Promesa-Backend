#!/bin/bash
# scripts/after_install.sh

# Docker 이미지 pull/run도 스크립트 내부에서 할 것이므로, 특별히 할 작업이 없다면
# 스크립트에 실행 권한을 다시 한 번 보장해 두는 정도로만 둡니다.

chmod +x /home/ubuntu/app/scripts/*.sh