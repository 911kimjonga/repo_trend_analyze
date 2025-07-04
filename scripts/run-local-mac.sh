#!/usr/bin/env bash
set -e

# [1] 현재 스크립트 위치 기준 루트 경로 계산
SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
PROJECT_ROOT=$(cd "$SCRIPT_DIR/.." && pwd)
cd "$PROJECT_ROOT"

# Java 경로 (Mac에서 설치된 Corretto 사용)
export JAVA_HOME="/Users/gimjong-won/Library/Java/JavaVirtualMachines/corretto-21.0.6/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"

# .env 로드
set -a
[ -f "$PROJECT_ROOT/.env" ] && source "$PROJECT_ROOT/.env"
set +a

# Gradle 실행
./gradlew :auth:bootRun --args='--spring.profiles.active=auth,local' &
PID1=$!
./gradlew :integration:bootRun --args='--spring.profiles.active=integration,local' &
PID2=$!
./gradlew :messaging:bootRun --args='--spring.profiles.active=messaging,local' &
PID3=$!

wait $PID1
wait $PID2
wait $PID3