# 抽奖系统后端

## 项目介绍
这是一个基于 Spring Boot + MyBatis-Plus + MySQL 开发的抽奖系统后端项目。主要功能包括：
- 用户注册和登录
- JWT 认证
- 抽奖功能
- 奖品管理
- 中奖记录管理

## 技术栈
- Spring Boot 3.x
- Spring Cloud Alibaba
- MyBatis-Plus
- MySQL 8.x
- JWT
- Nacos

## 开发环境要求
- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Nacos 2.x

## 项目启动

1. 启动 Nacos 服务
```bash
# Windows
nacos/bin/startup.cmd

# Linux/Mac
nacos/bin/startup.sh
```

2. 创建数据库和表
```sql
-- 创建数据库
CREATE DATABASE lottery_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 用户表
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 奖品表
CREATE TABLE `prize` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '奖品名称',
  `description` varchar(500) NOT NULL COMMENT '奖品描述',
  `probability` decimal(10,4) NOT NULL COMMENT '中奖概率',
  `stock` int NOT NULL COMMENT '库存',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='奖品表';

-- 中奖记录表
CREATE TABLE `prize_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `prize_id` bigint NOT NULL COMMENT '奖品ID',
  `status` varchar(20) NOT NULL COMMENT '状态：UNCLAIMED未领取，CLAIMED已领取',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='中奖记录表';
```

3. 修改配置文件
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lottery_system?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848

jwt:
  secret: your_jwt_secret_key
  expiration: 86400000  # 24小时
```

4. 启动项目
```bash
mvn spring-boot:run
```

## 项目结构
```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/wjz/user/
│   │   │       ├── config/      # 配置类
│   │   │       ├── controller/  # 控制器
│   │   │       ├── entity/      # 实体类
│   │   │       ├── mapper/      # MyBatis mapper
│   │   │       ├── service/     # 服务层
│   │   │       └── utils/       # 工具类
│   │   └── resources/
│   │       ├── mapper/          # MyBatis XML
│   │       └── application.yml  # 配置文件
│   └── test/                    # 测试代码
├── pom.xml                      # 项目依赖
└── README.md                    # 项目说明
```

## API 文档

### 用户相关
#### 注册
- URL: POST /api/user/register
- 请求体:
```json
{
  "username": "string",
  "password": "string"
}
```

#### 登录
- URL: POST /api/user/login
- 请求体:
```json
{
  "username": "string",
  "password": "string"
}
```

### 抽奖相关
#### 抽奖
- URL: POST /api/lottery/draw
- 请求头: Authorization: Bearer {token}

#### 获取抽奖次数
- URL: GET /api/lottery/chances
- 请求头: Authorization: Bearer {token}

#### 获取中奖历史
- URL: GET /api/lottery/history
- 请求头: Authorization: Bearer {token}
- 参数:
  - page: 页码
  - size: 每页大小

#### 领取奖品
- URL: POST /api/lottery/claim/{id}
- 请求头: Authorization: Bearer {token}

## 部署说明
1. 准备环境
   - JDK 17+
   - MySQL 8.0+
   - Nacos 2.x

2. 配置文件修改
   - 修改数据库连接信息
   - 修改 Nacos 配置
   - 修改 JWT 密钥

3. 打包
```bash
mvn clean package
```

4. 运行
```bash
java -jar target/user-login-0.0.1-SNAPSHOT.jar
```

## 注意事项
1. 确保 MySQL 和 Nacos 服务正常运行
2. 注意修改配置文件中的敏感信息
3. 生产环境部署时建议使用 HTTPS
4. 定期备份数据库

## 许可证
MIT License