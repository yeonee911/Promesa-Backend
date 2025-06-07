#!/bin/bash
# scripts/after_install.sh

# .env 복원
if [ -f "/home/ubuntu/.env.backup" ]; then
  cp /home/ubuntu/.env.backup /home/ubuntu/app/.env
fi

chmod +x /home/ubuntu/app/scripts/*.sh

rm -f /home/ubuntu/.env.backup