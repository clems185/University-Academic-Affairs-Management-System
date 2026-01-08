<template>
  <div class="forget-container">
    <div class="forget-background"></div>
    <div class="forget-gradient"></div>
    
    <el-card class="forget-card">
      <h2 class="forget-title">密码重置</h2>
      <div class="decoration-line"></div>
      
      <el-form 
        :model="resetForm" 
        :rules="rules" 
        ref="resetFormRef"
        @submit.prevent="onReset"
      >
        <!-- 用户名 -->
        <el-form-item prop="username">
          <el-input 
            v-model="resetForm.username" 
            placeholder="请输入学号/工号"
            class="custom-input"
          >
            <template #prefix>
              <i class="icon-user el-icon-user"></i>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item prop="email">
          <el-input 
            v-model="resetForm.email" 
            placeholder="请输入注册邮箱"
            class="custom-input"
          >
            <template #prefix>
              <i class="icon-email el-icon-message"></i>
            </template>
          </el-input>
        </el-form-item>
        
        <!-- 新密码 -->
        <el-form-item prop="password">
          <el-input 
            v-model="resetForm.password" 
            type="password"
            placeholder="请输入新密码"
            show-password
            class="custom-input"
          >
            <template #prefix>
              <i class="icon-lock el-icon-lock"></i>
            </template>
          </el-input>
        </el-form-item>
        
        <!-- 确认密码 -->
        <el-form-item prop="confirmPassword">
          <el-input 
            v-model="resetForm.confirmPassword" 
            type="password"
            placeholder="请确认新密码"
            show-password
            class="custom-input"
          >
            <template #prefix>
              <i class="icon-lock el-icon-lock"></i>
            </template>
          </el-input>
        </el-form-item>
        
        <!-- 验证码 -->
        <el-form-item prop="captcha">
          <div class="captcha-container">
            <el-input 
              v-model="resetForm.captcha" 
              placeholder="请输入验证码"
              class="captcha-input"
            >
              <template #prefix>
                <i class="icon-key el-icon-key"></i>
              </template>
            </el-input>
            <el-button 
              type="primary" 
              class="captcha-btn"
              :disabled="countdown > 0"
              @click="sendCaptcha"
            >
              {{ countdown > 0 ? `${countdown}秒后重试` : '获取验证码' }}
            </el-button>
          </div>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            native-type="submit"
            class="reset-btn"
            :loading="loading"
          >
            重置密码
          </el-button>
        </el-form-item>
      </el-form>
      <div class="back-login">
        <el-link type="primary" @click="goBackToLogin">
          <i class="el-icon-arrow-left"></i> 返回登录
        </el-link>
      </div>
      <div class="footer">
        <div class="dotted-line"></div>
        <div class="copyright">© 同济大学2025年数据库课设-教务管理系统</div>
      </div>
      
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { verification, forgotPassword } from '@/api/user/user'  // 导入API函数
import type { forgetPasswordParams, verificationParams } from '@/api/user/types'  // 导入类型定义

const router = useRouter()
const loading = ref(false)  // 重置按钮加载状态

// 表单数据
const resetForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  captcha: ''
})

// 验证规则
const validatePassword = (rule: any, value: string, callback: any) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else if (value.length < 6) {
    callback(new Error('密码长度不能少于6位'))
  } else {
    callback()
  }
}

const validateConfirmPassword = (rule: any, value: string, callback: any) => {
  if (value === '') {
    callback(new Error('请确认密码'))
  } else if (value !== resetForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: ['blur', 'change'] }
  ],
  password: [
    { validator: validatePassword, trigger: 'blur' }
  ],
  confirmPassword: [
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { min: 6, max: 6, message: '验证码长度为6位', trigger: 'blur' }
  ]
})

// 验证码倒计时
const countdown = ref(0)
let countdownTimer: number | null = null

const sendCaptcha = async () => {
  // 验证用户名和邮箱是否填写
  if (!resetForm.username) {
    ElMessage.error('请输入用户名')
    return
  }
  
  if (!resetForm.email) {
    ElMessage.error('请输入邮箱')
    return
  }
  
  try {
    // 调用API发送验证码
    const params: verificationParams = {
      userId: resetForm.username,
      email: resetForm.email
    }
    
    const response = await verification(params)
    console.log("response",response)
    if (response.status === 200) {
      ElMessage.success('验证码已发送到您的邮箱')
      
      // 设置倒计时
      countdown.value = 60
      countdownTimer = setInterval(() => {
        countdown.value--
        if (countdown.value <= 0 && countdownTimer) {
          clearInterval(countdownTimer)
          countdownTimer = null
        }
      }, 1000)
    } else {
      ElMessage.error(response.data?.Message || '验证码发送失败')
    }
  } catch (error: any) {
    console.error('验证码发送失败:', error)
    ElMessage.error(error.response?.data?.Message || '验证码发送失败，请稍后重试')
  }
}

// 重置密码
const resetFormRef = ref()
const onReset = async () => {
  try {
    const valid = await (resetFormRef.value as any).validate()
    if (!valid) return
    
    loading.value = true
    
    // 调用API重置密码
    const params: forgetPasswordParams = {
      Id: resetForm.username,
      VerificationCode: resetForm.captcha,
      NewPassword: resetForm.password
    }
    
    const response = await forgotPassword(params)
    
    if (response.status === 200) {
      ElMessage.success('密码重置成功，请使用新密码登录')
      setTimeout(() => {
        router.push('/')
      }, 1500)
    } else {
      ElMessage.error(response.data?.Message || '密码重置失败')
    }
  } catch (error: any) {
    console.error('密码重置失败:', error)
    ElMessage.error(error.response?.data?.Message || '密码重置失败，请重试')
  } finally {
    loading.value = false
  }
}

// 返回登录
const goBackToLogin = () => {
  router.push('/')
}
</script>
<style scoped lang="scss">
.forget-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  position: relative;
  padding-right: 5%;
  overflow: hidden;
  
  .forget-background {
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

  .forget-gradient {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 2;
    backdrop-filter: blur(0);
  }

  .forget-card {
    position: relative;
    z-index: 3;
    width: 360px;
    padding: 5px;
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
    
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 15px 50px rgba(0, 0, 0, 0.2);
    }

    .forget-title {
      text-align: center;
      font-size: 22px;
      font-weight: 600;
      color: #1a365d;
      letter-spacing: 5px;
      margin-bottom: 25px;
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
    
    .captcha-container {
      display: flex;
      width: 100%;
      
      .captcha-input {
        flex: 1;
        margin-right: 10px;
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
      
      
      .captcha-btn {
        width: 140px;
        border-radius: 19px;
      }
    }

    .reset-btn {
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

    .footer {
      margin-top: 20px;
      text-align: center;
      
      .dotted-line {
        height: 1px;
        background: linear-gradient(to right, transparent, #ccc, transparent);
        margin-bottom: 12px;
        margin-top: 2px;
      }
      
      .copyright {
        font-size: 12px;
        color: #666;
      }
    }
    
    .back-login {
      margin-top: 5px;
      text-align: center;
      .el-link {
        font-size: 12px;
        color:#1a365d;
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