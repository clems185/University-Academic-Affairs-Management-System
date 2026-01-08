<template>
  <div class="my-exam-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>我的考试</h2>
      <p>查看我即将参加的考试安排</p>
    </div>

    <!-- 考试列表 -->
    <div class="exams-section">
      <div class="section-header">
        <h3>考试安排列表</h3>
        <el-button 
          type="primary" 
          @click="fetchMyExams"
          :loading="examsLoading">
          刷新列表
        </el-button>
      </div>

      <!-- 数据统计 -->
      <div class="stats-section" v-if="examsData.length > 0">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-statistic title="即将参加的考试" :value="examsData.length" suffix="场" />
          </el-col>
          <el-col :span="8">
            <el-statistic title="涉及课程" :value="uniqueCourseCount" suffix="门" />
          </el-col>
          <el-col :span="8">
            <el-statistic title="不同考场" :value="uniqueClassroomCount" suffix="个" />
          </el-col>
        </el-row>
      </div>

      <!-- 考试表格 -->
      <el-table 
        :data="examsData" 
        stripe 
        style="width: 100%" 
        v-loading="examsLoading"
        empty-text="暂无考试安排">
        
        <el-table-column prop="ExamId" label="考试编号" width="120" />
        
        <el-table-column prop="CourseId" label="课程编号" width="120" />
        
        <el-table-column prop="CourseName" label="课程名称" width="200">
          <template #default="scope">
            <el-tag type="info" size="small">{{ scope.row.CourseName || '未知课程' }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="学期信息" width="150">
          <template #default="scope">
            {{ scope.row.Year }}年第{{ scope.row.Semester }}学期
          </template>
        </el-table-column>
        
        <el-table-column prop="TimeSlotId" label="时间段ID" width="120">
          <template #default="scope">
            <el-tag type="warning" size="small">{{ scope.row.TimeSlotId }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="ClassroomId" label="考场" width="120">
          <template #default="scope">
            <el-tag type="success" size="small">{{ scope.row.ClassroomId }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="考试时间" width="200">
          <template #default="scope">
            <div v-if="scope.row.ExamDate || scope.row.ExamTime">
              <div v-if="scope.row.ExamDate">
                <i class="el-icon-date"></i> {{ scope.row.ExamDate }}
              </div>
              <div v-if="scope.row.ExamTime">
                <i class="el-icon-time"></i> {{ scope.row.ExamTime }}
              </div>
            </div>
            <el-text v-else type="info">待定</el-text>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button 
              size="small" 
              type="info" 
              @click="showExamDetail(scope.row)">
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 考试详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="考试详情"
      width="500px">
      <div v-if="selectedExam">
        <div class="exam-detail">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="考试编号">
              {{ selectedExam.ExamId }}
            </el-descriptions-item>
            <el-descriptions-item label="课程编号">
              {{ selectedExam.CourseId }}
            </el-descriptions-item>
            <el-descriptions-item label="课程名称">
              {{ selectedExam.CourseName || '未知课程' }}
            </el-descriptions-item>
            <el-descriptions-item label="学期">
              {{ selectedExam.Year }}年第{{ selectedExam.Semester }}学期
            </el-descriptions-item>
            <el-descriptions-item label="时间段ID">
              {{ selectedExam.TimeSlotId }}
            </el-descriptions-item>
            <el-descriptions-item label="考场">
              {{ selectedExam.ClassroomId }}
            </el-descriptions-item>
            <el-descriptions-item label="考试日期">
              {{ selectedExam.ExamDate || '待定' }}
            </el-descriptions-item>
            <el-descriptions-item label="考试时间">
              {{ selectedExam.ExamTime || '待定' }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
        
        <div class="exam-tips">
          <el-alert
            title="考试提醒"
            type="info"
            :closable="false">
            <template #default>
              <ul>
                <li>请提前30分钟到达考场</li>
                <li>考试时请携带有效身份证件</li>
                <li>考试期间请保持安静，遵守考场纪律</li>
                <li>如有疑问请及时联系监考老师</li>
              </ul>
            </template>
          </el-alert>
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyExams } from '@/api/myExam/myExam'
import { MyExam } from '@/api/myExam/types'

// 数据状态
const examsData = ref<MyExam[]>([])
const examsLoading = ref(false)

// 对话框状态
const detailDialogVisible = ref(false)
const selectedExam = ref<MyExam | null>(null)

// 计算属性
const uniqueCourseCount = computed(() => {
  const courses = new Set(examsData.value.map(exam => exam.CourseId))
  return courses.size
})

const uniqueClassroomCount = computed(() => {
  const classrooms = new Set(examsData.value.map(exam => exam.ClassroomId))
  return classrooms.size
})

// 获取我的考试列表
const fetchMyExams = async () => {
  try {
    examsLoading.value = true
    const currentUserId = sessionStorage.getItem('userId') || ''
    console.log('当前登录学生ID:', currentUserId) // 调试日志
    
    if (!currentUserId) {
      ElMessage.error('请先登录')
      return
    }
    
    console.log('发送请求获取学生考试，studentId:', currentUserId) // 调试日志
    const response = await getMyExams(currentUserId)
    console.log('后端返回结果:', response.data) // 调试日志
    
    if (response.data.IsSuccess) {
      examsData.value = response.data.Result || []
      console.log('成功获取考试数据:', examsData.value) // 调试日志
      ElMessage.success(`考试列表加载成功，共${examsData.value.length}场考试`)
    } else {
      console.error('后端返回错误:', response.data.Message) // 调试日志
      ElMessage.error(response.data.Message || '获取考试列表失败')
    }
  } catch (error) {
    console.error('获取考试列表失败:', error)
    ElMessage.error('获取考试列表失败，请检查网络连接')
  } finally {
    examsLoading.value = false
  }
}

// 显示考试详情
const showExamDetail = (exam: MyExam) => {
  selectedExam.value = exam
  detailDialogVisible.value = true
}

// 组件挂载时自动加载数据
onMounted(() => {
  fetchMyExams()
})
</script>

<style scoped>
.my-exam-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h2 {
  color: #303133;
  margin-bottom: 10px;
}

.page-header p {
  color: #606266;
  margin: 0;
}

.exams-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h3 {
  color: #303133;
  margin: 0;
}

.stats-section {
  margin-bottom: 20px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 6px;
}

.exam-detail {
  margin-bottom: 20px;
}

.exam-tips {
  margin-top: 20px;
}

.exam-tips ul {
  margin: 0;
  padding-left: 20px;
}

.exam-tips li {
  margin-bottom: 5px;
  color: #606266;
}

.dialog-footer {
  text-align: right;
}

.el-table {
  font-size: 14px;
}

.el-statistic {
  text-align: center;
}

:deep(.el-descriptions__label) {
  width: 100px;
  font-weight: bold;
}
</style>