import axios from 'axios'

// 创建axios实例
const request = axios.create({
    baseURL: '', // 开发环境使用相对路径，确保代理生效
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// 请求拦截器
request.interceptors.request.use(
    (config) => {
        // 可以在这里添加token等认证信息
        const token = localStorage.getItem('token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

// 响应拦截器
request.interceptors.response.use(
    (response) => {
        console.log('响应拦截器 - 原始响应:', response);
        console.log('响应拦截器 - response.data:', response.data);
        console.log('响应拦截器 - 返回类型:', typeof response.data);
        return response.data
    },
    (error) => {
        // 统一错误处理
        if (error.response) {
            // 服务器返回错误状态码
            console.error('响应错误:', error.response.data)
        } else if (error.request) {
            // 请求发送失败
            console.error('请求失败:', error.request)
        } else {
            // 其他错误
            console.error('请求配置错误:', error.message)
        }
        return Promise.reject(error)
    }
)

export default request

