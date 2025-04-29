#!/bin/bash

CONFIG_PATH="/home/ec2-user/docker-compose.yml"
INIT_FILE="/home/ec2-user/.nada"

# 도커 이미지 업데이트
sudo docker-compose -f $CONFIG_PATH pull

# 초기 실행 로직 (한 번만 실행)
if [ ! -f $INIT_FILE ]; then
    echo "First time running...."
    sudo docker-compose -f $CONFIG_PATH up -d redis
    touch $INIT_FILE
fi

# app 컨테이너 실행 (환경 변수와 볼륨 마운트 적용)
sudo docker-compose -f $CONFIG_PATH up -d --no-deps app

# fcm 컨테이너 실행 (추가 설정 없이 실행)
#sudo docker-compose -f $CONFIG_PATH up -d --no-deps fcm

# 사용하지 않는 이미지 제거
sudo docker image prune -af