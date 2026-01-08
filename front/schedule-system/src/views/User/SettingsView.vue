<template>
  <div class="page-container">
    <!-- 主内容板块 -->
    <div class="settings-main-container">
      <!-- 页面标题区域 -->
      <div class="settings-header">
        <h1 class="settings-title">{{ $t('settings.title') }}</h1>
        <p class="settings-description">{{ $t('settings.description') }}</p>
      </div>
      
      <!-- 设置内容区域 -->
      <div class="settings-content">
        <!-- 基础设置卡片 -->
        <el-card class="settings-card">
          <div slot="header" class="card-header">
            <el-icon class="card-icon"><setting /></el-icon>
            <span class="card-title">{{ $t('settings.basicSettings') }}</span>
          </div>
          
          <div class="setting-item">
            <div class="setting-label">
              <span>{{ $t('settings.language') }}</span>
              <p class="setting-hint">{{ $t('settings.languageHint') }}</p>
            </div>
            <div class="setting-control">
              <el-select v-model="selectedLanguage" @change="handleLanguageChange">
                <el-option label="简体中文" value="zh-CN" />
                <el-option label="English" value="en-US" />
              </el-select>
            </div>
          </div>
          
          <div class="setting-item">
            <div class="setting-label">
              <span>{{ $t('settings.notifications') }}</span>
              <p class="setting-hint">{{ $t('settings.notificationsHint') }}</p>
            </div>
            <div class="setting-control">
              <el-switch v-model="notificationEnabled" 
                :active-text="$t('settings.enabled')" 
                :inactive-text="$t('settings.disabled')" />
            </div>
          </div>
          
          <div class="setting-item">
            <div class="setting-label">
              <span>{{ $t('settings.dataBackup') }}</span>
              <p class="setting-hint">{{ $t('settings.dataBackupHint') }}</p>
            </div>
            <div class="setting-control">
              <el-switch v-model="autoBackup" 
                :active-text="$t('settings.enabled')" 
                :inactive-text="$t('settings.disabled')" />
              <el-select v-model="backupFrequency" class="frequency-select" :disabled="!autoBackup">
                <el-option :label="$t('settings.daily')" value="daily" />
                <el-option :label="$t('settings.weekly')" value="weekly" />
                <el-option :label="$t('settings.monthly')" value="monthly" />
              </el-select>
            </div>
          </div>
        </el-card>
        
        <!-- 外观设置卡片 -->
        <el-card class="settings-card">
          <div slot="header" class="card-header">
            <el-icon class="card-icon"><brush /></el-icon>
            <span class="card-title">{{ $t('settings.appearance') }}</span>
          </div>
          
          <div class="setting-item">
            <div class="setting-label">
              <span>{{ $t('settings.darkMode') }}</span>
              <p class="setting-hint">{{ $t('settings.darkModeHint') }}</p>
            </div>
            <div class="setting-control">
              <el-switch v-model="darkMode" 
                :active-text="$t('settings.dark')" 
                :inactive-text="$t('settings.light')" 
                @change="toggleDarkMode" />
            </div>
          </div>
        </el-card>
        
        <!-- 操作按钮区域 -->
        <div class="settings-actions">
          <el-button type="primary" size="large" @click="saveSettings">
            <el-icon><check /></el-icon>{{ $t('settings.save') }}
          </el-button>
          <el-button size="large" @click="resetSettings" class="reset-button">
            <el-icon><refresh-left /></el-icon>{{ $t('settings.reset') }}
          </el-button>
          <el-button type="text" @click="cancelSettings">
            {{ $t('settings.cancel') }}
          </el-button>
        </div>
        
        <!-- 底部额外空间，确保可以滚动到maincontainer下方50px -->
        <div class="bottom-extra-space"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { 
  Setting, Brush, Check, RefreshLeft
} from '@element-plus/icons-vue'

const router = useRouter()

