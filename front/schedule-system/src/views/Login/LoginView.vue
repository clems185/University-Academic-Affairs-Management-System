<template>
  <div class="login-container">
    <div class="login-background"></div>
    <div class="login-gradient"></div>
    
    <el-card class="login-card">
      <h2 class="login-title">高校教务管理系统</h2>
      <div class="decoration-line"></div>
      
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef">
        <el-form-item prop="Name">
          <el-input 
            v-model="loginForm.Name" 
            placeholder="请输入学号/工号"
            class="custom-input"
          >
            <template #prefix>
              <i class="icon-user el-icon-user"></i>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item prop="Password">
          <el-input 
            v-model="loginForm.Password" 
            type="Password" 
            placeholder="请输入密码" 
            show-password
            class="custom-input"
          >
            <template #prefix>
              <i class="icon-lock el-icon-lock"></i>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            @click="onLogin"
            class="login-btn"
            :loading="loading"
          >
            登录
          </el-button>
        </el-form-item>
        <!-- 新增的链接行 -->
        <div class="login-links">
          <span class="link-item" style="cursor:pointer" @click="goInformation">使用说明</span>
          <span class="link-item" style="cursor:pointer" @click="goForget">激活/忘记密码</span>
        </div>
      </el-form>
      
      <div class="footer">
        <div class="dotted-line"></div>
        <div class="copyright">© 同济大学2025年数据库课设-教务管理系统</div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
import { login } from '@/api/user/user'
import { LoginParams } from '@/api/user/types'
import axios from 'axios';

const router = useRouter()
const route = useRoute()
const loading = ref(false)

const goForget = () => {
  router.push("/forget")
    .catch(err => console.error("跳转失败:", err))
}

const goInformation = () => {
  router.push("/information")
    .catch(err => console.error("跳转失败:", err))
}

const loginForm = ref<LoginParams>({
  Name: '',
  Password: '',
})

