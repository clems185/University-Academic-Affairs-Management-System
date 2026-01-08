<template>
  <div
    style="display: flex; flex-direction: column; align-items: center; margin-top: 40px; max-width: 1000px; margin-left: auto; margin-right: auto;">
    <h2>扫码签到</h2>

    <!-- 课程选择表单 -->
    <el-form :inline="true" style="margin-bottom: 16px; width: 100%;">
      <el-form-item label="选择课程" style="margin-right: 24px;">
        <el-select v-model="selectedCourseId" placeholder="请选择课程" style="width: 220px" @change="onCourseChange">
          <el-option v-for="course in courseList" :key="course.CourseId + '-' + course.ClassId" :label="course.Name"
            :value="course.CourseId + '-' + course.ClassId" />
        </el-select>
      </el-form-item>
      <el-form-item label="二维码刷新间隔(秒)">
        <el-input-number v-model="refreshInterval" :min="10" :max="300" :step="10" style="width: 120px"
          @change="onIntervalChange" />
      </el-form-item>
    </el-form>

    <!-- 添加当前课程显示 -->
    <div v-if="selectedCourse" style="width: 100%; text-align: center; margin-bottom: 20px;">
      <h3>当前课程：{{ selectedCourse.Name }}</h3>
    </div>

    <div v-if="selectedCourse" style="display: flex; width: 100%; gap: 40px;">
      <!-- 左侧：二维码区域 -->
      <div style="flex: 1; display: flex; flex-direction: column; align-items: center; min-width: 300px;">
        <!-- 开始签到按钮 -->
        <el-button v-if="!isCheckinActive" type="success" @click="startCheckin"
          style="margin-bottom: 16px; width: 100%;">
          开始签到
        </el-button>

        <div v-if="isCheckinActive" style="display: flex; flex-direction: column; align-items: center; width: 100%;">
          <img v-if="qrcodeUrl" :src="qrcodeUrl" alt="二维码" style="margin-bottom: 8px; max-width: 100%;" />
          <p style="margin: 0 0 8px 0; font-size: 16px;">签到码：{{ checkinCode }}</p>
          <!-- 显示当前二维码的时间戳 -->
          <p style="margin: 0 0 8px 0; font-size: 14px; color: #666;">二维码时间戳：{{ currentQrTimestamp }}</p>
          <p style="margin: 0 0 16px 0; font-size: 14px;">二维码将在 <b style="color: #f56c6c;">{{ remainSeconds }}</b>
            秒后自动刷新</p>

          <!-- 结束签到按钮 -->
          <el-button type="danger" @click="stopCheckin" style="margin-bottom: 16px; width: 100%;">
            结束签到
          </el-button>
        </div>
      </div>

      <!-- 右侧：学生列表区域 -->
      <div style="flex: 2; display: flex; flex-direction: column; min-width: 300px;">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
          <h3 style="margin: 0;">{{ selectedCourse.Name }} - 签到学生列表</h3>
          <el-button 
            type="primary" 
            @click="fetchStudentList"
            :loading="loading"
          >
            刷新列表
          </el-button>
        </div>
        <el-table :data="studentList" style="width: 100%;" height="400">
          <el-table-column prop="StudentId" label="学号" width="120" align="center" />
          <el-table-column prop="StudentName" label="姓名" width="120" align="center" />
          <el-table-column prop="SignTime" label="签到时间" align="center" />
        </el-table>
        <p v-if="studentList.length === 0" style="text-align: center; padding: 20px; color: #999;">
          暂无签到学生
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { getSignInStudents, getClassList } from '@/api/qrcode/qrcode'
import type { SignReq, ClassList } from '@/api/qrcode/types'

const courseList = ref<ClassList[]>([])
const selectedCourseId = ref('')
const selectedCourse = ref<ClassList | null>(null)
const checkinCode = ref('') // 固定的签到码
const qrcodeUrl = ref('')
const refreshInterval = ref(60) // 默认60秒刷新
const remainSeconds = ref(60)
const timer = ref<any>(null)
const studentList = ref<any[]>([])
const loading = ref(false)

const isCheckinActive = ref(false)
const startTimestamp = ref<number>(0) // 签到开始时间（固定）
const currentQrTimestamp = ref<number>(0) // 当前二维码时间戳（动态变化）

