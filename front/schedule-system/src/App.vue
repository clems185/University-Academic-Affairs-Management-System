<template>
  <router-view />
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'App'
})
</script>

<style>
body, #app {
  margin: 0;
  padding: 0;
  height: 100%;
}
.el-header {
  background: #409EFF;
  padding: 0 !important;
  box-sizing: border-box;
  border-bottom: none;
}
.el-aside {
  background: #545c64;
  min-height: 100vh;
  border-right: none;
}
.el-main {
  background: #f5f6fa;
  min-height: 100vh;
  padding: 0;
}
.el-container {
  /* 防止嵌套容器有间隙 */
  gap: 0;
}
</style>