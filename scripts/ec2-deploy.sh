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

# [2] 최신 이미지 Pull
echo "[INFO] Pulling latest images..."
sudo docker-compose -f "$CONFIG_PATH" pull auth
sudo docker-compose -f "$CONFIG_PATH" pull integration
sudo docker-compose -f "$CONFIG_PATH" pull messaging
sudo docker-compose -f "$CONFIG_PATH" pull kafka
sudo docker-compose -f "$CONFIG_PATH" pull zookeeper

# [3] Kafka 및 Zookeeper 기동 상태 확인 및 기동
KAFKA_RUNNING=$(docker ps --filter "name=kafka" --filter "status=running" -q)
ZOOKEEPER_RUNNING=$(docker ps --filter "name=zookeeper" --filter "status=running" -q)

if [ -z "$ZOOKEEPER_RUNNING" ]; then
  echo "[INFO] Starting zookeeper..."
  sudo docker-compose -f "$CONFIG_PATH" up -d zookeeper
fi

if [ -z "$KAFKA_RUNNING" ]; then
  echo "[INFO] Starting kafka..."
  sudo docker-compose -f "$CONFIG_PATH" up -d kafka
fi

# [4] 최초 배포 시 Redis만 먼저 기동
if [ ! -f "$INIT_FILE" ]; then
  echo "[INFO] First time deploy: Starting redis..."
  sudo docker-compose -f "$CONFIG_PATH" up -d redis-auth redis-service
  touch "$INIT_FILE"
fi

# [5] 모듈별 컨테이너 기동/재기동
echo "[INFO] Starting auth and integration services..."
sudo docker-compose -f "$CONFIG_PATH" up -d --no-deps auth
sudo docker-compose -f "$CONFIG_PATH" up -d --no-deps integration
sudo docker-compose -f "$CONFIG_PATH" up -d --no-deps messaging

# [6] 사용하지 않는 이미지 정리
echo "[INFO] Pruning unused docker images..."
sudo docker image prune -af

echo "[✅ Deploy] Done."