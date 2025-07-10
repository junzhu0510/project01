# 🎲 抽奖系统 (Lottery System)

基于 Vue3 + Spring Boot + Spring Cloud 的现代化微服务抽奖系统，提供完整的用户管理、抽奖游戏、中奖记录查询等功能。采用前后端分离架构，支持高并发、高可用的企业级应用场景。

## 🎯 功能特性

### 核心功能
- **用户管理**: 用户注册、登录、密码重置、忘记密码、JWT认证
- **抽奖系统**: 公平的随机抽奖算法，支持多种奖品，实时转盘动画效果
- **中奖记录**: 完整的中奖历史查询、分页浏览、详情查看
- **安全认证**: JWT令牌认证、用户状态管理、自动登录
- **响应式设计**: 支持PC端和移动端，现代化UI界面
- **消息队列**: 基于RabbitMQ的异步消息处理
- **微服务架构**: 服务注册发现、配置管理、API网关

### 技术特性
- **前端框架**: Vue 3.3.4 + Composition API
- **UI组件库**: Element Plus 2.3.12
- **状态管理**: Pinia 2.1.6
- **路由管理**: Vue Router 4.2.4
- **HTTP客户端**: Axios 1.5.0
- **构建工具**: Vite 4.4.9
- **样式预处理**: Sass 1.66.1
- **后端框架**: Spring Boot + Spring Cloud + MyBatis Plus
- **数据库**: MySQL 8.0
- **缓存**: Redis 6.0+
- **消息队列**: RabbitMQ 3.12+
- **服务治理**: Nacos (注册中心 + 配置中心)
- **API网关**: Spring Cloud Gateway
- **容器化**: Docker + Docker Compose
- **反向代理**: Nginx

## 🏗️ 系统架构

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   前端 (Vue3)    │────│  Nginx 反向代理   │────│   API 网关       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                        ┌───────────────────────────────┼───────────────────────────────┐
                        │                               │                               │
                ┌───────▼────────┐              ┌───────▼────────┐              ┌───────▼────────┐
                │   用户服务       │              │   抽奖服务       │              │ RabbitMQ服务    │
                │ (user-login)   │              │   (prize)      │              │(rabbitmq-service)│
                └────────────────┘              └────────────────┘              └────────────────┘
                        │                               │                               │
                        └───────────────────────────────┼───────────────────────────────┘
                                                        │
                ┌───────────────────────────────────────┼───────────────────────────────────────┐
                │                                       │                                       │
        ┌───────▼────────┐                      ┌───────▼────────┐                      ┌───────▼────────┐
        │     MySQL      │                      │     Redis      │                      │   RabbitMQ     │
        │   (数据存储)     │                      │    (缓存)       │                      │   (消息队列)     │
        └────────────────┘                      └────────────────┘                      └────────────────┘
                                                        │
                                                ┌───────▼────────┐
                                                │     Nacos      │
                                                │ (注册中心+配置中心) │
                                                └────────────────┘
```

## 📦 项目结构

```
wjz/
├── user/
│   └── user-login/                 # 用户服务
│       ├── src/main/java/          # 后端Java代码
│       ├── frontend/               # Vue3前端项目
│       │   ├── src/
│       │   │   ├── views/          # 页面组件
│       │   │   │   ├── LoginView.vue
│       │   │   │   ├── RegisterView.vue
│       │   │   │   ├── LotteryView.vue
│       │   │   │   ├── PrizeHistoryView.vue
│       │   │   │   ├── ForgotPasswordView.vue
│       │   │   │   └── ResetPasswordView.vue
│       │   │   ├── stores/         # Pinia状态管理
│       │   │   ├── utils/          # 工具函数
│       │   │   └── assets/         # 静态资源
│       │   ├── package.json
│       │   └── vite.config.js
│       └── pom.xml
├── prize/                          # 抽奖服务
│   ├── src/main/java/
│   └── pom.xml
├── prize-record-service/           # 中奖记录服务
│   ├── src/main/java/
│   └── pom.xml
├── rabbitmq-service/               # RabbitMQ服务
│   ├── src/main/java/
│   └── pom.xml
├── gateway/                        # API网关
│   ├── src/main/java/
│   └── pom.xml
├── nginx/
│   └── nginx.conf                  # Nginx配置
├── logs/                           # 系统日志
├── docker-compose.yml              # Docker编排文件
├── build-frontend.bat              # 前端构建脚本
├── pom.xml                         # 父级POM
└── README.md
```

## 🚀 快速开始

### 环境要求

- **开发环境**:
  - JDK 17+
  - Maven 3.6+
  - Node.js 16+
  - MySQL 8.0+
  - Redis 6.0+
  - RabbitMQ 3.12+

- **生产环境**:
  - Docker 20.0+
  - Docker Compose 2.0+

### 方式一：Docker 一键部署 (推荐)

1. **克隆项目**
```bash
git clone <repository-url>
cd wjz
```

2. **构建前端**
```bash
cd user/user-login/frontend
npm install
npm run build
cd ../../../
```

3. **启动所有服务**
```bash
docker-compose up -d
```

4. **访问系统**
- 前端页面: http://localhost
- API网关: http://localhost:8080
- Nacos控制台: http://localhost:8848/nacos (nacos/nacos)
- RabbitMQ管理界面: http://localhost:15672 (admin/admin123)

### 方式二：本地开发部署

1. **启动基础服务**
```bash
# 启动MySQL、Redis、RabbitMQ、Nacos
docker-compose up -d mysql redis rabbitmq nacos
```

2. **初始化数据库**
```bash
# 连接MySQL并执行sql/init.sql脚本
mysql -h localhost -u root -p < sql/init.sql
```

3. **启动后端服务**
```bash
# 启动网关
cd gateway
mvn spring-boot:run

