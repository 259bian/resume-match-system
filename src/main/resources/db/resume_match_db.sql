/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : localhost:3306
 Source Schema         : resume_match_db

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 01/06/2026 15:03:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for application
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `job_id` bigint NULL DEFAULT NULL,
  `resume_id` bigint NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT 1 COMMENT '1=待查看 2=已查看 3=面试通知 4=已拒绝 5=已录取',
  `match_score` int NULL DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_application_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_application_job_id`(`job_id` ASC) USING BTREE,
  INDEX `idx_application_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '投递申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of application
-- ----------------------------
INSERT INTO `application` VALUES (1, 2, 1, 1, 3, 92, '面试安排：2025-06-20 14:00，线上面试', '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `application` VALUES (2, 2, 10, 1, 3, 85, '简历已查看，等待用人部门评估', '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `application` VALUES (3, 3, 2, 2, 3, 78, NULL, '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `application` VALUES (4, 3, 9, 2, 4, 65, '岗位要求与当前技能匹配度不足', '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `application` VALUES (5, 4, 3, 3, 2, 88, '简历已查看，技术评估中', '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `application` VALUES (6, 4, 6, 3, 5, 95, '恭喜被录取！请于2025-07-01报到', '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `application` VALUES (7, 5, 4, 4, 1, 70, NULL, '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `application` VALUES (8, 5, 7, 4, 2, 72, '简历已查看，进入初筛阶段', '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `application` VALUES (9, 2, 2, 5, 4, 30, NULL, '2026-05-30 22:06:47', '2026-05-30 22:06:47', 0);
INSERT INTO `application` VALUES (10, 2, 5, 5, 1, 25, NULL, '2026-05-31 00:09:05', '2026-05-31 09:25:44', 1);
INSERT INTO `application` VALUES (11, 2, 3, 1, 1, 0, NULL, '2026-05-31 10:34:03', '2026-05-31 10:34:56', 1);
INSERT INTO `application` VALUES (12, 2, 1, 1, 1, 0, NULL, '2026-05-31 10:34:20', '2026-05-31 10:34:53', 1);
INSERT INTO `application` VALUES (13, 2, 1, 1, 1, 20, NULL, '2026-05-31 10:34:28', '2026-05-31 10:34:28', 0);
INSERT INTO `application` VALUES (14, 2, 4, 5, 2, 25, NULL, '2026-05-31 10:34:39', '2026-05-31 10:34:39', 0);
INSERT INTO `application` VALUES (15, 2, 1, 1, 2, 70, NULL, '2026-05-31 10:59:06', '2026-05-31 10:59:06', 0);
INSERT INTO `application` VALUES (16, 2, 2, 6, 5, 100, NULL, '2026-05-31 11:07:07', '2026-05-31 11:07:07', 0);
INSERT INTO `application` VALUES (17, 2, 1, 1, 2, 60, NULL, '2026-05-31 12:50:40', '2026-05-31 12:50:40', 0);
INSERT INTO `application` VALUES (18, 2, 1, 1, 1, 68, NULL, '2026-06-01 14:55:13', '2026-06-01 14:55:13', 0);
INSERT INTO `application` VALUES (19, 2, 1, 6, 1, 25, NULL, '2026-06-01 14:55:23', '2026-06-01 14:55:23', 0);

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `job_id` bigint NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_job`(`user_id` ASC, `job_id` ASC) USING BTREE,
  INDEX `idx_favorite_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of favorite
-- ----------------------------
INSERT INTO `favorite` VALUES (1, 2, 3, '2026-05-27 18:32:36', 1);
INSERT INTO `favorite` VALUES (2, 2, 5, '2026-05-27 18:32:36', 0);
INSERT INTO `favorite` VALUES (3, 3, 1, '2026-05-27 18:32:36', 0);
INSERT INTO `favorite` VALUES (4, 3, 9, '2026-05-27 18:32:36', 0);
INSERT INTO `favorite` VALUES (5, 4, 2, '2026-05-27 18:32:36', 0);
INSERT INTO `favorite` VALUES (6, 4, 8, '2026-05-27 18:32:36', 0);
INSERT INTO `favorite` VALUES (7, 5, 6, '2026-05-27 18:32:36', 0);
INSERT INTO `favorite` VALUES (8, 5, 10, '2026-05-27 18:32:36', 0);
INSERT INTO `favorite` VALUES (9, 1, 1, '2026-05-27 22:35:19', 1);
INSERT INTO `favorite` VALUES (10, 6, 1, '2026-05-29 15:29:56', 0);
INSERT INTO `favorite` VALUES (13, 2, 1, '2026-05-31 00:09:48', 1);
INSERT INTO `favorite` VALUES (14, 2, 4, '2026-05-31 09:45:24', 1);
INSERT INTO `favorite` VALUES (19, 2, 2, '2026-05-31 12:42:13', 0);
INSERT INTO `favorite` VALUES (20, 2, 11, '2026-05-31 13:18:23', 0);
INSERT INTO `favorite` VALUES (23, 2, 7, '2026-06-01 14:34:23', 0);

-- ----------------------------
-- Table structure for job
-- ----------------------------
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `company` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `salary` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `experience` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `education` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `requirements` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `skills` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT 1 COMMENT '1=在招 0=停招',
  `view_count` int NULL DEFAULT 0,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_job_category`(`category` ASC) USING BTREE,
  INDEX `idx_job_location`(`location` ASC) USING BTREE,
  INDEX `idx_job_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '职位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job
-- ----------------------------
INSERT INTO `job` VALUES (1, 'Java开发工程师', '阿里巴巴', '杭州', '25k-45k', '3-5年', '本科及以上', '负责电商核心系统设计与开发，参与高并发、高可用架构设计与优化，承担日均亿级流量的系统建设与维护。', '1. 精通Java，熟悉JVM调优和多线程编程\n2. 熟练掌握Spring Boot/Spring Cloud微服务架构\n3. 熟悉MySQL、Redis、Elasticsearch等中间件\n4. 有大规模分布式系统开发经验\n5. 良好的系统设计和问题解决能力', 'Java,Spring Boot,Spring Cloud,MySQL,Redis,Elasticsearch,分布式系统', '后端开发', 1, 1266, '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `job` VALUES (2, 'Python开发工程师', '腾讯', '深圳', '20k-40k', '2-5年', '本科及以上', '负责公司AI平台后端服务开发，参与机器学习模型部署与优化，构建高性能数据处理流水线。', '1. 精通Python，熟悉Django或Flask框架\n2. 熟悉MySQL、MongoDB等数据库\n3. 了解机器学习框架（TensorFlow/PyTorch）\n4. 有RESTful API设计经验\n5. 熟悉Linux开发环境', 'Python,Django,Flask,MySQL,MongoDB,Machine Learning', '后端开发', 1, 986, '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `job` VALUES (3, '前端开发工程师', '字节跳动', '北京', '30k-55k', '3-5年', '本科及以上', '参与公司核心产品前端架构设计与开发，负责组件库建设与性能优化，打造极致的用户体验。', '1. 精通HTML5、CSS3、JavaScript/TypeScript\n2. 熟练掌握React或Vue.js框架\n3. 熟悉前端工程化（Webpack/Vite）\n4. 有移动端H5开发经验\n5. 了解Node.js服务端开发', 'JavaScript,TypeScript,React,Vue.js,HTML,CSS,Node.js', '前端开发', 1, 1563, '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `job` VALUES (4, '数据分析师', '美团', '北京', '18k-35k', '1-3年', '本科及以上', '负责业务数据分析与数据产品建设，通过数据驱动业务决策，搭建数据指标体系和可视化看板。', '1. 熟练使用SQL，熟悉Hive/Spark等大数据处理工具\n2. 掌握Python或R进行数据分析\n3. 熟悉Tableau/FineBI等可视化工具\n4. 有良好的数据敏感度和业务理解能力\n5. 统计学或数学相关专业优先', 'SQL,Python,Hive,Spark,Tableau,数据分析', '数据', 1, 727, '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `job` VALUES (5, '产品经理', '华为', '深圳', '20k-40k', '3-5年', '本科及以上', '负责云计算产品的规划与设计，深入理解客户需求，推动产品从概念到上线的全过程。', '1. 3年以上B端产品经验\n2. 熟悉云计算/IaaS/PaaS领域\n3. 有较强的需求分析和文档撰写能力\n4. 良好的沟通协调和项目管理能力\n5. 计算机相关专业背景优先', '产品设计,需求分析,项目管理,数据分析,云计算,原型设计', '产品', 1, 562, '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `job` VALUES (6, 'UI设计师', '百度', '北京', '18k-32k', '2-5年', '本科及以上', '负责公司AI产品的界面设计与用户体验优化，参与设计规范制定与组件库建设。', '1. 精通Figma/Sketch等设计工具\n2. 有B端产品设计经验\n3. 了解前端开发基础知识\n4. 有设计系统搭建经验\n5. 优秀的审美和视觉表达能力', 'Figma,Sketch,UI设计,用户体验,设计系统,视觉设计', '设计', 1, 430, '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `job` VALUES (7, '测试工程师', '京东', '北京', '18k-30k', '2-5年', '本科及以上', '负责电商平台核心业务的质量保证工作，制定测试策略，开发自动化测试框架。', '1. 熟悉软件测试流程和方法论\n2. 有自动化测试框架开发经验（Selenium/Appium）\n3. 熟悉至少一门编程语言（Java/Python）\n4. 有性能测试和安全测试经验\n5. 良好的逻辑分析和问题定位能力', '自动化测试,Selenium,Java,Python,性能测试,CI/CD', '测试', 1, 381, '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `job` VALUES (8, '运维工程师', '网易', '杭州', '18k-35k', '2-5年', '本科及以上', '负责公司游戏业务线的运维保障工作，参与自动化运维平台建设与容器化改造。', '1. 熟悉Linux操作系统，掌握Shell脚本编程\n2. 精通Docker和Kubernetes容器编排\n3. 熟悉Nginx、Redis、MySQL等中间件运维\n4. 有Prometheus/Grafana监控体系搭建经验\n5. 有大规模集群运维经验优先', 'Linux,Docker,Kubernetes,Nginx,Redis,MySQL,Shell,Prometheus', '运维', 1, 290, '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `job` VALUES (9, 'Node.js后端开发工程师', '腾讯', '深圳', '22k-42k', '2-4年', '本科及以上', '负责内部DevOps平台的后端服务开发，参与微服务架构演进与技术方案设计。', '1. 精通Node.js，熟悉Express/Koa等框架\n2. 熟悉TypeScript\n3. 熟悉MongoDB、Redis等NoSQL数据库\n4. 有微服务架构设计和开发经验\n5. 了解前端开发（React/Vue）优先', 'Node.js,TypeScript,MongoDB,Redis,Express,微服务', '后端开发', 1, 640, '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `job` VALUES (10, 'Java高级开发工程师', '华为', '西安', '22k-40k', '5-10年', '本科及以上', '负责通信领域核心系统架构设计与技术攻关，参与技术规范制定和团队技术培训。', '1. 5年以上Java开发经验\n2. 精通Spring全家桶和微服务架构\n3. 有高并发、高可用系统架构经验\n4. 熟悉网络通信协议和分布式理论\n5. 有技术团队管理经验者优先', 'Java,Spring Boot,Spring Cloud,MySQL,Redis,Kubernetes,Docker,分布式系统', '后端开发', 1, 980, '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `job` VALUES (11, '前端开发工程师', '百度', '上海', '13k-16k', '不限', '本科及以上', '参与公司核心产品前端架构设计与开发，负责组件库建设与性能优化，打造极致的用户体验。', '1. 精通HTML5、CSS3、JavaScript/TypeScript\n2. 熟练掌握React或Vue.js框架\n3. 熟悉前端工程化（Webpack/Vite）\n4. 有移动端H5开发经验\n5. 了解Node.js服务端开发', 'JavaScript,TypeScript,React,Vue.js,HTML,CSS,Node.js', '前端开发', 1, 3, '2026-05-31 12:06:11', '2026-05-31 13:16:20', 0);

-- ----------------------------
-- Table structure for match_result
-- ----------------------------
DROP TABLE IF EXISTS `match_result`;
CREATE TABLE `match_result`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `resume_id` bigint NULL DEFAULT NULL,
  `job_id` bigint NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  `score` int NULL DEFAULT NULL,
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'JSON',
  `suggestion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_match_result_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_match_result_resume_id`(`resume_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 153 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '匹配结果表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of match_result
-- ----------------------------
INSERT INTO `match_result` VALUES (92, 5, 5, 2, 25, '{\"score\":25,\"suggestion\":\"建议候选人补充B端产品相关项目经验，学习云计算基础知识（如IaaS、PaaS概念），并加强需求分析和文档撰写的实践。同时，可考虑获取计算机相关专业认证或学历以提升背景匹配度。\",\"analysis\":\"候选人拥有5年Java开发和2年团队管理经验，具备一定的项目管理能力，但岗位核心要求是B端产品经验和云计算领域知识，这些在简历中完全未体现。此外，需求分析和文档撰写能力也未被提及，计算机专业背景未知。整体匹配度较低。\",\"skillMatch\":{\"missing\":[\"B端产品经验\",\"云计算/IaaS/PaaS领域知识\",\"需求分析能力\",\"文档撰写能力\",\"计算机专业背景\"],\"matched\":[\"团队管理\",\"项目管理\"]}}', '建议候选人补充B端产品相关项目经验，学习云计算基础知识（如IaaS、PaaS概念），并加强需求分析和文档撰写的实践。同时，可考虑获取计算机相关专业认证或学历以提升背景匹配度。', '2026-05-31 00:09:08');
INSERT INTO `match_result` VALUES (127, 1, 1, 2, 60, '{\"score\":60,\"suggestion\":\"建议深入学习Spring Cloud组件（如Nacos、Gateway、Sentinel）并实践微服务项目；学习Elasticsearch的安装、索引、搜索和聚合功能；参与或设计分布式系统项目（如高并发、分库分表、服务治理），以积累大规模系统经验。\",\"analysis\":\"候选人具备2年Java后端开发经验，熟练掌握Spring Boot、MySQL、Redis等核心技能，有独立开发业务模块和性能优化的实际经验。但岗位要求中的Spring Cloud微服务架构、Elasticsearch中间件以及大规模分布式系统开发经验，候选人仅了解Spring Cloud基础概念，未涉及Elasticsearch，且缺乏分布式系统实战经验。整体匹配度中等。\",\"skillMatch\":{\"missing\":[\"Spring Cloud\",\"Elasticsearch\",\"大规模分布式系统开发经验\"],\"matched\":[\"Java\",\"Spring Boot\",\"MySQL\",\"Redis\",\"JVM\"]}}', '建议深入学习Spring Cloud组件（如Nacos、Gateway、Sentinel）并实践微服务项目；学习Elasticsearch的安装、索引、搜索和聚合功能；参与或设计分布式系统项目（如高并发、分库分表、服务治理），以积累大规模系统经验。', '2026-05-31 10:58:55');
INSERT INTO `match_result` VALUES (132, 1, 6, 2, 25, '{\"score\":25,\"suggestion\":\"建议候选人考虑后端开发相关岗位，或通过系统学习设计工具（Figma/Sketch）、参与B端设计项目、学习设计系统搭建来转行。\",\"analysis\":\"该简历为Java后端开发工程师，岗位要求为UI/UX设计师。简历中没有任何与设计工具、B端产品设计、设计系统、审美视觉相关的经验或技能。仅有的‘了解前端开发基础知识’在岗位要求中提及，但简历中未体现前端开发知识。整体匹配度极低。\",\"skillMatch\":{\"missing\":[\"Figma/Sketch\",\"B端产品设计经验\",\"前端开发基础知识\",\"设计系统搭建经验\",\"优秀的审美和视觉表达能力\"],\"matched\":[]}}', '建议候选人考虑后端开发相关岗位，或通过系统学习设计工具（Figma/Sketch）、参与B端设计项目、学习设计系统搭建来转行。', '2026-05-31 10:59:05');
INSERT INTO `match_result` VALUES (133, 1, 7, 2, 35, '{\"score\":35,\"suggestion\":\"建议候选人补充软件测试相关经验，例如：1. 学习软件测试基础理论（黑盒/白盒测试、测试用例设计方法）；2. 掌握自动化测试框架（如Selenium、Appium）并应用于项目；3. 学习性能测试工具（如JMeter）和安全测试基础；4. 如果可能，在简历中突出任何与测试相关的实践，如参与接口测试、编写单元测试或使用Postman进行API测试。\",\"analysis\":\"候选人张三是一名Java后端开发工程师，拥有2年工作经验，技术栈集中在Java后端开发、数据库优化和项目全流程管理。岗位要求侧重于软件测试，包括测试流程、自动化测试、性能测试和安全测试。虽然候选人具备Java编程能力，并且在项目经验中展现了问题定位和系统维护能力（如日志排查、线上问题解决），但简历中完全没有提及任何与软件测试相关的经验，如测试用例设计、测试框架使用、性能测试工具（如JMeter）或安全测试知识。因此，匹配度较低。\",\"skillMatch\":{\"missing\":[\"软件测试流程和方法论\",\"自动化测试框架开发经验（Selenium/Appium）\",\"性能测试经验\",\"安全测试经验\",\"Python\"],\"matched\":[\"Java\",\"问题定位能力\"]}}', '建议候选人补充软件测试相关经验，例如：1. 学习软件测试基础理论（黑盒/白盒测试、测试用例设计方法）；2. 掌握自动化测试框架（如Selenium、Appium）并应用于项目；3. 学习性能测试工具（如JMeter）和安全测试基础；4. 如果可能，在简历中突出任何与测试相关的实践，如参与接口测试、编写单元测试或使用Postman进行API测试。', '2026-05-31 10:59:07');
INSERT INTO `match_result` VALUES (134, 1, 1, 2, 70, '{\"score\":70,\"suggestion\":\"建议深入学习Spring Cloud微服务架构，掌握服务注册与发现、配置中心、网关等组件；补充JVM调优实践，如内存分析、GC调优；学习Elasticsearch的安装、索引设计、搜索优化；尝试参与或构建分布式系统项目，提升系统设计能力。\",\"analysis\":\"候选人有2年Java后端开发经验，熟练使用SpringBoot、MyBatis、MySQL、Redis等核心技术，具备独立开发模块和项目全流程经验，但简历中明确提到‘了解Spring Cloud组件’而非熟练掌握，缺乏微服务架构实战经验；JVM调优仅作为基础技能提及，未体现实际调优经验；未涉及Elasticsearch中间件；项目经验中未体现大规模分布式系统开发经验，项目规模较小。综合匹配度中等。\",\"skillMatch\":{\"missing\":[\"Spring Cloud\",\"JVM调优\",\"Elasticsearch\",\"分布式系统经验\"],\"matched\":[\"Java\",\"Spring Boot\",\"MySQL\",\"Redis\",\"MyBatis\",\"MyBatis-Plus\"]}}', '建议深入学习Spring Cloud微服务架构，掌握服务注册与发现、配置中心、网关等组件；补充JVM调优实践，如内存分析、GC调优；学习Elasticsearch的安装、索引设计、搜索优化；尝试参与或构建分布式系统项目，提升系统设计能力。', '2026-05-31 10:59:08');
INSERT INTO `match_result` VALUES (135, 1, 8, 2, 35, '{\"score\":35,\"suggestion\":\"建议补充Shell脚本编程学习，掌握自动化运维脚本编写；系统学习Docker和Kubernetes，从容器化部署到编排管理逐步实践；了解Prometheus/Grafana监控体系搭建流程；积累Nginx、Redis、MySQL的运维调优经验，可参与线上集群运维项目或通过实验环境提升。\",\"analysis\":\"简历中提及了Linux常用命令和基本运维操作，但未涉及Shell脚本编程；对Docker和Kubernetes无任何经验；虽然了解Nginx基础使用，但缺乏深入运维经验；Redis和MySQL的使用偏向开发层面，未体现运维能力；完全没有Prometheus/Grafana监控体系搭建经验；缺乏大规模集群运维背景。整体匹配度较低，仅基础Linux、Redis和MySQL知识部分符合要求。\",\"skillMatch\":{\"missing\":[\"Shell脚本编程\",\"Docker\",\"Kubernetes\",\"Prometheus/Grafana\",\"大规模集群运维\"],\"matched\":[\"Linux\",\"Redis\",\"MySQL\",\"Nginx\"]}}', '建议补充Shell脚本编程学习，掌握自动化运维脚本编写；系统学习Docker和Kubernetes，从容器化部署到编排管理逐步实践；了解Prometheus/Grafana监控体系搭建流程；积累Nginx、Redis、MySQL的运维调优经验，可参与线上集群运维项目或通过实验环境提升。', '2026-05-31 10:59:10');
INSERT INTO `match_result` VALUES (140, 6, 2, 2, 100, '{\"score\":100,\"suggestion\":\"保持现有技能水平，建议在面试中准备具体项目案例以展示各技能的实践应用能力。\",\"analysis\":\"简历中的5项技能要求与岗位要求完全一致，没有缺失项。候选人精通Python并熟悉Django/Flask框架，熟悉MySQL和MongoDB数据库，了解TensorFlow/PyTorch等机器学习框架，具有RESTful API设计经验，以及熟悉Linux开发环境。所有关键技能均匹配。\",\"skillMatch\":{\"missing\":[],\"matched\":[\"Python\",\"Django或Flask框架\",\"MySQL\",\"MongoDB\",\"TensorFlow/PyTorch\",\"RESTful API设计\",\"Linux开发环境\"]}}', '保持现有技能水平，建议在面试中准备具体项目案例以展示各技能的实践应用能力。', '2026-05-31 12:42:41');
INSERT INTO `match_result` VALUES (142, 6, 4, 2, 55, '{\"score\":55,\"suggestion\":\"建议补充Hive/Spark等大数据处理工具的学习经验，掌握Tableau或FineBI等可视化工具，并通过项目案例展示数据分析和业务理解能力。如有条件，可补充统计学或数学相关课程或证书。\",\"analysis\":\"简历中具备Python技能（匹配岗位要求2），但岗位核心需求为大数据处理（Hive/Spark）、数据可视化（Tableau/FineBI）以及业务分析能力，而简历内容更偏向后端开发和机器学习，缺乏对大数据工具和可视化工具的掌握，且未体现数据敏感度或相关专业背景。整体匹配度一般。\",\"skillMatch\":{\"missing\":[\"Hive/Spark\",\"Tableau/FineBI\",\"数据敏感度/业务理解\",\"统计学/数学专业背景\"],\"matched\":[\"Python数据分析\"]}}', '建议补充Hive/Spark等大数据处理工具的学习经验，掌握Tableau或FineBI等可视化工具，并通过项目案例展示数据分析和业务理解能力。如有条件，可补充统计学或数学相关课程或证书。', '2026-05-31 12:42:46');
INSERT INTO `match_result` VALUES (151, 1, 1, 2, 68, '{\"score\":68,\"suggestion\":\"1. 深入学习JVM调优（如GC日志分析、参数调优）和多线程并发编程（如线程池、锁优化），积累实战案例。2. 补充Elasticsearch的学习，掌握索引、搜索、聚合等核心功能。3. 参与或研究开源分布式系统项目，积累分布式架构设计、高可用、高并发等经验。\",\"analysis\":\"候选人具备2年Java后端开发经验，熟练掌握Spring Boot/MyBatis等框架，精通MySQL和Redis，了解Spring Cloud微服务基础，与岗位要求的部分技术栈匹配。但缺少Elasticsearch经验，JVM调优和多线程编程仅为了解层面，缺乏大规模分布式系统实际开发经验，整体经验深度和广度与高级岗位要求存在差距。\",\"skillMatch\":{\"missing\":[\"JVM调优\",\"Elasticsearch\",\"大规模分布式系统开发经验\"],\"matched\":[\"Java\",\"Spring Boot\",\"MySQL\",\"Redis\",\"Spring Cloud\"]}}', '1. 深入学习JVM调优（如GC日志分析、参数调优）和多线程并发编程（如线程池、锁优化），积累实战案例。2. 补充Elasticsearch的学习，掌握索引、搜索、聚合等核心功能。3. 参与或研究开源分布式系统项目，积累分布式架构设计、高可用、高并发等经验。', '2026-06-01 14:55:16');
INSERT INTO `match_result` VALUES (152, 6, 1, 2, 25, '{\"score\":25,\"suggestion\":\"建议候选人补充Java语言学习，重点掌握JVM调优、多线程编程及Spring Boot/Spring Cloud框架；同时学习Redis、Elasticsearch等中间件，并积累分布式系统设计经验。可考虑通过个人项目或开源贡献来弥补差距。\",\"analysis\":\"简历中核心技术栈为Python，岗位要求以Java为主，技术方向严重不匹配。虽然候选人在数据库（MySQL）、系统设计（RESTful API、安全、性能优化）方面有一定基础，但缺失Java生态（包括JVM、多线程、Spring框架）以及Redis、Elasticsearch等关键中间件，且未体现分布式系统开发经验。整体匹配度较低。\",\"skillMatch\":{\"missing\":[\"Java\",\"JVM调优\",\"多线程编程\",\"Spring Boot\",\"Spring Cloud\",\"Redis\",\"Elasticsearch\",\"分布式系统\"],\"matched\":[\"MySQL\",\"系统设计\",\"问题解决能力\"]}}', '建议候选人补充Java语言学习，重点掌握JVM调优、多线程编程及Spring Boot/Spring Cloud框架；同时学习Redis、Elasticsearch等中间件，并积累分布式系统设计经验。可考虑通过个人项目或开源贡献来弥补差距。', '2026-06-01 14:55:26');

-- ----------------------------
-- Table structure for notification
-- ----------------------------
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` tinyint NULL DEFAULT NULL COMMENT '1=投递成功 2=面试通知 3=已拒绝 4=系统消息',
  `is_read` tinyint NULL DEFAULT 0,
  `related_id` bigint NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_notification_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_notification_is_read`(`is_read` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notification
-- ----------------------------
INSERT INTO `notification` VALUES (1, 2, '投递成功', '您已成功投递【阿里巴巴-Java开发工程师】职位，请耐心等待企业反馈。', 1, 1, 1, '2026-05-27 18:32:36', 0);
INSERT INTO `notification` VALUES (2, 2, '面试通知', '恭喜！您投递的【阿里巴巴-Java开发工程师】已通过初筛，面试时间：2025-06-20 14:00。', 2, 1, 1, '2026-05-27 18:32:36', 0);
INSERT INTO `notification` VALUES (3, 3, '投递成功', '您已成功投递【腾讯-Python开发工程师】职位，请耐心等待企业反馈。', 1, 1, 2, '2026-05-27 18:32:36', 0);
INSERT INTO `notification` VALUES (4, 3, '投递结果通知', '很遗憾，您投递的【腾讯-Node.js后端开发工程师】岗位与当前岗位要求匹配度不足，感谢您的关注。', 3, 0, 4, '2026-05-27 18:32:36', 0);
INSERT INTO `notification` VALUES (5, 4, '系统消息', '恭喜您被【百度-UI设计师】岗位录取！请于2025-07-01携带相关材料到公司报到。', 4, 0, 6, '2026-05-27 18:32:36', 0);
INSERT INTO `notification` VALUES (6, 2, '投递成功', '您已成功投递岗位【Python开发工程师】，匹配度得分: 30分', 1, 1, 9, '2026-05-30 22:06:50', 0);
INSERT INTO `notification` VALUES (7, 2, '恭喜您获得面试通知', '您投递的岗位【Python开发工程师】状态已更新为：恭喜您获得面试通知', 2, 1, 9, '2026-05-30 23:28:38', 0);
INSERT INTO `notification` VALUES (8, 2, '很遗憾，您未被录取', '您投递的岗位【Python开发工程师】状态已更新为：很遗憾，您未被录取', 3, 1, 9, '2026-05-30 23:28:44', 0);
INSERT INTO `notification` VALUES (9, 2, '恭喜您被录取', '您投递的岗位【Python开发工程师】状态已更新为：恭喜您被录取', 2, 1, 9, '2026-05-30 23:29:27', 0);
INSERT INTO `notification` VALUES (10, 2, '投递成功', '您已成功投递岗位【产品经理】，匹配度得分: 25分', 1, 1, 10, '2026-05-31 00:09:08', 0);
INSERT INTO `notification` VALUES (11, 2, '投递成功', '您已成功投递岗位【前端开发工程师】，匹配度得分: 0分', 1, 0, 11, '2026-05-31 10:34:04', 1);
INSERT INTO `notification` VALUES (12, 2, '投递成功', '您已成功投递岗位【Java开发工程师】，匹配度得分: 0分', 1, 0, 12, '2026-05-31 10:34:21', 1);
INSERT INTO `notification` VALUES (13, 2, '投递成功', '您已成功投递岗位【Java开发工程师】，匹配度得分: 20分', 1, 1, 13, '2026-05-31 10:34:29', 0);
INSERT INTO `notification` VALUES (14, 2, '投递成功', '您已成功投递岗位【数据分析师】，匹配度得分: 25分', 1, 1, 14, '2026-05-31 10:34:41', 0);
INSERT INTO `notification` VALUES (15, 2, '投递成功', '您已成功投递岗位【Java开发工程师】，匹配度得分: 70分', 1, 0, 15, '2026-05-31 10:59:13', 0);
INSERT INTO `notification` VALUES (16, 2, '投递成功', '您已成功投递岗位【Python开发工程师】，匹配度得分: 100分', 1, 0, 16, '2026-05-31 11:07:08', 0);
INSERT INTO `notification` VALUES (17, 2, '简历已被查看', '您投递的岗位【Python开发工程师】状态已更新为：简历已被查看', 4, 0, 16, '2026-05-31 11:10:49', 0);
INSERT INTO `notification` VALUES (18, 2, '很遗憾，您未被录取', '您投递的岗位【Python开发工程师】状态已更新为：很遗憾，您未被录取', 3, 0, 9, '2026-05-31 11:10:58', 0);
INSERT INTO `notification` VALUES (19, 2, '简历已被查看', '您投递的岗位【Java开发工程师】状态已更新为：简历已被查看', 4, 0, 15, '2026-05-31 11:21:49', 0);
INSERT INTO `notification` VALUES (20, 2, '投递成功', '您已成功投递岗位【Java开发工程师】，匹配度得分: 60分', 1, 0, 17, '2026-05-31 12:50:43', 0);
INSERT INTO `notification` VALUES (21, 2, '恭喜您获得面试通知', '您投递的岗位【Python开发工程师】状态已更新为：恭喜您获得面试通知', 2, 0, 16, '2026-05-31 12:58:02', 0);
INSERT INTO `notification` VALUES (22, 2, '投递状态已更新', '您投递的岗位【Java开发工程师】状态已更新为：投递状态已更新', 4, 0, 13, '2026-05-31 12:58:21', 0);
INSERT INTO `notification` VALUES (23, 2, '简历已被查看', '您投递的岗位【数据分析师】状态已更新为：简历已被查看', 4, 0, 14, '2026-05-31 12:58:29', 0);
INSERT INTO `notification` VALUES (24, 2, '恭喜您被录取', '您投递的岗位【Python开发工程师】状态已更新为：恭喜您被录取', 2, 0, 16, '2026-05-31 12:58:45', 0);
INSERT INTO `notification` VALUES (25, 2, '简历已被查看', '您投递的岗位【Python开发工程师】状态已更新为：简历已被查看', 4, 0, 9, '2026-05-31 12:59:06', 0);
INSERT INTO `notification` VALUES (26, 2, '恭喜您获得面试通知', '您投递的岗位【Java高级开发工程师】状态已更新为：恭喜您获得面试通知，备注：简历已查看，等待用人部门评估', 2, 0, 2, '2026-05-31 12:59:33', 0);
INSERT INTO `notification` VALUES (27, 3, '恭喜您获得面试通知', '您投递的岗位【Python开发工程师】状态已更新为：恭喜您获得面试通知', 2, 0, 3, '2026-05-31 12:59:49', 0);
INSERT INTO `notification` VALUES (28, 4, '恭喜您获得面试通知', '您投递的岗位【前端开发工程师】状态已更新为：恭喜您获得面试通知，备注：简历已查看，技术评估中', 2, 0, 5, '2026-05-31 13:00:36', 0);
INSERT INTO `notification` VALUES (29, 2, '恭喜您获得面试通知', '您投递的岗位【Java开发工程师】状态已更新为：恭喜您获得面试通知，备注：面试安排：2025-06-20 14:00，线上面试', 2, 0, 1, '2026-05-31 13:00:58', 0);
INSERT INTO `notification` VALUES (30, 2, '恭喜您获得面试通知', '您投递的岗位【Java高级开发工程师】状态已更新为：恭喜您获得面试通知，备注：简历已查看，等待用人部门评估', 2, 1, 2, '2026-05-31 13:01:10', 0);
INSERT INTO `notification` VALUES (31, 4, '简历已被查看', '您投递的岗位【前端开发工程师】状态已更新为：简历已被查看，备注：简历已查看，技术评估中', 4, 0, 5, '2026-05-31 13:01:26', 0);
INSERT INTO `notification` VALUES (32, 4, '简历已被查看', '您投递的岗位【前端开发工程师】状态已更新为：简历已被查看，备注：简历已查看，技术评估中', 4, 0, 5, '2026-05-31 13:02:25', 0);
INSERT INTO `notification` VALUES (33, 2, '很遗憾，您未被录取', '您投递的岗位【Python开发工程师】状态已更新为：很遗憾，您未被录取', 3, 0, 9, '2026-05-31 13:02:45', 0);
INSERT INTO `notification` VALUES (34, 2, '简历已被查看', '您投递的岗位【Java开发工程师】状态已更新为：简历已被查看', 4, 0, 17, '2026-06-01 14:35:46', 0);
INSERT INTO `notification` VALUES (35, 2, '投递成功', '您已成功投递岗位【Java开发工程师】，匹配度得分: 68分', 1, 0, 18, '2026-06-01 14:55:16', 0);
INSERT INTO `notification` VALUES (36, 2, '投递成功', '您已成功投递岗位【Java开发工程师】，匹配度得分: 25分', 1, 0, 19, '2026-06-01 14:55:26', 0);

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operation` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `params` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cost_time` bigint NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_operation_log_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_operation_log_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 692 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for resume
-- ----------------------------
DROP TABLE IF EXISTS `resume`;
CREATE TABLE `resume`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `file_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `file_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `file_size` bigint NULL DEFAULT NULL,
  `file_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `current_version` int NULL DEFAULT 1,
  `is_public` tinyint NULL DEFAULT 1,
  `match_score` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_resume_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '简历表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resume
-- ----------------------------
INSERT INTO `resume` VALUES (1, 2, '张三-Java开发工程师简历', 'zhangsan_resume_v3.pdf', '/files/resumes/zhangsan_resume_v3.pdf', 245760, 'pdf', '\n1. 2年 Java 后端开发经验，熟练使用 SpringBoot、MyBatis-Plus 等主流框架，独立负责多个业务模块从需求分析、方案设计、编码开发到上线维护全流程。\n\n2. 精通 MySQL 数据库，熟悉索引优化、慢查询调优、事务、锁机制；熟练使用 Redis 做缓存、防重复提交、数据去重。\n\n3. 具备接口开发、前后端联调、线上问题排查、版本迭代经验，了解 Linux 服务器部署、日志查看、项目运维基础操作。\n\n4. 熟悉微服务基础概念，了解 Spring Cloud 组件；代码规范，注重接口复用与性能优化，具备良好的沟通能力与团队协作能力。\n\n专业技能\n\n• 核心技术：Java 基础、集合、多线程、JVM、IO、并发编程、Lambda、反射、注解。\n\n• 主流框架：熟练 SpringBoot、Spring MVC、MyBatis、MyBatis-Plus、Spring AOP/IOC。\n\n• 数据库&缓存：精通 MySQL，掌握 SQL 优化、索引、事务、分表基础；熟练使用 Redis，理解缓存穿透/击穿/雪崩解决方案。\n\n• 中间件&服务：了解 Nginx、MQ 基础使用；熟练对接第三方API、云存储（阿里云OSS）、AI大模型接口。\n\n• 工程化：Maven/Git、IDEA、Linux 常用命令、Shell 简单脚本、项目打包部署、日志排查。', 4, 1, NULL, '2026-05-27 18:32:36', '2026-06-01 10:59:11', 0);
INSERT INTO `resume` VALUES (2, 3, '李四-高级Python开发工程师简历', 'lisi_resume_v1.pdf', '/files/resumes/lisi_resume_v1.pdf', 189440, 'pdf', '李四，3年Python开发经验，擅长Django/Flask框架，熟悉数据分析与机器学习，有推荐系统开发经验。', 1, 1, NULL, '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `resume` VALUES (3, 4, '王五-前端开发工程师简历', 'wangwu_resume_v2.pdf', '/files/resumes/wangwu_resume_v2.pdf', 204800, 'pdf', '王五，4年前端开发经验，精通React和Vue.js技术栈，有丰富的中后台系统开发经验，熟悉TypeScript和前端工程化。', 2, 1, NULL, '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `resume` VALUES (4, 5, '赵六-产品经理简历', 'zhaoliu_resume_v1.pdf', '/files/resumes/zhaoliu_resume_v1.pdf', 163840, 'pdf', '赵六，3年B端产品经理经验，擅长需求分析和产品规划，有SaaS产品从0到1的经验。', 1, 1, NULL, '2026-05-27 18:32:36', '2026-05-27 18:32:36', 0);
INSERT INTO `resume` VALUES (5, 2, '张三-技术管理方向简历', 'zhangsan_resume_mgmt_v1.pdf', '/files/resumes/zhangsan_resume_mgmt_v1.pdf', 215040, 'pdf', '张三，5年Java+2年团队管理经验', 1, 1, NULL, '2026-05-27 18:32:36', '2026-06-01 14:48:34', 0);
INSERT INTO `resume` VALUES (6, 2, '张三-Python开发工程师简历', 'zhangsan_resume_python_v1', '/files/resumes/zhangsan_resume_python_v1.pdf', 198898, 'pdf', '核心语言  \n- 精通Python，熟练运用Django、Flask等主流Web框架，主导开发高并发、可扩展的企业级应用，项目响应时间平均降低30%。\n数据库技术  \n- 精通MySQL、MongoDB数据库设计与优化，具备复杂查询（如多表联查、子查询）编写、索引优化及数据建模能力，曾优化查询性能提升50%以上。\n机器学习  \n- 熟悉TensorFlow、PyTorch深度学习框架，能独立完成模型训练、超参数调优及生产环境部署，参与项目预测准确率提升至95%。\n架构与设计  \n- 具备丰富的RESTful API设计与开发经验，注重接口规范（如OpenAPI 3.0）、安全性（JWT认证、OAuth2.0）及性能（缓存策略、限流机制），交付接口响应时间<200ms。\n开发环境  \n- 熟练使用Linux系统（CentOS/Ubuntu），掌握Docker容器化部署、Git版本控制及Nginx反向代理配置，具备CI/CD流水线搭建经验，实现自动化部署效率提升40%。', 2, 1, NULL, '2026-05-31 11:06:42', '2026-06-01 14:46:31', 0);

-- ----------------------------
-- Table structure for resume_skill
-- ----------------------------
DROP TABLE IF EXISTS `resume_skill`;
CREATE TABLE `resume_skill`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `resume_id` bigint NULL DEFAULT NULL,
  `skill_id` bigint NULL DEFAULT NULL,
  `skill_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_resume_skill_resume_id`(`resume_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 252 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '简历技能关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resume_skill
-- ----------------------------
INSERT INTO `resume_skill` VALUES (8, 2, 2, 'Python', '2026-05-27 18:32:36');
INSERT INTO `resume_skill` VALUES (9, 2, 8, 'MySQL', '2026-05-27 18:32:36');
INSERT INTO `resume_skill` VALUES (10, 2, 18, 'MongoDB', '2026-05-27 18:32:36');
INSERT INTO `resume_skill` VALUES (11, 3, 3, 'JavaScript', '2026-05-27 18:32:36');
INSERT INTO `resume_skill` VALUES (12, 3, 6, 'React', '2026-05-27 18:32:36');
INSERT INTO `resume_skill` VALUES (13, 3, 7, 'Vue', '2026-05-27 18:32:36');
INSERT INTO `resume_skill` VALUES (14, 3, 16, 'TypeScript', '2026-05-27 18:32:36');
INSERT INTO `resume_skill` VALUES (15, 3, 17, 'Node.js', '2026-05-27 18:32:36');
INSERT INTO `resume_skill` VALUES (55, 1, NULL, 'Java', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (56, 1, NULL, 'Spring', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (57, 1, NULL, 'SpringBoot', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (58, 1, NULL, 'MyBatis', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (59, 1, NULL, 'MySQL', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (60, 1, NULL, 'Redis', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (61, 1, NULL, 'Git', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (62, 1, NULL, 'Linux', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (63, 1, NULL, '阿里云', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (64, 1, NULL, '运维', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (65, 1, NULL, '###', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (66, 1, NULL, '####', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (67, 1, NULL, 'Lambda', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (68, 1, NULL, 'MVC', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (69, 1, NULL, 'DeepSeek', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (70, 1, NULL, 'AI', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (71, 1, NULL, 'Maven', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (72, 1, NULL, 'API', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (73, 1, NULL, 'Tracking', '2026-05-31 10:56:55');
INSERT INTO `resume_skill` VALUES (125, 6, NULL, 'Python', '2026-06-01 14:33:35');
INSERT INTO `resume_skill` VALUES (126, 6, NULL, 'Django', '2026-06-01 14:33:35');
INSERT INTO `resume_skill` VALUES (127, 6, NULL, 'Flask', '2026-06-01 14:33:35');
INSERT INTO `resume_skill` VALUES (128, 6, NULL, 'MySQL', '2026-06-01 14:33:35');
INSERT INTO `resume_skill` VALUES (129, 6, NULL, 'MongoDB', '2026-06-01 14:33:35');
INSERT INTO `resume_skill` VALUES (130, 6, NULL, 'Docker', '2026-06-01 14:33:35');
INSERT INTO `resume_skill` VALUES (131, 6, NULL, 'Git', '2026-06-01 14:33:35');
INSERT INTO `resume_skill` VALUES (132, 6, NULL, 'Linux', '2026-06-01 14:33:35');
INSERT INTO `resume_skill` VALUES (133, 6, NULL, '机器学习', '2026-06-01 14:33:35');
INSERT INTO `resume_skill` VALUES (134, 6, NULL, '深度学习', '2026-06-01 14:33:35');
INSERT INTO `resume_skill` VALUES (135, 6, NULL, 'JWT', '2026-06-01 14:33:35');
INSERT INTO `resume_skill` VALUES (136, 6, NULL, 'PyTorch', '2026-06-01 14:33:35');
INSERT INTO `resume_skill` VALUES (163, 5, NULL, 'Java', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (164, 5, NULL, 'Spring', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (165, 5, NULL, 'MyBatis', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (166, 5, NULL, 'MySQL', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (167, 5, NULL, 'MongoDB', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (168, 5, NULL, 'Redis', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (169, 5, NULL, 'Elasticsearch', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (170, 5, NULL, 'Docker', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (171, 5, NULL, 'Kubernetes', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (172, 5, NULL, 'Jenkins', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (173, 5, NULL, 'Git', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (174, 5, NULL, 'Linux', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (175, 5, NULL, '团队管理', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (176, 5, NULL, '测试', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (177, 5, NULL, '运维', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (178, 5, NULL, 'com', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (179, 5, NULL, 'JPA', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (180, 5, NULL, 'Nginx', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (181, 5, NULL, 'Code', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (182, 5, NULL, 'Review', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (183, 5, NULL, 'Cloud', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (184, 5, NULL, 'Boot', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (185, 5, NULL, 'RocketMQ', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (186, 5, NULL, 'Alibaba', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (187, 5, NULL, 'Nacos', '2026-06-01 14:40:09');
INSERT INTO `resume_skill` VALUES (188, 5, NULL, 'Sentinel', '2026-06-01 14:40:09');

-- ----------------------------
-- Table structure for resume_version
-- ----------------------------
DROP TABLE IF EXISTS `resume_version`;
CREATE TABLE `resume_version`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `resume_id` bigint NULL DEFAULT NULL,
  `version` int NULL DEFAULT NULL,
  `file_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `file_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `is_current` tinyint NULL DEFAULT 1,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_resume_version_resume_id`(`resume_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '简历版本表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resume_version
-- ----------------------------
INSERT INTO `resume_version` VALUES (1, 1, 1, 'zhangsan_resume_v1.pdf', '/files/resumes/v1/zhangsan_resume_v1.pdf', '张三，初级Java开发，1年经验。', 0, '2026-05-27 18:32:36', 0);
INSERT INTO `resume_version` VALUES (2, 1, 2, 'zhangsan_resume_v2.pdf', '/files/resumes/v2/zhangsan_resume_v2.pdf', '张三，Java开发工程师，3年经验，熟悉Spring。', 0, '2026-05-27 18:32:36', 0);
INSERT INTO `resume_version` VALUES (3, 1, 3, 'zhangsan_resume_v3.pdf', '/files/resumes/v3/zhangsan_resume_v3.pdf', '张三，5年Java开发经验，精通Spring Boot微服务架构。', 0, '2026-05-27 18:32:36', 0);
INSERT INTO `resume_version` VALUES (4, 3, 1, 'wangwu_resume_v1.pdf', '/files/resumes/v1/wangwu_resume_v1.pdf', '王五，前端开发，2年经验，熟悉jQuery和Bootstrap。', 0, '2026-05-27 18:32:36', 0);
INSERT INTO `resume_version` VALUES (5, 3, 2, 'wangwu_resume_v2.pdf', '/files/resumes/v2/wangwu_resume_v2.pdf', '王五，4年前端开发经验，精通React和Vue.js。', 1, '2026-05-27 18:32:36', 0);
INSERT INTO `resume_version` VALUES (8, 1, 4, 'zhangsan_resume_v4.pdf', '/files/resumes/zhangsan_resume_v4.pdf', '1. 2年 Java 后端开发经验，熟练使用 SpringBoot、MyBatis-Plus 等主流框架，独立负责多个业务模块从需求分析、方案设计、编码开发到上线维护全流程。\n2. 精通 MySQL 数据库，熟悉索引优化、慢查询调优、事务、锁机制；熟练使用 Redis 做缓存、防重复提交、数据去重。\n3. 具备接口开发、前后端联调、线上问题排查、版本迭代经验，了解 Linux 服务器部署、日志查看、项目运维基础操作。\n4. 熟悉微服务基础概念，了解 Spring Cloud 组件；代码规范，注重接口复用与性能优化，具备良好的沟通能力与团队协作能力。\n专业技能\n• 核心技术：Java 基础、集合、多线程、JVM、IO、并发编程、Lambda、反射、注解。\n• 主流框架：熟练 SpringBoot、Spring MVC、MyBatis、MyBatis-Plus、Spring AOP/IOC。\n• 数据库&缓存：精通 MySQL，掌握 SQL 优化、索引、事务、分表基础；熟练使用 Redis，理解缓存穿透/击穿/雪崩解决方案。\n• 中间件&服务：了解 Nginx、MQ 基础使用；熟练对接第三方API、云存储（阿里云OSS）、AI大模型接口。\n• 工程化：Maven/Git、IDEA、Linux 常用命令、Shell 简单脚本、项目打包部署、日志排查。', 1, '2026-05-31 10:56:55', 0);
INSERT INTO `resume_version` VALUES (14, 6, 1, 'zhangsan_resume_python_v1', '/files/resumes/zhangsan_resume_python_v1.pdf', '张三\n核心语言： 精通Python，熟练运用Django、Flask等主流Web框架进行企业级应用开发。\n数据库技术： 精通MySQL、MongoDB数据库设计与优化，具备复杂查询、索引优化及数据建模能力。\n机器学习：熟悉TensorFlow、PyTorch深度学习框架，能独立完成模型训练、调优及部署。\n架构与设计：具备丰富的RESTful API设计与开发经验，注重接口规范、安全性与性能。\n开发环境： 熟练使用Linux系统，掌握Docker、Git、Nginx等常用工具，具备高效的DevOps流程意识。', 0, '2026-06-01 11:00:02', 0);
INSERT INTO `resume_version` VALUES (15, 6, 2, 'zhangsan_resume_python_v1', '/files/resumes/zhangsan_resume_python_v1.pdf', '核心语言  \n- 精通Python，熟练运用Django、Flask等主流Web框架，主导开发高并发、可扩展的企业级应用，项目响应时间平均降低30%。\n\n数据库技术  \n- 精通MySQL、MongoDB数据库设计与优化，具备复杂查询（如多表联查、子查询）编写、索引优化及数据建模能力，曾优化查询性能提升50%以上。\n\n机器学习  \n- 熟悉TensorFlow、PyTorch深度学习框架，能独立完成模型训练、超参数调优及生产环境部署，参与项目预测准确率提升至95%。\n\n架构与设计  \n- 具备丰富的RESTful API设计与开发经验，注重接口规范（如OpenAPI 3.0）、安全性（JWT认证、OAuth2.0）及性能（缓存策略、限流机制），交付接口响应时间<200ms。\n\n开发环境  \n- 熟练使用Linux系统（CentOS/Ubuntu），掌握Docker容器化部署、Git版本控制及Nginx反向代理配置，具备CI/CD流水线搭建经验，实现自动化部署效率提升40%。\n\n\n\n优化建议\n\n1. 量化成果以增强说服力  \n   将模糊描述（如“优化性能”）替换为具体数字（如“查询性能提升50%”），突出实际贡献，吸引招聘方注意。\n\n2. 使用专业动词和行业关键词  \n   用“主导开发”“编写”“优化”等主动动词替换“运用”“具备”，同时加入ATS关键词（如CI/CD、JWT、OpenAPI），提高简历匹配度。\n\n3. 结构化排版提升可读性  \n   采用分点式布局（每项技能独立成段），并添加小标题（如“核心语言”“数据库技术”），便于HR快速扫描关键信息。\n\n4. 突出核心竞争力  \n   在每项技能中强调具体成果（如“响应时间降低30%”），避免泛泛而谈，展示解决问题的能力。\n\n5. 添加技术栈细节  \n   补充具体工具或框架版本（如Django 4.2、PyTorch 2.0），体现技术深度和时效性。\n\n\n\n希望以上优化能帮助您在求职中脱颖而出！如需进一步调整或扩展其他部分（如项目经验、教育背景），请随时告知。', 1, '2026-06-01 14:33:35', 0);
INSERT INTO `resume_version` VALUES (18, 5, 1, 'zhangsan_resume_mgmt_v1.pdf', '/files/resumes/zhangsan_resume_mgmt_v1.pdf', '张三\nJava开发工程师 / 技术团队负责人\n手机：138-xxxx-xxxx | 邮箱：zhangsan@email.com | 所在地：北京\nGitHub/博客：github.com/yourname | 个人网站：yourblog.com (可选)\n\n【专业概要】\n\n拥有5年Java后端开发经验及2年技术团队管理经验，具备从0到1构建高并发、高可用分布式系统的实战能力。擅长技术选型、架构设计与性能调优，同时具备跨部门协作与团队梯队建设能力。曾主导【项目名称】，实现系统QPS提升【数字】%、响应时间降低【数字】%，并成功带领【数字】人团队完成【核心里程碑】。\n\n【核心技能】\n\n   后端开发：Java (8/11/17), Spring Boot/Cloud, MyBatis, JPA, 微服务架构\n   中间件/数据库：Redis, RocketMQ/Kafka, MySQL, MongoDB, Elasticsearch\n   运维与工具：Docker, Kubernetes (K8s), Jenkins, Git, Linux, Nginx\n   架构能力：分布式事务（Seata）、服务治理（Nacos/Sentinel）、高并发设计、缓存策略\n   管理能力：Scrum/Kanban敏捷管理、技术方案评审、Code Review、团队培养与激励\n\n【工作经历】\n\n公司名称 | 技术团队负责人 / 资深Java开发 | 2021.04 - 至今\n\n   团队管理：负责【数字】人技术团队的日常管理，制定开发规范与迭代流程，通过Code Review和知识分享，提升团队代码质量与交付效率【数字】%。\n   架构升级：主导公司核心业务系统从单体架构向微服务架构的迁移，设计基于Spring Cloud Alibaba的微服务治理方案，实现服务解耦与弹性伸缩。\n   性能优化：针对线上高并发场景，通过Redis缓存、数据库索引优化及异步消息队列（RocketMQ）等手段，将核心接口的TP99响应时间从【数字】ms降至【数字】ms，系统可用性达到99.99%。\n   项目交付：作为核心开发与项目Owner，主导【项目A】、【项目B】的研发与上线，支撑日均【数字】万UV、峰值QPS【数字】的业务量，保障系统稳定运行零P0级事故。\n   团队建设：建立新人导师制与OKR目标管理体系，成功培养2名技术骨干晋升为高级工程师。\n\n前公司名称 | Java开发工程师 | 2019.03 - 2021.03\n\n   功能开发：独立负责【业务模块X】的需求分析、设计、开发与测试，交付【数字】个核心功能模块，代码复用率提升【数字】%。\n   技术攻坚：解决分布式环境下数据一致性问题，引入Seata分布式事务框架，实现跨服务的数据最终一致性，业务差错率降低至【数字】%以下。\n   质量保障：编写单元测试覆盖率达【数字】%，并搭建自动化集成测试流程，将线上Bug率降低【数字】%。\n   协作沟通：与产品、测试、运维团队高效协作，参与【数字】次大版本迭代，均按时高质量上线。\n\n【项目经验】\n\n项目名称：高并发电商秒杀系统 (2023.05 - 2023.09)\n\n   角色：技术负责人 / 核心开发\n   技术栈：Spring Boot + Redis + RocketMQ + MySQL + Nginx\n   项目描述：设计并实现了一套支持百万级并发抢购的秒杀系统。\n   核心贡献：\n       设计基于Redis的预减库存与令牌桶限流方案，成功抵挡了瞬时峰值流量，系统未发生雪崩。\n       使用RocketMQ异步落单与削峰填谷，将数据库写入压力降低【数字】%。\n       最终实现系统在【数字】QPS下稳定运行，下单成功率提升至【数字】%。\n\n项目名称：企业级SaaS服务平台 (2021.10 - 2022.06)\n\n   角色：后端开发 / 模块负责人\n   技术栈：Spring Cloud Alibaba + Nacos + Sentinel + Docker\n   项目描述：构建多租户SaaS平台，支持动态资源分配与租户隔离。\n   核心贡献：\n       设计并实现基于Nacos的动态配置中心与Sentinel的熔断降级策略，保障了多租户场景下的服务稳定性。\n       主导服务容器化部署，使用Docker Compose与K8s进行编排，实现一键部署与弹性扩缩容，运维效率提升【数字】%。\n\n【教育背景】\n\n   大学名称 | 计算机科学与技术 | 本科 | 2015.09 - 2019.06', 1, '2026-06-01 14:39:56', 0);

-- ----------------------------
-- Table structure for skill_tag
-- ----------------------------
DROP TABLE IF EXISTS `skill_tag`;
CREATE TABLE `skill_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '技能标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of skill_tag
-- ----------------------------
INSERT INTO `skill_tag` VALUES (1, 'Java', '编程语言', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (2, 'Python', '编程语言', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (3, 'JavaScript', '编程语言', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (4, 'Spring Boot', '后端框架', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (5, 'MyBatis', '后端框架', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (6, 'React', '前端框架', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (7, 'Vue', '前端框架', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (8, 'MySQL', '数据库', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (9, 'Redis', '中间件', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (10, 'Docker', '运维工具', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (11, 'Git', '开发工具', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (12, 'Linux', '操作系统', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (13, 'Nginx', '中间件', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (14, 'HTML', '前端技术', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (15, 'CSS', '前端技术', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (16, 'TypeScript', '编程语言', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (17, 'Node.js', '后端框架', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (18, 'MongoDB', '数据库', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (19, 'Elasticsearch', '中间件', '2026-05-27 18:32:36');
INSERT INTO `skill_tag` VALUES (20, 'Kubernetes', '运维工具', '2026-05-27 18:32:36');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` tinyint NULL DEFAULT 0 COMMENT '0=求职者 1=管理员',
  `status` tinyint NULL DEFAULT 0 COMMENT '0=正常 1=禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '$2a$10$phHHrMcw8q7H5gwFZph14uT5PfVzZ/V8x3hGA.Fo.eBzi7FVKtFcm', '管理员', '13800000006', 'admin@resumematch.com', NULL, 1, 0, '2026-05-27 18:32:36', '2026-05-30 21:54:23', 0);
INSERT INTO `user` VALUES (2, 'zhangsan', '$2a$10$OtG7UzwDm7Hxm9wBXqUniOL.WIv8noVWPIFu4UFHgBAL416Ld/Efe', '张三', '13800000000', 'zhangsan@qq.com', NULL, 0, 0, '2026-05-27 18:32:36', '2026-05-31 13:57:00', 0);
INSERT INTO `user` VALUES (3, 'lisi', '$2a$10$l39JEnsgZLcAI/Q1Pq62gutJ/E/onsrVtvJRbkkBQb4y/ocCU4glO', '李四', '13800000003', 'lisi@qq.com', NULL, 0, 0, '2026-05-27 18:32:36', '2026-05-29 16:37:25', 0);
INSERT INTO `user` VALUES (4, 'wangwu', '$2a$10$l39JEnsgZLcAI/Q1Pq62gutJ/E/onsrVtvJRbkkBQb4y/ocCU4glO', '王五', '13800000004', 'wangwu@qq.com', NULL, 0, 0, '2026-05-27 18:32:36', '2026-05-29 16:37:25', 0);
INSERT INTO `user` VALUES (5, 'zhaoliu', '$2a$10$l39JEnsgZLcAI/Q1Pq62gutJ/E/onsrVtvJRbkkBQb4y/ocCU4glO', '赵六', '13800000005', 'zhaoliu@qq.com', NULL, 0, 0, '2026-05-27 18:32:36', '2026-05-29 16:37:25', 0);
INSERT INTO `user` VALUES (6, 'uu', '$2a$10$l39JEnsgZLcAI/Q1Pq62gutJ/E/onsrVtvJRbkkBQb4y/ocCU4glO', 'uu', '13232323232', 'uu@qq.com', NULL, 1, 1, '2026-05-29 15:22:53', '2026-06-01 15:02:19', 0);

SET FOREIGN_KEY_CHECKS = 1;
