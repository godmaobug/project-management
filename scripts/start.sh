#!/bin/bash

# 一键启动脚本

echo "=========================================="
echo "ProjMaster 一键启动脚本"
echo "=========================================="

# 检查端口占用
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null 2>&1; then
        echo "✗ 端口 $1 已被占用"
        return 1
    else
        echo "✓ 端口 $1 可用"
        return 0
    fi
}

echo "检查端口..."
check_port 8080 || exit 1
check_port 5173 || exit 1

echo ""
echo "启动后端服务..."
cd backend
./mvnw spring-boot:run > ../logs/backend.log 2>&1 &
BACKEND_PID=$!
echo "后端服务PID: $BACKEND_PID"

echo "等待后端服务启动..."
sleep 30

echo ""
echo "启动前端服务..."
cd ../frontend
npm install > ../logs/npm-install.log 2>&1
npm run dev > ../logs/frontend.log 2>&1 &
FRONTEND_PID=$!
echo "前端服务PID: $FRONTEND_PID"

echo ""
echo "=========================================="
echo "服务启动成功!"
echo "=========================================="
echo ""
echo "访问地址:"
echo "  前端: http://localhost:5173"
echo "  后端: http://localhost:8080"
echo ""
echo "日志文件:"
echo "  后端: logs/backend.log"
echo "  前端: logs/frontend.log"
echo ""
echo "停止服务:"
echo "  kill $BACKEND_PID"
echo "  kill $FRONTEND_PID"
echo ""

# 保存PID
mkdir -p ../logs
echo $BACKEND_PID > ../logs/backend.pid
echo $FRONTEND_PID > ../logs/frontend.pid
