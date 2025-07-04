#!/bin/bash
set -e

# [0] Docker 설치 확인 및 설치
if ! command -v docker &> /dev/null; then
  echo "[INFO] Docker not found. Installing..."
  sudo yum update -y
  sudo yum install -y docker
  sudo systemctl start docker
  sudo systemctl enable docker
  sudo usermod -aG docker ec2-user
  echo "[INFO] Docker installed."
fi

# [1] docker-compose 설치 확인 및 설치
if ! command -v docker-compose &> /dev/null; then
  echo "[INFO] docker-compose not found. Installing..."
  DOCKER_COMPOSE_VERSION="v2.24.6"
  sudo curl -L "https://github.com/docker/compose/releases/download/${DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)" \
    -o /usr/local/bin/docker-compose
  sudo chmod +x /usr/local/bin/docker-compose
  echo "[INFO] docker-compose installed."
fi

CONFIG_PATH="/home/ec2-user/docker-compose.yml"
INIT_FILE="/home/ec2-user/.nada"

# [2] 기존 컨테이너 정리 (포트 변경 등 대응)
echo "[INFO] Removing existing auth and integration containers..."
sudo docker-compose -f "$CONFIG_PATH" rm -fs auth
sudo docker-compose -f "$CONFIG_PATH" rm -fs integration

# [3] 최신 이미지 Pull
echo "[INFO] Pulling latest images..."
sudo docker-compose -f "$CONFIG_PATH" pull

# [4] 최초 배포 시 Redis만 먼저 기동
if [ ! -f "$INIT_FILE" ]; then
  echo "[INFO] First time deploy: Starting redis..."
  sudo docker-compose -f "$CONFIG_PATH" up -d redis
  touch "$INIT_FILE"
fi

# [5] 모듈별 컨테이너 기동/재기동
echo "[INFO] Starting auth and integration services..."
sudo docker-compose -f "$CONFIG_PATH" up -d --no-deps auth
sudo docker-compose -f "$CONFIG_PATH" up -d --no-deps integration

# [6] 사용하지 않는 이미지 정리
echo "[INFO] Pruning unused docker images..."
sudo docker image prune -af

echo "[✅ Deploy] Done."