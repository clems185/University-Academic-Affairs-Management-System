# 前端API路由配置说明

## 当前配置状态

✅ **配置已完成** - `vue.config.js` 已正确配置代理规则

## 路由规则

### 智能体API → Flask后端（端口5000）

以下API请求会被自动代理到 `http://localhost:5000`：

1. **Dashboard智能客服**
   - 路径：`/api/agentdash/chat`
   - 后端：Flask (端口5000)
   - 前端调用：`sendQuestionToAgent()` in `src/api/agentdash/agentdash.ts`

2. **消息审核智能助手**
   - 路径：`/api/agentmessage/chat`
   - 后端：Flask (端口5000)
   - 前端调用：`sendQuestionToMessageAgent()` in `src/api/agentmessage/agentmessage.ts`

### 其他API → .NET后端（端口5133）

所有其他 `/api/*` 请求会被代理到 `http://localhost:5133`

## 工作原理

```
前端请求: /api/agentdash/chat
    ↓
Vue CLI开发服务器 (代理)
    ↓
匹配规则: /api/agentdash
    ↓
转发到: http://localhost:5000/api/agentdash/chat
    ↓
Flask后端处理并返回响应
```

## 验证配置

### 1. 检查代理配置

文件：`schedule-system/vue.config.js`

确保有以下配置：
```javascript
proxy: {
    '/api/agentdash': {
        target: 'http://localhost:5000',  // Flask后端
        ...
    },
    '/api/agentmessage': {
        target: 'http://localhost:5000',  // Flask后端
        ...
    },
    '/api': {
        target: 'http://localhost:5133',  // .NET后端
        ...
    }
}
```

### 2. 验证步骤

1. **启动Flask后端**
   ```bash
   cd flask
   python run.py
   ```
   应该看到：`Running on http://0.0.0.0:5000`

2. **启动.NET后端**
   确保.NET后端运行在 `http://localhost:5133`

3. **启动前端**
   ```bash
   cd schedule-system
   npm run serve
   ```

4. **测试智能体API**
   - 打开浏览器开发者工具（F12）
   - 切换到 Network 标签
   - 在Dashboard页面使用智能客服
   - 查看请求，应该显示：
     - Request URL: `http://localhost:8080/api/agentdash/chat` (前端地址)
     - 实际转发到: `http://localhost:5000/api/agentdash/chat` (Flask后端)

## 前端代码说明

### API调用方式

前端代码**不需要修改**，因为：

1. **使用相对路径**
   ```typescript
   // src/api/agentdash/agentdash.ts
   const instance = axios.create({
     baseURL: '/api',  // 相对路径，会被代理处理
     ...
   })
   ```

2. **请求路径**
   ```typescript
   instance.post('/agentdash/chat', params)
   // 完整路径: /api/agentdash/chat
   // 会被代理转发到: http://localhost:5000/api/agentdash/chat
   ```

## 常见问题

### Q: 为什么前端代码不需要指定端口5000？

A: 因为使用了Vue CLI的代理功能。前端代码使用相对路径 `/api`，开发服务器会根据路径规则自动转发到对应的后端。

### Q: 代理不生效怎么办？

A: 
1. 重启前端开发服务器
2. 检查 `vue.config.js` 配置
3. 确认Flask后端已启动
4. 查看浏览器控制台和Network标签

### Q: 生产环境怎么办？

A: 生产环境需要通过Nginx等反向代理服务器配置路由规则，将智能体API路由到Flask后端。

## 总结

✅ **前端已配置完成** - 智能体API会自动路由到Flask后端（5000端口）
✅ **无需修改前端代码** - API调用使用相对路径，代理会自动处理
✅ **开发环境已就绪** - 启动Flask后端后即可使用智能体功能