function showError(msg: string) {
  ElMessage.error(msg)
}

function showWarning(msg: string) {
  ElMessage.warning(msg)
}

function createCheckinCode() {
  return `${Date.now()}-${Math.floor(1000 + Math.random() * 9000)}`
}

function generateQrcode() {
  if (!selectedCourse.value || !isCheckinActive.value) return

  // 更新当前二维码的时间戳（每次刷新都更新）
  currentQrTimestamp.value = Date.now()

  // 使用固定签到码 + 新时间戳生成URL
  const url = `${window.location.origin}/checkin?classId=${selectedCourse.value.ClassId}&courseId=${selectedCourse.value.CourseId}&code=${checkinCode.value}&ts=${currentQrTimestamp.value}&interval=${refreshInterval.value}`

  const QRCode = require('qrcode-generator')
  const qr = QRCode(0, 'L')
  qr.addData(url)
  qr.make()
  qrcodeUrl.value = qr.createDataURL(6)
  remainSeconds.value = refreshInterval.value
  fetchStudentList()
}

function startCheckin() {
  if (!selectedCourse.value) {
    showWarning('请先选择课程')
    return
  }
  
  isCheckinActive.value = true
  // 记录开始时间（仅用于显示）
  startTimestamp.value = Date.now()
  
  // 生成固定签到码（只生成一次）
  checkinCode.value = createCheckinCode()
  
  // 生成第一个二维码（会设置currentQrTimestamp）
  generateQrcode()
  startAutoRefresh()
}

function stopCheckin() {
  isCheckinActive.value = false
  stopAutoRefresh()
  qrcodeUrl.value = ''
  checkinCode.value = ''
  ElMessage.success('签到已结束')
}

function fetchCourseList() {
  const teacherId = String(sessionStorage.getItem('userId') || '1')
  getClassList(teacherId).then(res => {
    if (res.data && res.data.IsSuccess) {
      courseList.value = res.data.Result
    } else {
      showError('获取课程列表失败')
    }
  }).catch((error) => {
    showError('获取课程列表失败: ' + error.message)
  })
}

async function fetchStudentList() {
  loading.value = true
  
  try {
    if (!selectedCourse.value) {
      showWarning('请先选择课程')
      return
    }
    
    if (!isCheckinActive.value) {
      showWarning('请先开始签到')
      return
    }

    if (!checkinCode.value) {
      showWarning('签到未开始或签到码未生成')
      return
    }

    const signReq: SignReq = {
      ClassId: selectedCourse.value.ClassId,
      CourseId: selectedCourse.value.CourseId,
      SignId: checkinCode.value, // 使用固定签到码
    }
    
    const res = await getSignInStudents(signReq)
    if (res.data && res.data.IsSuccess) {
      studentList.value = res.data.Result
    } else {
      studentList.value = []
      const errorMsg = res.data?.Result|| '未知错误'
      showError(`获取签到学生列表失败: ${errorMsg}`)
    }
  } catch (error: any) {
    studentList.value = []
    showError('获取签到学生列表失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

function startAutoRefresh() {
  if (timer.value) clearInterval(timer.value)
  timer.value = setInterval(() => {
    if (!isCheckinActive.value) return

    remainSeconds.value--
    if (remainSeconds.value <= 0) {
      generateQrcode()
    }
  }, 1000)
}

function stopAutoRefresh() {
  if (timer.value) clearInterval(timer.value)
  timer.value = null
}

function onCourseChange() {
  selectedCourse.value = courseList.value.find(
    c => `${c.CourseId}-${c.ClassId}` === selectedCourseId.value
  ) || null
  
  isCheckinActive.value = false
  stopAutoRefresh()
  qrcodeUrl.value = ''
  checkinCode.value = ''
  
  if (selectedCourse.value) {
    fetchStudentList()
  } else {
    studentList.value = []
  }
}

function onIntervalChange() {
  if (isCheckinActive.value) {
    // 立即应用新的刷新间隔
    generateQrcode()
  }
}

onMounted(() => {
  fetchCourseList()
})

onUnmounted(() => {
  stopAutoRefresh()
})

watch(selectedCourseId, () => {
  selectedCourse.value = courseList.value.find(
    c => `${c.CourseId}-${c.ClassId}` === selectedCourseId.value
  ) || null
})
</script>