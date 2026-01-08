<template>
  <el-header height="auto" class="header-bar">
    <div class="top-row">
      <div class="left-section">
        <el-icon class="collapse-icon" @click="toggleCollapse">
          <Expand v-if="props.collapse" />
          <Fold v-else />
        </el-icon>
        <div class="logo">教学管理信息系统</div>
      </div>
      <div class="user-info">
        <el-dropdown @command="handleCommand">
          <div class="avatar-container">
            <el-avatar 
              :size="40" 
              :icon="UserFilled"
              class="user-avatar" />
            <span class="username">{{ userInfo.name || '用户' }}</span>
            <el-icon class="el-icon--right"><arrow-down /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人信息</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </el-header>
</template>

<script setup lang="ts">
import { ref, onMounted, defineProps, defineEmits } from 'vue'
import { ElMessage } from 'element-plus'
import { UserFilled, ArrowDown, Expand, Fold } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  collapse: {
    type: Boolean,
    required: true
  }
})

const emit = defineEmits(['toggle-collapse'])
const router = useRouter()

// 默认头像
const defaultAvatar = '/src/assets/logo.png'

const userInfo = ref({
  id: sessionStorage.getItem('userId'),
  name: sessionStorage.getItem('name'),
  userType: sessionStorage.getItem('userType')
})

// 处理下拉菜单命令
const handleCommand = (command: string) => {
  if (command === 'logout') {
    sessionStorage.removeItem('token')
    window.location.reload()
  }
  else if(command === 'profile'){
    router.push('/profile')
  }
}

const toggleCollapse = () => {
  emit('toggle-collapse')
}
</script>

<style scoped>
.header-bar {
  display: flex;
  flex-direction: column;
  background: #FFF;
  color: #35a8e1;
  padding: 0 20px;
  height: 64px !important;
}

.top-row {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.left-section {
  display: flex;
  align-items: center;
  margin: 12px,24px,0,0;
  padding-left: 16px;
  gap: 24px;
}

.collapse-icon {
  font-size: 24px;
  cursor: pointer;
  transition: all 0.3s;
}

.collapse-icon:hover {
  color: #e6f7ff;
}

.logo {
  font-size: 23px;
  font-weight: bold;
  font-family: "Microsoft YaHei", sans-serif;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar-container {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 20px;
  transition: background-color 0.3s;
}

.avatar-container:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.user-avatar {
  margin-right: 8px;
}

.username {
  margin-right: 5px;
  font-size: 14px;
  font-family: "Microsoft YaHei", sans-serif;
}
</style>