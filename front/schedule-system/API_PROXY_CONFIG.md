# 前端API代理配置说明

## 代理规则

前端通过Vue CLI的代理功能，将不同的API请求路由到不同的后端服务：

### 智能体API（Flask后端 - 端口5000）

以下API请求会被代理到 `http://localhost:5000`：

- `/api/agentdash/*` - Dashboard智能客服
- `/api/agentmessage/*` - 消息审核智能助手

### 其他API（.NET后端 - 端口5133）

所有其他 `/api/*` 请求会被代理到 `http://localhost:5133`

## 配置位置

配置文件：`schedule-system/vue.config.js`

## 工作原理

1. 前端发起请求：`/api/agentdash/chat`
2. Vue CLI开发服务器拦截请求
3. 根据路径匹配规则，转发到对应的后端
4. 智能体API → Flask后端（5000端口）
5. 其他API → .NET后端（5133端口）

## 注意事项

1. **代理顺序很重要**：更具体的路径（如 `/api/agentdash`）必须放在更通用的路径（如 `/api`）之前
2. **开发环境**：此配置仅在开发环境（`npm run serve`）生效
3. **生产环境**：生产环境需要通过Nginx等反向代理服务器配置路由

## 验证代理是否生效

1. 启动Flask后端（端口5000）
2. 启动.NET后端（端口5133）
3. 启动前端开发服务器
4. 在浏览器开发者工具的Network标签中查看请求
5. 智能体API请求应该显示目标为 `localhost:5000`
6. 其他API请求应该显示目标为 `localhost:5133`

## 常见问题

### 1. 代理不生效

- 检查 `vue.config.js` 配置是否正确
- 重启前端开发服务器
- 检查后端服务是否启动

### 2. CORS错误

- Flask后端已配置CORS，允许跨域请求
- 如果仍有问题，检查Flask后端的CORS配置

### 3. 404错误

- 确认Flask后端已启动并监听5000端口
- 检查Flask后端的路由配置是否正确

