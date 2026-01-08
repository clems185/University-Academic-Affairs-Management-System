import axios from 'axios'
import { LoginParams, LoginResult, UserInfo, UpdateUserInfoParams, ChangePasswordParams, UserApiResult, forgetPasswordParams, verificationParams } from './types'

// 创建axios实例
const instance = axios.create({
  baseURL: '/api',
  timeout: 10000,
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

export const login = (params: LoginParams) => {
  return instance.post<UserApiResult<LoginResult>>('/user/login', params)
    .then(response => {
      console.log('登录响应原始数据:', response.data)
      console.log('响应数据结构:', response.data)
      
      // 尝试不同的Token提取方式
      const token = (response.data.Result && response.data.Result.Token)
      
      console.log('提取到的Token:', token)
      
      if (token) {
        localStorage.setItem('token', token)
        console.log('Token已存储到localStorage')
        
        // 验证Token格式
        const tokenParts = token.split('.')
        if (tokenParts.length === 3) {
          try {
            const payload = JSON.parse(atob(tokenParts[1]))
            console.log('Token payload:', payload)
          } catch (e) {
            console.error('Token解码失败:', e)
          }
        } else {
          console.error('Token格式不正确，期望3部分，实际:', tokenParts.length)
        }
      } else {
        console.error('响应中没有找到Token字段')
        // 打印所有字段

      }
      return response
    })
    .catch(error => {
      console.error('登录详细错误:')
      console.error('错误信息:', error.message)
      console.error('请求配置:', error.config)
      console.error('响应数据:', error.response?.data)
      console.error('响应状态:', error.response?.status)
      throw error
    })
}

// 获取用户个人信息
export const getUserInfo = () => {
  return instance.get<UserApiResult<UserInfo>>('/user/info')
}

// 更新用户个人信息
export const updateUserInfo = (params: UpdateUserInfoParams) => {
  return instance.put<UserApiResult<any>>('/user/update', params)
}

// 修改密码
export const changePassword = (params: ChangePasswordParams) => {
  return instance.post<UserApiResult<any>>('/user/changePassword', params)
}

// 忘记密码
export const forgotPassword = (params: forgetPasswordParams) => {
  return instance.post<UserApiResult<any>>('/user/forgetPassword', params)
}

// 验证码
export const verification = (params: verificationParams) => {
  return instance.post<UserApiResult<any>>('/user/verification', params)
}