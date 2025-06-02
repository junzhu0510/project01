# 抽奖系统前端

## 项目介绍
这是一个基于 Vue 3 + Vite + Element Plus 开发的抽奖系统前端项目。主要功能包括：
- 用户注册
- 用户登录
- 抽奖功能
- 中奖记录查看
- 奖品领取

## 技术栈
- Vue 3
- Vue Router
- Pinia
- Element Plus
- Axios
- Vite

## 开发环境要求
- Node.js >= 16.0.0
- npm >= 7.0.0

## 项目启动

1. 安装依赖
```bash
npm install
```

2. 启动开发服务器
```bash
npm run dev
```

3. 构建生产版本
```bash
npm run build
```

4. 预览生产构建
```bash
npm run preview
```

## 项目结构
```
├── src/
│   ├── assets/         # 静态资源
│   ├── components/     # 公共组件
│   ├── router/         # 路由配置
│   ├── stores/         # 状态管理
│   ├── utils/          # 工具函数
│   ├── views/          # 页面组件
│   ├── App.vue         # 根组件
│   └── main.js         # 入口文件
├── public/             # 公共资源
├── index.html          # HTML 模板
├── package.json        # 项目配置
├── vite.config.js      # Vite 配置
└── README.md           # 项目说明
```

## 开发注意事项
1. 本地开发时，API 请求会通过 Vite 的代理功能转发到后端服务
2. 确保后端服务已启动并运行在正确的端口（默认 8000）
3. 所有 API 请求都应该使用 @/utils/request.js 中封装的 axios 实例
4. 注意处理登录状态和 Token 的有效期

## 部署说明
1. 执行 `npm run build` 生成生产环境代码
2. 将 dist 目录下的文件部署到 Web 服务器
3. 配置 Web 服务器将 API 请求转发到后端服务

## 接口文档
### 用户相关
- POST /api/user/register - 用户注册
- POST /api/user/login - 用户登录

### 抽奖相关
- POST /api/lottery/draw - 进行抽奖
- GET /api/lottery/chances - 获取剩余抽奖次数
- GET /api/lottery/history - 获取中奖历史
- POST /api/lottery/claim/{id} - 领取奖品

## 贡献指南
1. Fork 本仓库
2. 创建特性分支
3. 提交代码
4. 创建 Pull Request

## 许可证
MIT License