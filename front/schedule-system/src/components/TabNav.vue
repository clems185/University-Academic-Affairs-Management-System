<template>
  <div class="tab-container">
    <el-tabs
      v-model="activeTab"
      type="card"
      closable
      @tab-remove="removeTab"
      @tab-click="tabClick"
      class="tab-nav"
    >
      <el-tab-pane
        v-for="tab in tabs"
        :key="tab.path"
        :label="tab.title"
        :name="tab.path"
        :closable="tab.path !== '/'"
      />
    </el-tabs>
    
    
  </div>
</template>

<script lang="ts" setup>
import { ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Refresh, Close, FolderDelete } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 固定首页标签为"工作台"
const tabs = ref([
  { title: '工作台', path: '/workbench' }  // 直接使用固定标题
])
const activeTab = ref(route.path)

// 监听路由变化，动态添加tab
watch(
  () => route.fullPath,
  (newPath) => {
    activeTab.value = newPath
    
    // 首页特殊处理：确保只添加一次
    if (newPath === '/workbench') return
    
    const existingTab = tabs.value.find(tab => tab.path === newPath)
    if (!existingTab) {
      tabs.value.push({
        title:String(route.meta.title || route.name || '未命名页面'),
        path: newPath
      })
    } else {
      // 更新现有标签的标题（如果路由元信息变化）
      existingTab.title = String(route.meta.title || route.name || '未命名页面')
    }
  },
  { immediate: true }
)

// 切换tab时跳转路由
function tabClick(tab: any) {
  if (tab.props.name !== route.path) {
    router.push(tab.props.name)
  }
}

// 关闭tab
function removeTab(targetName: string) {
  if (targetName === '/workbench') return // 首页不可关闭
  const idx = tabs.value.findIndex(tab => tab.path === targetName)
  if (idx > -1) {
    tabs.value.splice(idx, 1)
    // 如果关闭的是当前tab，自动切换到最后一个tab
    if (activeTab.value === targetName) {
      const lastTab = tabs.value[tabs.value.length - 1] || tabs.value[0]
      router.push(lastTab.path)
    }
  }
}

// 刷新当前标签页
function refreshTab() {
  router.replace({ path: '/redirect' + route.fullPath })
}

// 关闭其他标签页
function closeOtherTabs() {
  tabs.value = tabs.value.filter(tab => tab.path === '/' || tab.path === route.path)
}

// 关闭所有标签页
function closeAllTabs() {
  tabs.value = tabs.value.filter(tab => tab.path === '/')
  router.push('/')
}
</script>

<style scoped>
.tab-container {
  display: flex;
  align-items: center;
  background: #EFF2F5;
  border-radius: 6px 6px 0 0;
  padding: 0;
  margin: 0;
  position: relative;
}

.tab-nav {
  flex: 1;
  margin-right: 10px;
}

.tab-nav::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: #409EFF;
  z-index: 1;
}

:deep(.el-tabs__header) {
  margin: 0;
}

:deep(.el-tabs__nav) {
  display: flex;
  gap: 6px; /* 标签间微小间隔 */
  padding: 8px 8px 0;
}

:deep(.el-tabs__item) {
  border-radius: 4px 4px 0 0 !important;
  border-bottom: none !important;
  margin-right: 0 !important;
  background: white;
  color: #606266;
  transition: all 0.3s;
  padding: 0 20px !important;
  height: 36px;
  line-height: 36px;
  font-size: 13px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

/* 当前激活标签样式 */
:deep(.el-tabs__item.is-active) {
  background: #2a7ed2 !important;
  color: white !important;
  border-color: #2f80d2;
  font-weight: 500;
  box-shadow: 0 1px 3px rgba(42, 126, 210, 0.3);
}

/* 鼠标悬停效果 */
:deep(.el-tabs__item:not(.is-active):hover) {
  color: #409EFF;
  transform: translateY(-2px);
}

.tab-actions {
  display: flex;
  padding: 0 10px;
  gap: 8px;
}

.tab-actions .el-button {
  width: 28px;
  height: 28px;
  padding: 0;
}
</style>