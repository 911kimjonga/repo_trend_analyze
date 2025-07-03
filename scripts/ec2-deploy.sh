#!/bin/bash
set -e

# docker-compose가 설치되어 있는지 확인하고 없으면 설치
if ! command -v docker-compose &> /dev/null; then
  echo "[INFO] docker-compose not found. Installing..."
  sudo curl -L "https://github.com/docker/compose/releases/download/v2.24.6/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
  sudo chmod +x /usr/local/bin/docker-compose
fi

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