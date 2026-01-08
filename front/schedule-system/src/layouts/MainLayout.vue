<template>
  <div>
    <LoginView v-if="!isLogin" @login-success="setLogin" />
    <div v-else>
      <el-container style="height: 100vh; overflow: hidden;">  
        <el-header style="padding:0; height: 64px;">
          <HeaderBar :collapse="isCollapse" @toggle-collapse="toggleCollapse" />
        </el-header>
        
        <el-container style="position: relative; flex: 1; overflow: hidden;">  
          <!-- 修改侧边栏为固定定位和transform动画 -->
          <el-aside 
            :style="{
              transform: isCollapse ? 'translateX(-100%)' : 'translateX(0)',
              width: '200px'
            }"
            class="animated-sidebar">
            <SideBar />
          </el-aside>

          <div 
            v-show="!isCollapse"
            class="sidebar-overlay"
            @click="isCollapse = true">
          </div>

          <el-container 
            direction="vertical"
            class="main-content-container">
            <el-header height="40px" style="padding:0; background: white; border-bottom: 1px solid #ebeef5;">
              <TabNav />
            </el-header>
            <el-main style="padding: 16px; background: #f5f6fa; height: calc(100vh - 104px);"> 
              <router-view></router-view>
            </el-main>
          </el-container>
        </el-container>
      </el-container>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'  
import SideBar from '@/components/SideBar.vue'
import HeaderBar from '@/components/HeaderBar.vue'
import TabNav from '@/components/TabNav.vue'
import LoginView from '@/views/Login/LoginView.vue'

export default defineComponent({
  name: 'MainLayout',
  components: {
    SideBar,
    HeaderBar,
    TabNav,
    LoginView
  },
  setup() {
    const route = useRoute()
    const isLogin = ref(false)
    const isCollapse = ref(true)

    // 检查登录状态
    const checkLoginStatus = () => {
      const token = sessionStorage.getItem('token')
      isLogin.value = !!token
    }

    onMounted(() => {
      checkLoginStatus()
    })

    // 监听路由变化，非工作台路由自动关闭菜单
    watch(() => route.path, (newPath) => {
      if (newPath !== '/') {
        isCollapse.value = true
      }
    })

    const setLogin = () => {
      isLogin.value = true
    }
    
    const toggleCollapse = () => {
      isCollapse.value = !isCollapse.value
    }
    
    return {  
      isLogin, 
      setLogin,
      isCollapse,
      toggleCollapse
    }
  }
})
</script>

<style scoped>
/* 侧边栏动画 */
.animated-sidebar {
  position: fixed;
  top: 64px;
  left: 0;
  bottom: 0;
  z-index: 1001; /* 高于遮罩层 */
  transition: transform 0.3s ease;
  overflow-y: auto;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.15);
}

/* 主内容区域 - 始终100%宽度 */
.main-content-container {
  width: 100%;
  height: 100%;
  transition: none; /* 移除过渡 */
}

/* 遮罩层样式 */
.sidebar-overlay {
  position: fixed;
  top: 64px;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  z-index: 1000; /* 低于侧边栏 */
  backdrop-filter: blur(2px);
  transition: opacity 0.3s ease;
}

/* 响应式调整：大屏幕时隐藏遮罩层 */
@media (min-width: 992px) {
  .sidebar-overlay {
    display: none;
  }
  
  /* 桌面端菜单展开时右侧内容稍微右移 */
  .animated-sidebar:not([style*="translateX(-100%)"]) + .main-content-container {
    margin-left: 200px;
    width: calc(100% - 200px);
  }
}

/* 移动端优化 */
@media (max-width: 991px) {
  .animated-sidebar {
    width: 70%; /* 移动端菜单宽度 */
  }
}
</style>