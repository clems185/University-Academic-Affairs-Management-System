<template>
  <div class="information-container">
    <h1 class="title">XX大学统一身份认证使用手册</h1>
    
    <div class="section">
      <h2>【激活账号/重置密码】</h2>
      <p>在登录页，点击"激活/忘记密码"来激活个人账号或重新设置个人密码。</p>
      <div class="image-container">
        <el-image 
          :src="require('@/assets/Information/login.png')" 
          alt="登录页面示意图"
          class="manual-image"
          :preview-src-list="[require('@/assets/Information/login.png')]"
        ></el-image>
      </div>
    </div>
    
    <div class="section">
      <h2>激活/忘记密码操作步骤</h2>
      <div class="steps">
        <div class="step">
          <div class="step-header">第一步：确认账号</div>
          <p>输入工号/学号进行账号确认</p>
          <div class="image-container">
            <el-image 
              :src="require('@/assets/Information/forget.png')" 
              alt="账号确认页面"
              class="manual-image"
              :preview-src-list="[require('@/assets/Information/forget.png')]"
            ></el-image>
          </div>
        </div>
        
        <div class="step">
          <div class="step-header">第二步：安全验证</div>
          <p>可通过选择手机或邮箱获取验证码方式来完成验证。</p>
          <p class="note">
            如需更新手机或邮箱：<br>
            - 教职工请登录"人事系统"进行更新<br>
            - 学生请登录"学工系统"进行更新<br>
            认证平台将自动同步更新后的信息
          </p>
        </div>
        
        <div class="step">
          <div class="step-header">第三步：设置新密码</div>
          <p>按照密码规则要求设置新密码：</p>
          <ul class="password-rules">
            <li>长度8-20位字符</li>
            <li>包含至少一个大写字母</li>
            <li>包含至少一个小写字母</li>
            <li>包含至少一个数字</li>
            <li>包含至少一个特殊字符（如!@#$%^&*）</li>
          </ul>
        </div>
      </div>
    </div>
    
    <div class="footer">
      <p class="support">如有任何问题，请联系项目负责人：</p>
      <p>电话：13039257216 | 邮箱：2353119@tongji.edu.cn</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

// 响应式数据示例（可根据需要扩展）
const activeStep = ref(1);
</script>

<style scoped lang="scss">
.information-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 30px 20px;
  font-family: "Microsoft YaHei", sans-serif;
  color: #333;
  background-color: #fff;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  
  .title {
    text-align: center;
    font-size: 28px;
    color: #1a365d;
    margin-bottom: 40px;
    padding-bottom: 20px;
    border-bottom: 2px solid #3498db;
  }
  
  .section {
    margin-bottom: 40px;
    
    h2 {
      color: #2980b9;
      font-size: 22px;
      margin-bottom: 15px;
      padding-left: 10px;
      border-left: 4px solid #3498db;
    }
    
    p {
      line-height: 1.8;
      margin-bottom: 20px;
      font-size: 16px;
    }
    
    .note {
      background-color: #f8f9fa;
      border-left: 4px solid #3498db;
      padding: 15px;
      border-radius: 0 4px 4px 0;
    }
  }
  
  .steps {
    display: flex;
    flex-wrap: wrap;
    gap: 30px;
    margin-top: 25px;
    
    .step {
      flex: 1;
      min-width: 280px;
      padding: 20px;
      border-radius: 8px;
      background: #f8fafc;
      box-shadow: 0 3px 10px rgba(0, 0, 0, 0.05);
      transition: transform 0.3s ease;
      
      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
      }
      
      .step-header {
        font-size: 18px;
        font-weight: bold;
        color: #1a365d;
        margin-bottom: 15px;
        padding-bottom: 10px;
        border-bottom: 1px dashed #3498db;
      }
      
      .password-rules {
        padding-left: 20px;
        margin-top: 10px;
        
        li {
          margin-bottom: 8px;
          line-height: 1.5;
        }
      }
    }
  }
  
  .image-container {
    margin: 20px 0;
    text-align: center;
    
    .manual-image {
      max-width: 100%;
      border-radius: 8px;
      border: 1px solid #eee;
      box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
      cursor: pointer;
      transition: all 0.3s ease;
      
      &:hover {
        transform: scale(1.02);
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
      }
    }
  }
  
  .footer {
    margin-top: 40px;
    padding-top: 20px;
    border-top: 1px solid #eee;
    text-align: center;
    color: #666;
    font-size: 14px;
    
    .support {
      font-weight: bold;
      margin-bottom: 10px;
      color: #1a365d;
    }
  }
}

@media (max-width: 768px) {
  .information-container {
    padding: 20px 15px;
    
    .title {
      font-size: 24px;
    }
    
    .section h2 {
      font-size: 20px;
    }
    
    .steps {
      flex-direction: column;
      gap: 20px;
    }
  }
}
</style>