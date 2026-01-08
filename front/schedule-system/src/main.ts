import { createApp } from 'vue'
import App from './App.vue'
import router from './router' 
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from 'axios';

axios.interceptors.request.use(config => {
  const token = sessionStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
    console.log('正在发送Token:', token); // 调试信息
  } else {
    console.warn('未找到Token'); // 调试信息
  }
  return config;
}, error => {
  return Promise.reject(error);
});

axios.interceptors.response.use(response => {
  return response;
}, error => {
  if (error.response?.status === 401) {
    // Token无效或过期
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('userType');
    window.location.href = '/';
  }
  return Promise.reject(error);
});

const app = createApp(App)
app.use(router) 
app.use(ElementPlus)
app.mount('#app')