# ResumeMatch 简历智能匹配系统

> 招聘场景 Web 业务系统｜SpringBoot 后端｜MySQL 数据库｜AI 人岗匹配

## 项目简介

ResumeMatch 是面向招聘业务场景的 Web 系统，包含求职者端与管理员端，覆盖简历管理、岗位管理、AI 简历优化、人岗匹配、投递管理和后台运营等功能。

本人项目角色为**后端开发**，重点负责数据库设计、业务接口开发、AI 能力集成以及接口与业务效果验证。

## 我的主要工作

### 1. 数据库设计
独立完成 **11 张业务数据表**结构设计，完成实体关系建模，并通过索引优化查询、事务保障数据一致性。

主要表包括：`user`、`resume`、`resume_version`、`resume_skill`、`skill_tag`、`job`、`application`、`favorite`、`match_result`、`notification`、`operation_log`。

### 2. 后端接口开发
基于 SpringBoot 开发 **20+ REST 风格业务接口**，覆盖简历投递、岗位收藏、状态流转、Excel 批量导出、用户管理、岗位管理、简历管理、投递处理和操作日志等业务。

### 3. AI 能力集成
封装 DeepSeek 大模型 API，完成大模型能力与招聘业务结合；设计加权人岗匹配逻辑，输出匹配结果及优化建议。

### 4. 接口与业务验证
使用 Postman 进行接口调试与验证，参与完整招聘业务流程联调，对 AI 功能进行业务效果验证，并整理项目文档。

## 技术栈

**后端：** SpringBoot 3.2.5、SpringMVC、MyBatis-Plus 3.5、Java、RESTful API  
**数据库：** MySQL 8、ER/EER 建模、索引、事务、逻辑删除  
**安全与基础设施：** JWT、全局拦截器、阿里云 OSS  
**AI：** DeepSeek API、Prompt、加权人岗匹配  
**文档与开发：** POI、PDFBox、Git、IDEA、Postman  
**前端：** Vue 3、Element-Plus、Axios、ECharts

## 核心功能

### 求职者端
1. 注册登录与 JWT 身份认证
2. 个人信息维护
3. PDF / Word 简历上传解析
4. 简历多版本快照管理
5. AI 简历优化
6. AI 人岗匹配
7. 岗位搜索与筛选
8. 岗位收藏
9. 简历投递
10. 投递状态跟踪
11. 消息通知

### 管理员端
1. 数据可视化大屏
2. 用户管理
3. 岗位管理
4. 简历检索
5. 投递记录管理
6. Excel 批量导出
7. 投递状态处理
8. 操作日志审计

## 数据库设计

数据库：`resume_match_db`

| 表 | 说明 |
|---|---|
| `user` | 用户与角色权限 |
| `resume` | 简历主表 |
| `resume_version` | 简历历史版本 |
| `resume_skill` | 简历与技能标签关联 |
| `skill_tag` | 技能标签 |
| `job` | 岗位信息 |
| `application` | 投递记录 |
| `favorite` | 岗位收藏 |
| `match_result` | AI 人岗匹配结果 |
| `notification` | 消息通知 |
| `operation_log` | 操作审计日志 |

统一使用 `create_time / update_time` 时间字段，并采用 `deleted` 字段实现逻辑删除。

## 项目结构

```text
resume-match-system
├── frontend
├── backend
│   ├── controller
│   ├── service
│   ├── mapper
│   ├── entity
│   ├── config
│   ├── common
│   └── ResumeJobMatchSystemApplication.java
├── docs
│   ├── images
│   └── sql
└── README.md
```

## 项目成果

- 完成招聘业务后端服务搭建
- 完成 11 张业务数据表设计
- 完成 20+ REST API 开发
- 完成 DeepSeek AI 能力集成
- 实现简历、岗位、投递、匹配等完整业务闭环
- 业务数据出错率控制在 **0.5% 以内**

## 后续计划

- 接入 RAG 检索增强生成
- 引入行业岗位知识库
- 进一步提升 AI 匹配与简历优化效果
- 增加视频简历解析
- 拓展微信小程序端