// 验证规则
const rules = {
  userId: [
    { required: true, message: '请输入学号/工号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const loginFormRef = ref()

const onLogin = async () => {
  loading.value = true
  try {
    const valid = await (loginFormRef.value as any).validate()
    if (valid) {
      const res = await login(loginForm.value)
      
      if (res.data?.IsSuccess) {
        // 存储用户信息
        if(res.data.Result) {
          sessionStorage.setItem('userType', res.data.Result.UserType?.toString() || '')
          sessionStorage.setItem('token', res.data.Result.Token || '')  
          sessionStorage.setItem('userId', res.data.Result.Id?.toString() || '')
          sessionStorage.setItem('name', res.data.Result.Name?.toString() || '')
          sessionStorage.setItem('nickName', res.data.Result.NickName?.toString() || '')
        }
        // 登录成功后
        localStorage.setItem('token', res.data.Result.Token);
        ElMessage.success('登录成功！')
        
        // 处理重定向路径
        const redirect = route.query.redirect as string | undefined
        let targetPath = '/workbench'
        
        if (redirect) {
          try {
            // 安全解码URL
            targetPath = decodeURIComponent(redirect)
          } catch (e) {
            console.warn('重定向路径解码错误:', e)
          }
        }
        
        // 强制刷新页面以确保路由状态重置
        window.location.replace(targetPath)
      } else {
        ElMessage.error(res.data?.Message || '登录失败，请检查凭证')
      }
    } else {
      ElMessage.error('请填写完整信息')
    }
  } catch (err) {
    console.error('登录异常:', err)
    ElMessage.error('网络错误或服务器异常')
  } finally {
    loading.value = false
  }
}
</script>
<style scoped lang="scss">
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-right: 5%;
  position: relative;
  overflow: hidden;
  
  .login-background {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: url('@/assets/background.png') no-repeat center center;
    background-size: cover;
    z-index: 1;
    filter: brightness(1) contrast(1) blur(0);
    transition: filter 0.3s ease;
  }

  .login-gradient {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 2;
    backdrop-filter: blur(0);
  }

  /* 登录卡片 */
  .login-card {
    position: relative;
    z-index: 3;
    height: 400px;
    width: 320px;
    padding: 0px 0px;
    margin-top: 0px;
    border-radius: 15px;
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(2px);
    transform: translateY(0);
    transition: all 0.3s ease;
    background: linear-gradient(
      135deg,
      rgba(255, 255, 255, 0.6),
      rgba(255, 255, 255, 0.5)
    );
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 15px 50px rgba(0, 0, 0, 0.2);
    }

    /* 标题 */
    .login-title {
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 22px;
      font-weight: 600;
      padding: 0px,0px;
      color: #1a365d;
      margin-bottom: 35px;
      margin-top: 0px;
      letter-spacing: 1.2px;
      text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
      position: relative;

      &::after {
        content: "";
        position: absolute;
        bottom: -10px;
        left: 50%;
        transform: translateX(-50%);
        width: 50px;
        height: 3px;
        background: linear-gradient(90deg, #3498db, #2980b9);
        border-radius: 2px;
      }
    }

    .decoration-line {
      height: 1px;
      background: linear-gradient(to right, transparent, #3498db, transparent);
      margin: 0 auto 25px;
      width: 80%;
    }

    /* 输入框 */
    .custom-input {
      height: 38px;
      border-radius: 19px;
      transition: all 0.3s ease;
      border: 1px solid #e2e8f0;
      background: rgba(255, 255, 255, 0.9);
      box-shadow: 0 1px 5px rgba(0, 0, 0, 0.1);
      width: 100%;

      :deep(.el-input__prefix) {
        font-size: 14px;
      }
      
      :deep(.el-input__inner) {
        font-size: 13px;
        padding-left: 0px;
      }
    }

    .login-btn {
      width: 100%;
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      border: none;
      height: 38px;
      font-size: 14px;
      font-weight: 500;
      transition: all 0.3s ease;
      border-radius: 21px;
      margin-top: 10px;
      position: relative;
      overflow: hidden;
      box-shadow: 0 4px 15px rgba(79, 172, 254, 0.2);

      &::before {
        content: "";
        position: absolute;
        top: 0;
        left: -100%;
        width: 100%;
        height: 100%;
        background: linear-gradient(
          90deg,
          transparent,
          rgba(255, 255, 255, 0.2),
          transparent
        );
        transition: 0.5s;
      }

      &:hover {
        background: linear-gradient(135deg, #00f2fe 0%, #4facfe 100%);
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(79, 172, 254, 0.3);

        &::before {
          left: 100%;
        }
      }

      &:active {
        transform: translateY(0);
        box-shadow: 0 2px 10px rgba(79, 172, 254, 0.2);
      }
    }

    /* 底部区域 */
    .footer {
      margin-top: auto;
      text-align: center;
      padding-top: 10px;
      
      .dotted-line {
        height: 1px;
        background: linear-gradient(to right, transparent, #ccc, transparent);
        margin-bottom: 12px;
      }
      
      .copyright {
        font-size: 12px;
        color: #666;
      }
    }
    
    .login-links {
      display: flex;
      justify-content: space-between;
      width: 100%;
      padding: 0 5px;
      margin-bottom: 5px;
      
      .link-item {
        color: #1a365d; 
        font-size: 12px;
        text-decoration: none;
        transition: color 0.2s;
        
        &:hover {
          color: #2980b9; 
          text-decoration: underline;
        }
      }
    }
  }
}

/* 适配Element Plus组件 */
:deep(.el-card) {
  border: none;
  background: transparent;
}

:deep(.el-form-item) {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

:deep(.el-form-item__content) {
  width: 100%;
  display: flex;
  justify-content: center;
}

:deep(.el-input__prefix) {
  display: flex;
  align-items: center;
  padding-left: 10px;
  color: #3498db !important;
}

:deep(.el-input__wrapper) {
  background: transparent;
  box-shadow: none;
  border: none !important;
}
</style>