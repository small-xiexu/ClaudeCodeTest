#!/bin/bash

echo "=========================================="
echo "      图书管理系统 Docker 部署"
echo "=========================================="
echo ""

# 停止并删除旧容器
echo "[1/4] 清理旧容器..."
docker-compose down -v

# 构建镜像
echo ""
echo "[2/4] 构建 Docker 镜像..."
docker-compose build --no-cache

# 启动服务
echo ""
echo "[3/4] 启动服务..."
docker-compose up -d

# 等待服务启动
echo ""
echo "[4/4] 等待服务启动..."
echo "等待 MySQL 启动..."
sleep 10

echo "等待后端应用启动..."
for i in {1..30}; do
  if curl -s http://localhost:8080/api/categories > /dev/null 2>&1; then
    echo "✓ 后端应用启动成功！"
    break
  fi
  echo -n "."
  sleep 2
done

echo ""
echo "=========================================="
echo "          部署完成！"
echo "=========================================="
echo ""
echo "服务访问地址："
echo "  - API 文档: http://localhost:8080/api/swagger-ui.html"
echo "  - API 端点: http://localhost:8080/api"
echo ""
echo "管理命令："
echo "  - 查看日志: docker-compose logs -f"
echo "  - 停止服务: docker-compose down"
echo "  - 重启服务: docker-compose restart"
echo ""
