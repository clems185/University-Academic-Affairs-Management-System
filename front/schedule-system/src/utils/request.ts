import axios, { AxiosRequestConfig } from 'axios';

// 创建 axios 实例
const instance = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL || '/api',
  timeout: 10000,
});

// 请求拦截器
instance.interceptors.request.use(
  (config) => {
    // 可在此添加认证 token
    // const token = localStorage.getItem('token');
    // if (token) {
    //   config.headers.Authorization = `Bearer ${token}`;
    // }
    return config;
  },
  (error) => Promise.reject(error)
);

// 响应拦截器
instance.interceptors.response.use(
  (response) => response.data,
  (error) => {
    // 统一错误处理
    console.error('API Error:', error.response?.data || error.message);
    return Promise.reject(error);
  }
);

// 封装 request 函数
const request = <T = any>(config: AxiosRequestConfig): Promise<T> => {
  return instance(config);
};

export default request;