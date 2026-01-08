<template>
  <el-menu
    :default-active="activeMenu"
    class="el-menu-vertical-demo"
    background-color="#545c64"
    text-color="#fff"
    active-text-color="#ffd04b"
    router
    unique-opened
  >
    <el-sub-menu index="message" >
      <template #title>
        <span>消息中心</span>
      </template>
      <el-menu-item index="mymessage">我的消息</el-menu-item>
      <el-menu-item index="handle" v-if="userType === 0||userType === 1" >消息处理</el-menu-item>
      <el-menu-item index="messageapply" v-if="userType === 1||userType === 2">消息申请</el-menu-item>
    </el-sub-menu>
    <el-sub-menu index="user" v-if="userType === 0||userType === 1">
      <template #title>
        <span>用户中心</span>
      </template>
      <el-menu-item index ="studentinfo" v-if="userType === 1">学生信息</el-menu-item>
      <el-menu-item index="teacherinfo" v-if="userType === 1">教师信息</el-menu-item>
      <el-menu-item index="allinfo" v-if="userType === 0">用户信息</el-menu-item>
    </el-sub-menu>
    <el-sub-menu index="apply" v-if="userType === 1">
      <template #title>
        <span>申请处理</span>
      </template>
      <el-menu-item index="competitionhandle" >竞赛处理</el-menu-item>
      <el-menu-item index="imagehandle">头像审核</el-menu-item>
      <el-menu-item index="deferrredhandle">缓考处理</el-menu-item>
      <el-menu-item index="advisorhandle">导师处理</el-menu-item>
      <el-menu-item index="teachinghandle">授课处理</el-menu-item>
    </el-sub-menu>
    <el-sub-menu index="exam">
      <template #title>
        <span>考试信息</span>
      </template>
      <el-menu-item index="schedule">考试安排</el-menu-item>
      <el-menu-item index="paper" v-if="userType === 0||userType === 1">试卷中心</el-menu-item>
      <el-menu-item index="myexam" v-if="userType === 3">我的考试</el-menu-item>
      <el-menu-item index="deffered" v-if="userType === 3">申请缓考</el-menu-item>
      <el-menu-item index="myinvigilate" v-if="userType === 2">我的监考</el-menu-item>
      <el-menu-item index="organize" v-if="userType === 2">组织考试</el-menu-item>
    </el-sub-menu>
    <el-sub-menu index="competition">
      <template #title>
        <span>竞赛信息</span>
      </template>
      <el-menu-item index="competitionquery">竞赛查询</el-menu-item>
      <el-menu-item index="competitionapply" v-if="userType === 2">竞赛申请</el-menu-item>
    </el-sub-menu>
    <el-sub-menu index="course">
      <template #title>
        <span>课程信息</span>
      </template>
      <el-menu-item index="weeklycourse" v-if="userType === 2||userType === 3||userType === 1||userType === 0">每周课表</el-menu-item>
      <el-menu-item index="myteaching" v-if="userType === 2">我的授课</el-menu-item>
      <el-menu-item index="studentselectcourse" v-if="userType === 3">学生选课</el-menu-item>
      <el-menu-item index="qrcode" v-if="userType === 2">扫码签到</el-menu-item>
      <el-menu-item index="signlist" v-if="userType === 2">签到列表</el-menu-item>
      <el-menu-item index="teachingapply" v-if="userType === 2">授课申请</el-menu-item>
      <el-menu-item index="grade" v-if="userType === 2">成绩登记</el-menu-item>
      <el-menu-item index="adminpublishselection" v-if="userType === 0">发布选课</el-menu-item>
      <el-menu-item index="schedulecourse" v-if="userType === 0">一键排课</el-menu-item>
    </el-sub-menu>
    <el-sub-menu index="advisor">
      <template #title>
        <span>导师信息</span>
      </template>
      <el-menu-item index="advisorinfo" v-if="userType === 2">信息编辑</el-menu-item>
      <el-menu-item index="advisorapply" v-if="userType === 2">导师申请</el-menu-item>
      <el-menu-item index="advisorquery" >导师查询</el-menu-item>
      <el-menu-item index="advisorselect" v-if="userType === 3">选择导师</el-menu-item>
    </el-sub-menu>
    <el-menu-item index="gradequery" v-if="userType === 3">成绩查询</el-menu-item>
    <el-menu-item index="classroom" >教室使用情况</el-menu-item>
  </el-menu>
</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router'
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router' // 新增导入

const route = useRoute()
const activeMenu = computed(() => route.path)
const router = useRouter() // 新增路由实例


// 处理菜单点击
const handleMenuClick = (path: string) => {
  router.push(`/workbench${path}`)
}

// 获取用户类型
const userType = ref(0);
try {
  const storedType = sessionStorage.getItem('userType');
  if (storedType) {
    userType.value = parseInt(storedType);
  }
} catch (e) {
  console.error('Failed to parse userType', e);
}
</script>

<style scoped>
.el-menu-vertical-demo {
  width: 200px;
  min-height: 100vh;
}
</style>