<template>
  <div class="breadcrumb-bar">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item to="/">首页</el-breadcrumb-item>
      <el-breadcrumb-item v-for="(item, index) in breadList" :key="index">
        {{ item.meta.title || item.name }}
      </el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router'
import { computed } from 'vue'

const route = useRoute()
const breadList = computed(() => route.matched.filter(r => r.meta && r.meta.title && r.path !== '/'))
</script>

<style scoped>
.breadcrumb-bar {
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  padding: 6px 18px;
  display: inline-block;
  margin: 8px 0;
}
.el-breadcrumb__item {
  font-size: 16px;
  color: #333;
}
.el-breadcrumb__item a {
  color: #409EFF;
  text-decoration: none;
  transition: color 0.2s;
}
.el-breadcrumb__item a:hover {
  color: #66b1ff;
  text-decoration: underline;
}
.el-breadcrumb__separator {
  color: #bbb;
}
</style> 