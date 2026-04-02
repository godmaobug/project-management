# ProjMaster 产品需求说明书

## 1. 产品概述

ProjMaster 是一款面向企业研发/项目团队的项目管理工具，核心解决三大问题：**需求如何记录**、**任务如何执行**、**缺陷如何追踪**。

## 2. 用户角色

| 角色 | 权限 | 职责 |
|------|------|------|
| 超级管理员 | 系统最高权限 | 人员配置、工作流设置 |
| 产品负责人 | 产品管理权限 | 需求池管理、规划迭代、验收成果 |
| 项目经理 | 项目管理权限 | 团队进度把控、任务分配、数据度量 |
| 成员 | 操作权限 | 处理具体的任务与缺陷 |

## 3. 功能模块

### 3.1 仪表盘
- 待办事项卡片（任务、需求、Bug）
- 项目概览及进度
- 快捷创建入口

### 3.2 项目管理
- 项目 CRUD（Scrum/看板/通用）
- 成员与权限管理
- 项目设置（公开/私有）

### 3.3 需求管理
- 需求池（优先级、状态）
- 需求详情（富文本、验收标准）
- 需求追踪（矩阵视图）

### 3.4 任务管理
- 迭代/Sprint 规划
- 任务分解（子任务）
- 看板视图（拖拽交互）

### 3.5 缺陷管理
- Bug生命周期（新建→确认→修复→关闭）
- 严重程度分级
- 关联需求和版本

### 3.6 统计报表
- 任务状态分布
- Bug统计
- 项目进度

## 4. 技术架构

### 4.1 后端
- Spring Boot 3.2
- Spring Security + JWT
- JPA + MySQL
- RESTful API

### 4.2 前端
- Vue 3 + Vite
- Element Plus
- Pinia + Vue Router
- ECharts

## 5. 数据库设计

### 5.1 核心实体
- users（用户）
- projects（项目）
- project_members（项目成员）
- requirements（需求）
- iterations（迭代）
- tasks（任务）
- bugs（缺陷）
- work_logs（工时日志）
- comments（评论）
- notifications（通知）
- attachments（附件）

## 6. 接口设计

### 6.1 认证接口
- POST /api/auth/login - 登录
- POST /api/auth/register - 注册
- GET /api/auth/me - 获取当前用户

### 6.2 项目接口
- GET /api/projects - 获取项目列表
- POST /api/projects - 创建项目
- GET /api/projects/{id} - 获取项目详情
- PUT /api/projects/{id} - 更新项目
- DELETE /api/projects/{id} - 删除项目

### 6.3 需求接口
- GET /api/requirements/project/{id} - 获取项目需求
- POST /api/requirements - 创建需求
- PUT /api/requirements/{id} - 更新需求

### 6.4 任务接口
- GET /api/tasks/project/{id} - 获取项目任务
- POST /api/tasks - 创建任务
- PATCH /api/tasks/{id}/status - 更新任务状态

### 6.5 Bug接口
- GET /api/bugs/project/{id} - 获取项目Bug
- POST /api/bugs - 创建Bug
- PATCH /api/bugs/{id}/status - 更新Bug状态

### 6.6 迭代接口
- GET /api/iterations/project/{id} - 获取项目迭代
- POST /api/iterations - 创建迭代

## 7. 部署说明

### 7.1 环境要求
- JDK 17+
- MySQL 8.0+
- Node.js 18+

### 7.2 部署步骤
1. 创建数据库
2. 配置后端数据库连接
3. 启动后端服务
4. 安装前端依赖并启动

## 8. 开发计划

### V1.0
- [x] 基础架构搭建
- [x] 用户认证
- [x] 项目管理
- [x] 需求管理
- [x] 任务管理
- [x] 缺陷管理
- [x] 迭代管理
- [x] 看板视图
- [x] 统计报表

### V1.1
- [ ] 甘特图
- [ ] 燃尽图
- [ ] 工时日志
- [ ] 评论系统

### V1.2
- [ ] 文件上传
- [ ] 邮件通知
- [ ] 导出报表
- [ ] 工作流配置