// 语言包定义
const messages = reactive({
  'zh-CN': {
    settings: {
      title: '系统设置',
      description: '自定义您的系统体验，调整界面外观和功能偏好',
      basicSettings: '基础设置',
      language: '系统语言',
      languageHint: '选择您偏好的系统显示语言',
      notifications: '通知设置',
      notificationsHint: '控制系统通知的接收方式',
      dataBackup: '数据备份',
      dataBackupHint: '自动备份您的个人数据',
      appearance: '外观设置',
      darkMode: '主题模式',
      darkModeHint: '切换明暗主题显示',
      save: '保存设置',
      reset: '重置为默认',
      cancel: '取消',
      enabled: '开启',
      disabled: '关闭',
      daily: '每天',
      weekly: '每周',
      monthly: '每月',
      dark: '深色',
      light: '浅色',
      changed: '已更改',
      success: '成功'
    }
  },
  'en-US': {
    settings: {
      title: 'System Settings',
      description: 'Customize your system experience, adjust interface appearance and preferences',
      basicSettings: 'Basic Settings',
      language: 'System Language',
      languageHint: 'Select your preferred display language',
      notifications: 'Notification Settings',
      notificationsHint: 'Control how you receive system notifications',
      dataBackup: 'Data Backup',
      dataBackupHint: 'Automatically back up your personal data',
      appearance: 'Appearance Settings',
      darkMode: 'Theme Mode',
      darkModeHint: 'Switch between light and dark themes',
      save: 'Save Settings',
      reset: 'Reset to Default',
      cancel: 'Cancel',
      enabled: 'Enabled',
      disabled: 'Disabled',
      daily: 'Daily',
      weekly: 'Weekly',
      monthly: 'Monthly',
      dark: 'Dark',
      light: 'Light',
      changed: 'changed',
      success: 'success'
    }
  }
})

// 翻译函数
const $t = (key: string) => {
  const keys = key.split('.')
  let value: any = messages[selectedLanguage.value]
  
  for (const k of keys) {
    if (value && value[k] !== undefined) {
      value = value[k]
    } else {
      return key
    }
  }
  
  return value
}

// 状态管理
const selectedLanguage = ref('zh-CN')
const notificationEnabled = ref(true)
const autoBackup = ref(false)
const backupFrequency = ref('weekly')
const darkMode = ref(false)
const originalSettings = ref({})

// 初始化
onMounted(() => {
  const savedSettings = localStorage.getItem('systemSettings')
  if (savedSettings) {
    const parsed = JSON.parse(savedSettings)
    selectedLanguage.value = parsed.language || 'zh-CN'
    notificationEnabled.value = parsed.notifications !== undefined ? parsed.notifications : true
    autoBackup.value = parsed.autoBackup || false
    backupFrequency.value = parsed.backupFrequency || 'weekly'
    darkMode.value = parsed.darkMode || false
    applyDarkMode()
  }
  saveOriginalSettings()
})

// 保存原始设置
const saveOriginalSettings = () => {
  originalSettings.value = {
    language: selectedLanguage.value,
    notifications: notificationEnabled.value,
    autoBackup: autoBackup.value,
    backupFrequency: backupFrequency.value,
    darkMode: darkMode.value
  }
}

// 语言变更
const handleLanguageChange = (lang: string) => {
  selectedLanguage.value = lang
  showToast($t('settings.language') + $t('settings.changed'))
}

// 深色模式切换
const toggleDarkMode = () => {
  applyDarkMode()
  showToast($t('settings.darkMode') + (darkMode.value ? $t('settings.dark') : $t('settings.light')))
}

// 应用深色模式
const applyDarkMode = () => {
  const mainContainer = document.querySelector('.settings-main-container')
  
  if (darkMode.value) {
    document.documentElement.classList.add('dark-mode')
    if (mainContainer) {
      mainContainer.style.backgroundColor = '#1d2129'
    }
  } else {
    document.documentElement.classList.remove('dark-mode')
    if (mainContainer) {
      mainContainer.style.backgroundColor = '#ffffff'
    }
  }
}

// 设置操作
const saveSettings = () => {
  const settings = {
    language: selectedLanguage.value,
    notifications: notificationEnabled.value,
    autoBackup: autoBackup.value,
    backupFrequency: backupFrequency.value,
    darkMode: darkMode.value
  }
  localStorage.setItem('systemSettings', JSON.stringify(settings))
  showToast($t('settings.save') + $t('settings.success'))
}

const resetSettings = () => {
//   selectedLanguage.value = originalSettings.value.language
//   notificationEnabled.value = originalSettings.value.notifications
//   autoBackup.value = originalSettings.value.autoBackup
//   backupFrequency.value = originalSettings.value.backupFrequency
//   darkMode.value = originalSettings.value.darkMode
  applyDarkMode()
  showToast($t('settings.reset') + $t('settings.success'))
}

const cancelSettings = () => {
  router.back()
}

// 提示消息
const showToast = (message: string) => {
  const toast = document.createElement('div')
  toast.className = 'custom-toast'
  toast.textContent = message
  if (darkMode.value) {
    toast.style.color = '#ffffff'
  }
  document.body.appendChild(toast)
  
  setTimeout(() => toast.classList.add('show'), 10)
  setTimeout(() => {
    toast.classList.remove('show')
    setTimeout(() => document.body.removeChild(toast), 300)
  }, 2000)
}

// 监听深色模式变化
watch(darkMode, () => {
  applyDarkMode()
})
</script>

