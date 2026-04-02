#!/bin/bash

# 数据库初始化脚本

echo "=========================================="
echo "ProjMaster 数据库初始化脚本"
echo "=========================================="

# 数据库配置
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-3306}
DB_USER=${DB_USER:-root}
DB_PASS=${DB_PASS:-root}
DB_NAME=${DB_NAME:-projmaster}

echo "连接数据库: $DB_HOST:$DB_PORT"
echo "用户名: $DB_USER"
echo "数据库名: $DB_NAME"
echo ""

# 创建数据库
echo "正在创建数据库..."
mysql -h $DB_HOST -P $DB_PORT -u $DB_USER -p$DB_PASS -e "CREATE DATABASE IF NOT EXISTS $DB_NAME CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

if [ $? -eq 0 ]; then
    echo "✓ 数据库创建成功"
else
    echo "✗ 数据库创建失败"
    exit 1
fi

# 执行Flyway迁移
echo ""
echo "正在执行数据库迁移..."
cd ../backend
./mvnw flyway:migrate -Dflyway.url=jdbc:mysql://$DB_HOST:$DB_PORT/$DB_NAME -Dflyway.user=$DB_USER -Dflyway.password=$DB_PASS

if [ $? -eq 0 ]; then
    echo "✓ 数据库迁移成功"
else
    echo "✗ 数据库迁移失败，尝试直接执行SQL脚本..."
    mysql -h $DB_HOST -P $DB_PORT -u $DB_USER -p$DB_PASS $DB_NAME < src/main/resources/db/migration/V1__init_schema.sql
    mysql -h $DB_HOST -P $DB_PORT -u $DB_USER -p$DB_PASS $DB_NAME < src/main/resources/db/migration/V2__insert_initial_data.sql
fi

echo ""
echo "=========================================="
echo "数据库初始化完成!"
echo "=========================================="
echo ""
echo "默认测试账号:"
echo "  管理员: admin / 123456"
echo "  项目经理: pm1 / 123456"
echo "  产品经理: po1 / 123456"
echo "  开发: dev1 / 123456"
echo "  测试: test1 / 123456"
