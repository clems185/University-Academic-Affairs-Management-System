import axios from 'axios'
import { MessageChatRequest, MessageChatResponse, AgentApiResult } from './types'

// 创建axios实例
const instance = axios.create({
  baseURL: '/api',
  timeout: 30000, // 智能体可能需要更长的响应时间
})

// 请求拦截器 - 添加token
instance.interceptors.request.use(
  (config) => {
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

// 响应拦截器 - 处理通用错误
instance.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// 发送问题给消息处理智能体
export const sendQuestionToMessageAgent = (params: MessageChatRequest) => {
  return instance.post<AgentApiResult<MessageChatResponse>>('/agentmessage/chat', params)
}