<style scoped>
/* 页面容器 - 确保可以滚动 */
.page-container {
  padding: 30px;
  background-color: #f0f2f5;
  min-height: 100vh;
  overflow-y: auto; /* 允许垂直滚动 */
  box-sizing: border-box;
  /* 确保滚动容器没有高度限制 */
  height: 1750px;
  max-height: 125vh;
}

/* 主内容容器 - 底部预留空间 */
.settings-main-container {
  background-color: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  padding: 30px;
  padding-bottom: 80px; /* 增加底部内边距，为额外空间做准备 */
  max-width: 1200px;
  margin: 0 auto;
  transition: all 0.3s ease;
  color: #303133;
  width: 100%;
  box-sizing: border-box;
  /* 移除任何可能限制高度的设置 */
  height: auto !important;
  min-height: auto !important;
}

/* 底部额外空间 - 确保可以滚动到maincontainer下方50px */
.bottom-extra-space {
  height: -20px; /* 精确的50px额外空间 */
  width: 100%;
}

/* 头部样式 */
.settings-header {
  margin-bottom: 32px;
  text-align: center;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.settings-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 8px;
}

.settings-description {
  font-size: 16px;
  color: #606266;
}

/* 内容区域 */
.settings-content {
  display: grid;
  grid-template-columns: 1fr;
  gap: 24px;
}

/* 卡片样式 */
.settings-card {
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  border: none;
  background-color: #fff;
}

.settings-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
  transition: all 0.3s ease;
}

/* 卡片头部 */
.card-header {
  display: flex;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #ebeef5;
}

.card-icon {
  margin-right: 12px;
  color: #409EFF;
  font-size: 20px;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
}

/* 设置项 */
.setting-item {
  display: flex;
  align-items: center;
  padding: 18px 24px;
  border-bottom: 1px solid #ebeef5;
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-item:hover {
  background-color: #f9fafb;
}

.setting-label {
  flex: 1;
}

.setting-label span {
  display: block;
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 4px;
}

.setting-hint {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.setting-control {
  flex: 1;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

/* 按钮区域 */
.settings-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 16px;
  padding: 16px;
  /* 确保按钮在视口中可见 */
  position: relative;
  z-index: 10;
}

/* 频率选择器 */
.frequency-select {
  margin-left: 12px;
  width: 120px;
}

/* 深色模式样式 */
.dark-mode {
  --text-color: #ffffff;
}

.dark-mode .settings-title,
.dark-mode .settings-description,
.dark-mode .card-title,
.dark-mode .setting-label span,
.dark-mode .setting-hint,
.dark-mode .el-switch,
.dark-mode .el-select {
  color: #ffffff !important;
}

.dark-mode .settings-card {
  background-color: #282c34;
  border-color: #4e5969;
}

.dark-mode .card-header {
  border-bottom-color: #4e5969;
}

.dark-mode .setting-item {
  border-bottom-color: #4e5969;
}

.dark-mode .setting-item:hover {
  background-color: #383e4a;
}

/* 深色模式按钮样式 */
.dark-mode .el-button {
  color: #ffffff !important;
  background-color: #383e4a !important;
  border-color: #4e5969 !important;
}

.dark-mode .el-button--primary {
  background-color: #409EFF !important;
  border-color: #409EFF !important;
  color: #ffffff !important;
}

.dark-mode .el-button--text {
  background-color: transparent !important;
  border-color: transparent !important;
  color: #409EFF !important;
}

/* 下拉菜单样式 */
.dark-mode .el-select-dropdown {
  background-color: #282c34;
  border-color: #4e5969;
}

.dark-mode .el-select-dropdown__item {
  color: #ffffff;
}

.dark-mode .el-select-dropdown__item:hover {
  background-color: #383e4a;
}

.dark-mode .el-checkbox__label {
  color: #ffffff;
}

/* 提示框样式 */
.custom-toast {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 12px 20px;
  background-color: #409EFF;
  color: white;
  border-radius: 4px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateX(120%);
  opacity: 0;
  transition: all 0.3s ease;
  z-index: 9999;
}

.custom-toast.show {
  transform: translateX(0);
  opacity: 1;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .page-container {
    padding: 15px;
  }
  
  .settings-main-container {
    padding: 15px;
    padding-bottom: 60px;
  }
  
  .setting-item {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .setting-control {
    width: 100%;
    justify-content: flex-start;
    margin-top: 12px;
  }
  
  .settings-actions {
    flex-direction: column;
  }
  
  .settings-actions button {
    width: 100%;
  }
  
  .bottom-extra-space {
    height: 30px; /* 移动端适当调整 */
  }
}
</style>
