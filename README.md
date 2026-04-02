# ProjMaster - 项目管理系统

基于 Spring Boot + Vue 3 的前后端分离项目管理系统。

## 技术栈

### 后端
- **框架**: Spring Boot 3.2.4
- **数据库**: MySQL 8.0
- **ORM**: Spring Data JPA + Hibernate
- **安全**: Spring Security + JWT
- **迁移**: Flyway
- **构建工具**: Maven

### 前端
- **框架**: Vue 3 + Vite
- **UI库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **图表**: ECharts
- **HTTP**: Axios

## 功能特性

- [x] 用户认证与授权 (JWT)
- [x] 项目管理 (CRUD、成员管理、归档)
- [x] 需求管理 (需求池、优先级、状态流转)
- [x] 任务管理 (任务分解、指派、工时预估)
- [x] 缺陷管理 (Bug生命周期、严重程度)
- [x] 迭代管理 (Sprint规划、容量规划)
- [x] 看板视图 (拖拽式任务管理)
- [x] 统计报表 (图表展示、项目进度)
- [x] 仪表盘 (待办事项、项目概览)

## 快速开始

### 环境要求
- JDK 17+
- MySQL 8.0+
- Node.js 18+
- Maven 3.8+

### 1. 克隆项目

```bash
cd projmaster
```

### 2. 初始化数据库

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE projmaster CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 或者使用提供的SQL脚本
cd backend/src/main/resources/db/migration
mysql -u root -p projmaster < V1__init_schema.sql
mysql -u root -p projmaster < V2__insert_initial_data.sql
```

### 3. 启动后端服务

```bash
cd backend
# 修改 application.yml 中的数据库配置
# 然后启动
./mvnw spring-boot:run
# 或者使用 IDEA 运行 ProjMasterApplication
```

后端服务默认运行在 http://localhost:8080

### 4. 启动前端服务

```bash
cd frontend
npm install
npm run dev
```

前端服务默认运行在 http://localhost:5173

### 5. 访问系统

打开浏览器访问 http://localhost:5173

默认测试账号：
| 角色 | 用户名 | 密码 |
|------|--------|------|
| 超级管理员 | admin | 123456 |
| 项目经理 | pm1 | 123456 |
| 产品经理 | po1 | 123456 |
| 开发 | dev1 | 123456 |
| 测试 | test1 | 123456 |

## 项目结构

```
projmaster/
├── backend/                 # 后端项目
│   ├── src/main/java/com/projmaster/
│   │   ├── controller/      # 控制器层
│   │   ├── service/         # 业务逻辑层
│   │   ├── entity/          # 实体类
│   │   ├── repository/      # 数据访问层
│   │   ├── dto/             # 数据传输对象
│   │   ├── config/          # 配置类
│   │   ├── security/        # 安全配置
│   │   └── exception/       # 异常处理
│   └── src/main/resources/
│       ├── db/migration/    # 数据库迁移脚本
│       └── application.yml  # 应用配置
│
├── frontend/                # 前端项目
│   ├── src/
│   │   ├── api/             # API接口
│   │   ├── views/           # 页面视图
│   │   ├── components/      # 组件
│   │   ├── stores/          # Pinia状态管理
│   │   ├── router/          # 路由配置
│   │   └── utils/           # 工具函数
│   └── package.json
│
└── docs/                    # 文档
```

## API 文档

启动后端服务后，可以访问以下地址查看API：

- REST API: http://localhost:8080/api/**
- 健康检查: http://localhost:8080/actuator/health

主要API列表：
- `POST /api/auth/login` - 登录
- `POST /api/auth/register` - 注册
- `GET /api/dashboard/stats` - 仪表盘统计
- `GET /api/projects` - 获取项目列表
- `GET /api/requirements/project/{id}` - 获取项目需求
- `GET /api/tasks/project/{id}` - 获取项目任务
- `GET /api/bugs/project/{id}` - 获取项目Bug
- `GET /api/iterations/project/{id}` - 获取项目迭代

## 开发计划

- [ ] 甘特图视图
- [ ] 燃尽图/燃起图
- [ ] 工作流配置
- [ ] 邮件通知
- [ ] 文件上传/附件管理
- [ ] 评论系统
- [ ] 工时日志
- [ ] 导出报表

## 许可证

MIT License
