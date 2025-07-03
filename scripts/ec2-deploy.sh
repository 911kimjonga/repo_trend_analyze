#!/bin/bash
set -e

CONFIG_PATH="/home/ec2-user/docker-compose.yml"
INIT_FILE="/home/ec2-user/.nada"

# 1. 최신 이미지 Pull
sudo docker-compose -f "$CONFIG_PATH" pull

# 2. 최초 배포 시 Redis만 먼저 기동
if [ ! -f "$INIT_FILE" ]; then
  echo "[Init] Starting redis..."
  sudo docker-compose -f "$CONFIG_PATH" up -d redis
  touch "$INIT_FILE"
fi

# 3. 모듈별 컨테이너 기동/재기동
sudo docker-compose -f "$CONFIG_PATH" up -d --no-deps auth
sudo docker-compose -f "$CONFIG_PATH" up -d --no-deps integration

# 4. 사용하지 않는 이미지 정리
sudo docker image prune -af
echo "[Deploy] Done."