# 启动用户服务
cd ../user/user-login
mvn spring-boot:run

# 启动抽奖服务
cd ../../prize
mvn spring-boot:run

# 启动RabbitMQ服务
cd ../rabbitmq-service
mvn spring-boot:run
```

4. **启动前端**
```bash
cd user/user-login/frontend
npm install
npm run dev
```

## 🎮 使用说明

### 用户操作流程

1. **注册账号**
   - 访问注册页面
   - 输入用户名和密码（5-16位字符）
   - 系统自动验证用户名唯一性
   - 注册成功后自动跳转登录页面

2. **登录系统**
   - 输入用户名和密码
   - 支持"忘记密码"功能
   - 系统验证成功后返回JWT令牌
   - 自动跳转到抽奖页面，显示用户信息

3. **密码管理**
   - **忘记密码**: 通过用户名重置密码
   - **密码重置**: 输入新密码并确认
   - 安全验证机制保护用户账户

4. **参与抽奖**
   - 查看精美的转盘式抽奖界面
   - 实时显示剩余抽奖次数
   - 点击"开始抽奖"体验动画效果
   - 弹窗显示抽奖结果和奖品详情

5. **查看中奖记录**
   - 进入"中奖记录"页面
   - 查看完整的中奖历史信息
   - 支持分页浏览和详情查看
   - 显示中奖时间、奖品信息、状态等

### 管理员功能

- **奖品管理**: 通过API接口管理奖品信息
- **用户管理**: 查看用户注册和抽奖情况
- **系统监控**: 通过Nacos和RabbitMQ管理界面监控系统状态

## 🔧 配置说明

### 数据库配置

默认数据库配置:
- 数据库名: `lottery_system`
- 用户名: `lottery`
- 密码: `lottery123`

### 奖品配置

系统预置了以下奖品:
- **一等奖**: iPhone 15 Pro Max (1%概率) - 获得iPhone 15 Pro Max一台
- **二等奖**: AirPods Pro (5%概率) - 获得AirPods Pro一副
- **三等奖**: 100元京东购物卡 (10%概率) - 获得100元京东购物卡
- **四等奖**: 20元话费充值卡 (20%概率) - 获得20元话费充值卡
- **五等奖**: 5元现金红包 (30%概率) - 获得5元现金红包
- **谢谢参与**: (34%概率) - 很遗憾，未中奖

> 💡 **提示**: 管理员可通过API接口动态调整奖品配置和中奖概率

### 服务端口

- Nginx: 80
- API网关: 8080
- 用户服务: 8081
- 抽奖服务: 8082
- RabbitMQ服务: 8083
- MySQL: 3306
- Redis: 6379
- RabbitMQ: 5672 (AMQP), 15672 (管理界面)
- Nacos: 8848

## 🔍 API 接口

### 用户服务 API

```
POST /api/user/register     # 用户注册
POST /api/user/login        # 用户登录
```

### 抽奖服务 API

```
POST /api/lottery/draw-json     # 执行抽奖（JSON格式）
GET  /api/lottery/chances       # 获取用户剩余抽奖次数
GET  /api/lottery/prizes        # 获取奖品列表
GET  /api/lottery/history       # 获取中奖历史（分页）
POST /api/prize/create          # 创建奖品
PUT  /api/prize/update          # 更新奖品
DELETE /api/prize/delete        # 删除奖品
```

### 中奖记录服务 API

```
GET  /api/prize-record/user/{userId}    # 获取用户中奖记录
GET  /api/prize-record/detail/{id}      # 获取中奖记录详情
POST /api/prize-record/create           # 创建中奖记录
```

## 🛠️ 开发指南

### 前端开发

1. **添加新页面**
   - 在 `src/views/` 目录下创建Vue组件
   - 在 `src/router/index.js` 中配置路由
   - 使用Element Plus组件库快速构建UI

2. **状态管理**
   - 使用Pinia进行状态管理
   - 在 `src/stores/` 目录下创建store模块
   - 支持持久化存储和响应式更新

3. **样式开发**
   - 支持Sass预处理器
   - 采用scoped样式避免污染
   - 响应式设计适配多端设备

### 后端开发

1. **添加新奖品**
   - 通过API接口或直接在数据库中添加奖品记录
   - 设置奖品的概率、数量、描述等信息
   - 确保总概率不超过100%

2. **自定义抽奖算法**
   - 修改 `LotteryServiceImpl.executeDrawAlgorithm()` 方法
   - 实现自定义抽奖逻辑和概率控制
   - 支持库存管理和中奖限制

3. **扩展消息队列**
   - 在 `RabbitMQConfig` 中添加新的队列和交换机配置
   - 实现异步消息处理和事件驱动架构

4. **微服务扩展**
   - 新增服务需在Nacos中注册
   - 通过Gateway配置路由规则
   - 遵循RESTful API设计规范

## 🐛 故障排除

### 常见问题

1. **服务启动失败**
   - 检查端口是否被占用 (`netstat -an | findstr :8080`)
   - 确认数据库连接配置和权限
   - 查看服务启动日志和错误信息
   - 确保JDK版本为17+

2. **前端无法访问后端**
   - 检查API网关服务是否正常运行
   - 确认CORS跨域配置
   - 检查Nginx代理配置和端口映射
   - 验证JWT令牌是否有效

3. **抽奖功能异常**
   - 检查用户剩余抽奖次数
   - 确认奖品概率配置和库存
   - 查看RabbitMQ消息队列状态
   - 检查数据库中奖记录表

4. **前端构建失败**
   - 确认Node.js版本为16+
   - 清除node_modules并重新安装依赖
   - 检查Vite配置和环境变量
   - 查看构建日志中的具体错误

5. **用户认证问题**
   - 检查JWT令牌配置和过期时间
   - 确认用户状态和权限
   - 查看登录日志和错误信息

### 日志查看

```bash
# 查看所有服务日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f gateway
docker-compose logs -f user-service
docker-compose logs -f prize-service
```

## 🎯 性能优化

### 前端优化
- **代码分割**: 使用Vue Router的懒加载
- **资源压缩**: Vite自动压缩CSS和JS
- **缓存策略**: 合理设置静态资源缓存
- **响应式图片**: 支持多种设备分辨率

### 后端优化
- **数据库优化**: 合理设计索引和查询
- **缓存机制**: Redis缓存热点数据
- **连接池**: 数据库连接池优化
- **异步处理**: RabbitMQ异步消息队列

## 🔒 安全特性

- **JWT认证**: 无状态令牌认证机制
- **密码加密**: BCrypt加密存储用户密码
- **CORS配置**: 跨域请求安全控制
- **输入验证**: 前后端双重数据验证
- **SQL注入防护**: MyBatis Plus参数化查询

## 📊 监控与日志

- **应用监控**: 通过Nacos监控服务状态
- **消息队列监控**: RabbitMQ管理界面
- **日志管理**: 结构化日志输出和归档
- **性能指标**: 响应时间和吞吐量监控

## 📄 许可证

MIT License - 详见 [LICENSE](LICENSE) 文件

## 🤝 贡献指南

欢迎贡献代码！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📞 技术支持

如有技术问题或建议，请通过以下方式联系：
- 📧 邮箱: support@lottery-system.com
- 🐛 问题反馈: [GitHub Issues](https://github.com/your-repo/issues)
- 📖 文档: [项目Wiki](https://github.com/your-repo/wiki)
- 💬 讨论: [GitHub Discussions](https://github.com/your-repo/discussions)

---

⭐ 如果这个项目对您有帮助，请给我们一个Star！