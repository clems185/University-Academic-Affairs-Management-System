const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    devServer: {
        proxy: {
            // 智能体API代理到Flask后端
            '/api/agentdash': {
                target: 'http://localhost:5000',
                changeOrigin: true,
                pathRewrite: { '^/api': '/api' }
            },
            '/api/agentmessage': {
                target: 'http://localhost:5000',
                changeOrigin: true,
                pathRewrite: { '^/api': '/api' }
            },
            // 其他API代理到.NET后端
            '/api': {
                target: 'http://localhost:5133', // .NET后端地址
                changeOrigin: true,
                pathRewrite: { '^/api': '/api' }
            }
        }
    }
})