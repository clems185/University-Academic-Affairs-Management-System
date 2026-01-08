<template>
  <div style="display: flex; flex-direction: column; align-items: center; margin-top: 80px;">
    <h2>学生签到</h2>
    <div v-if="loading" style="margin: 32px 0;">正在签到，请稍候...</div>
    <div v-else>
      <el-result v-if="signInResult === 'success'" icon="success" title="签到成功"
        :sub-title="`欢迎上课，签到时间：${signInTimeDisplay}`" />
      <el-result v-else icon="error" title="签到失败" :sub-title="failReason" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, warn } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { studentSignIn } from '@/api/qrcode/qrcode'
import type { Sign } from '@/api/qrcode/types.ts'
const route = useRoute()
const loading = ref(true)
const signInResult = ref<'success' | 'fail'>('fail')
const failReason = ref('')
const signInTimeDisplay = ref('')

// 增强二维码验证逻辑
function isCodeValid(ts: number, interval: number) {
  // 计算最大有效期 = 教师设置的间隔(秒) * 1000 + 5秒缓冲时间
  const maxAge = interval * 1000 + 5000
  return Date.now() - ts < maxAge
}

function showError(msg: string) {
  ElMessage.error(msg)
}

onMounted(async () => {
  // 解析二维码参数
  const courseId = String(route.query.courseId || '')
  const classId = String(route.query.classId || '')
  const code = String(route.query.code || '')
  const studentName = sessionStorage.getItem('name') || '未知姓名';
  const tsParam = route.query.ts
  const ts = tsParam ? Number(tsParam) : 0
  // 获取教师设置的刷新间隔（秒），默认60秒
  const interval = Number(route.query.interval) || 60
  // 假设学生ID存在sessionStorage
  const studentId = sessionStorage.getItem('userId') || '1'
  // 1. 参数完整性检查
  if (!courseId || !code || !ts || !studentId || !classId) {
    failReason.value = '参数缺失，无法签到'
    showError(failReason.value)
    loading.value = false
    return
  }

  // 2. 前端二维码有效性验证
  if (!isCodeValid(ts, interval)) {
    failReason.value = `二维码已过期（有效期${interval}秒），请刷新二维码后重新扫码`
    showError(failReason.value)
    loading.value = false
    return
  }

  // 3. 二维码有效才向后端发送请求
  try {
    
    

    // 构造符合Sign接口的对象
    const signData: Sign = {
      SignId: code,       // 二维码中的code就是SignId
      CourseId: courseId,
      interval:interval,
      ts:ts,
      ClassId: classId,
      StudentId: studentId,
      StudentName: studentName,  // 从sessionStorage获取
      SignTime: new Date()       // 当前时间
    };
    console.log(ts, studentId, courseId, code)
    console.log(1234123141)
    console.log('开始签到', signData)
    
    const res = await studentSignIn(signData)
    console.log("签到结果",res)
    if (res.data && res.data.IsSuccess) {
      signInResult.value = 'success'
      signInTimeDisplay.value = new Date().toLocaleString()
    } else {
      failReason.value = res.data.Message || '签到失败，请重试'
      showError(failReason.value)
    }
  } catch (e: any) {
    failReason.value = e?.response?.data?.Message || '网络错误，签到失败'
    showError(failReason.value)
  }

  loading.value = false
})
</script>