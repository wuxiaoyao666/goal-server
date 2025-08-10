#!/bin/bash

# 定义路径变量（统一结尾不加/，拼接时手动添加，避免重复）
BASE_DIR="/opt/soft/goal"
CLIENT_NAME="goal-client"
SERVER_NAME="goal-server"
SERVER_DIR="/opt/soft/nginx"
SERVER_RESOURCE_DIR="html/pc"
FRONT_STATIC_DIR="dist"

# 日志输出函数（新增，用于统一日志格式）
log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1"
}

# 查找目标进程的PID（提前定义，在使用前）
get_pids() {
    ps -ef | grep "java" | grep "$SERVER_NAME" | grep -v grep | awk '{print $2}'
}

log "开始部署$CLIENT_NAME..."

# 进入客户端目录（修正变量拼接）
CLIENT_DIR="$BASE_DIR/$CLIENT_NAME"
log "进入客户端目录: $CLIENT_DIR"
cd "$CLIENT_DIR" || {
    log "ERROR: 无法进入客户端目录 $CLIENT_DIR"
    exit 1
}
# 先删除目标目录的旧dist
rm -rf "$FRONT_STATIC_DIR"

# 拉取代码并构建
log "拉取客户端最新代码..."
git pull || {
    log "ERROR: git pull 失败"
    exit 1
}

log "开始打包客户端..."
pnpm build || {
    log "ERROR: pnpm build 失败"
    exit 1
}

# 检查dist目录是否存在
if [ ! -d "dist" ]; then
    log "ERROR: 客户端打包失败，未找到dist目录"
    exit 1
fi

# 拷贝dist到服务端资源目录
SERVER_RESOURCE_DIR="$SERVER_DIR/$SERVER_RESOURCE_DIR"
log "正在拷贝dist到服务端资源目录: $SERVER_RESOURCE_DIR"

# 删除旧资源 我勒个豆，“” 中不能写 * 不然 寄了
rm -rf $SERVER_RESOURCE_DIR/*
mv $FRONT_STATIC_DIR/* "$SERVER_RESOURCE_DIR" || {
    log "ERROR: 移动$CLIENT_DIR/$FRONT_STATIC_DIR/* 到 $SERVER_RESOURCE_DIR 失败"
    exit 1
}

# 部署服务端
log "开始部署$SERVER_NAME..."
SERVER_DIR="$BASE_DIR/$SERVER_NAME"
cd "$SERVER_DIR" || {
    log "ERROR: 无法进入服务端目录 $SERVER_DIR"
    exit 1
}

log "拉取服务端最新代码..."
git pull || {
    log "ERROR: 服务端git pull 失败"
    exit 1
}

log "开始打包服务端..."
mvn clean package || {
    log "ERROR: mvn打包失败"
    exit 1
}

# 终止旧服务进程
log "检查并终止旧服务进程..."
PIDS=$(get_pids)
if [ -z "$PIDS" ]; then
    log "未找到 $SERVER_NAME 相关的Java进程，无需终止"
else
    log "找到以下进程需要终止：$PIDS"
    for PID in $PIDS; do
        kill "$PID" >/dev/null 2>&1
        sleep 1
        if ps -p "$PID" >/dev/null 2>&1; then
            log "进程 $PID 终止失败，尝试强制终止..."
            kill -9 "$PID" >/dev/null 2>&1
            if [ $? -eq 0 ]; then
                log "进程 $PID 已强制终止"
            else
                log "ERROR: 进程 $PID 强制终止失败"
                exit 1
            fi
        else
            log "进程 $PID 已正常终止"
        fi
    done
fi

# 移动jar包并重启服务
log "准备启动新服务..."
# 移动jar包到BASE_DIR（使用绝对路径避免相对路径问题）
JAR_FILE=$(find target -name "$SERVER_NAME*.jar" | head -n 1)  # 找到最新的jar包
if [ -z "$JAR_FILE" ]; then
    log "ERROR: 未找到服务端jar包"
    exit 1
fi
mv "$JAR_FILE" "$BASE_DIR/" || {
    log "ERROR: 移动jar包失败"
    exit 1
}

# 切换到BASE_DIR启动服务（避免路径问题）
cd "$BASE_DIR" || {
    log "ERROR: 无法进入BASE_DIR $BASE_DIR"
    exit 1
}

# 清理旧日志，启动新服务
rm -rf nohup.out
log "启动服务:$JAR_FILE"
nohup java -jar "$(basename "$JAR_FILE")" &

# 检查启动是否成功
sleep 3
NEW_PIDS=$(get_pids)
if [ -n "$NEW_PIDS" ]; then
    log "服务启动成功，进程ID: $NEW_PIDS"
else
    log "ERROR: 服务启动失败，请检查nohup.out日志"
    exit 1
fi

log "部署完成！"
exit 0