<template>
  <div class="admin-schedule-container">
    <div class="admin-header">
      <h2>{{ `${collegeName}管理员${adminName}您好！` }}</h2>
      <h3>欢迎使用一键排课！</h3>
    </div>

    <div class="filter-section">
      <div class="filter-controls">
        <el-select v-model="selectedYear" placeholder="请选择学年" class="filter-input">
          <el-option
            v-for="year in availableYears"
            :key="year"
            :label="`${year}年`"
            :value="year.toString()"
          />
        </el-select>
        
        <el-select v-model="selectedSemester" placeholder="请选择学期" class="filter-input">
          <el-option
            v-for="semester in semesters"
            :key="semester.value"
            :label="semester.label"
            :value="semester.value"
          />
        </el-select>
        
        <el-button type="primary" @click="fetchCourses" class="filter-btn">筛选</el-button>
      </div>
    </div>

    <div class="course-list-section">
      <h3>课程班级列表</h3>
      <div class="course-list-container">
        <el-table 
          :data="filteredCourses" 
          height="400"
          border
          style="width: 100%"
          class="course-table"
        >
          <el-table-column prop="ClassId" label="班号" min-width="120" >
              <template #default="{ row }">
                {{ row.CourseId }} - {{ row.ClassId }}
              </template>
            </el-table-column>
          <el-table-column prop="CourseName" label="课程名称" min-width="200" />
          <el-table-column prop="TeacherName" label="授课教师" min-width="150" />
          <el-table-column label="学年学期" min-width="150">
            <template #default="{ row }">
              {{ `${row.Year}年 ${row.Semester === '0' ? '第一学期' : '第二学期'}` }}
            </template>
          </el-table-column>
          <el-table-column label="容量" min-width="100">
            <template #default="{ row }">
              {{ row.Capacity }}
            </template>
          </el-table-column>
          <el-table-column label="已选人数" min-width="100">
            <template #default="{ row }">
              {{ row.SelectedCount }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

  <div class="action-section">
    <p>请您确认无误后，点击：</p>
    <el-button type="primary" size="large" @click="openConstraintsDialog">一键排课</el-button>
  </div>

    <!-- 排课约束条件对话框 -->
    <el-dialog 
      v-model="constraintsDialogVisible" 
      title="排课约束条件设置"
      width="800px"
      :close-on-click-modal="false"
    >
      <div class="constraints-container">
        <div class="constraints-column">
          <div v-for="(constraint, index) in constraints.slice(0, Math.ceil(constraints.length/2))" :key="index" class="constraint-item">
            <el-checkbox v-model="constraint.selected">{{ constraint.label }}</el-checkbox>
          </div>
        </div>
        <div class="constraints-column">
          <div v-for="(constraint, index) in constraints.slice(Math.ceil(constraints.length/2))" :key="index" class="constraint-item">
            <el-checkbox v-model="constraint.selected">{{ constraint.label }}</el-checkbox>
          </div>
        </div>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button type="danger" @click="constraintsDialogVisible = false">上一步</el-button>
          <el-button type="primary" @click="runScheduling">下一步</el-button>
        </div>
      </template>
    </el-dialog>

<el-dialog 
    v-model="resultDialogVisible" 
    title="排课结果"
    width="1200px"
    :close-on-click-modal="false"
  >
    <div class="result-container">
      <h3>{{ `排课结果 - ${selectedYear}年 ${selectedSemester === "0" ? '第一学期' : '第二学期'}` }}</h3>
      
      <div class="stats-section">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-statistic title="总课程数" :value="scheduledCourses.length" />
          </el-col>
          <el-col :span="8">
            <el-statistic title="成功排课数" :value="successfulSchedules" />
          </el-col>
          <el-col :span="8">
            <el-statistic title="排课成功率" 
              :value="scheduledCourses.length > 0 ? Math.round((successfulSchedules / scheduledCourses.length) * 100) : 0" 
              suffix="%" />
          </el-col>

        </el-row>
      </div>
      
      <div class="course-result-list">
        <el-table :data="scheduledCourses" height="400" border style="width: 100%">
          <el-table-column label="班号" min-width="100">
            <template #default="{ row }">
              {{ row.CourseId }} - {{ row.ClassId }}
            </template>
          </el-table-column>
          <el-table-column prop="CourseName" label="课程名" min-width="150" />
          <el-table-column prop="TeacherName" label="教师名" min-width="120" />
          <el-table-column prop="Location" label="地点" min-width="100" />
          <el-table-column label="选课情况" min-width="100">
            <template #default="{ row }">
              {{ `${row.SelectedCount}/${row.Capacity}` }}
            </template>
          </el-table-column>
          <el-table-column label="时间安排" min-width="200">
            <template #default="{ row }">
              <div v-if="row.TimeSlots && row.TimeSlots.length > 0">
                <div v-for="(slot, index) in row.TimeSlots" :key="index">
                  {{ formatTimeSlot(slot) }}
                </div>
              </div>
              <span v-else class="no-schedule">未安排</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="100">
            <template #default="{ row }">
              <el-tag :type="row.TimeSlots && row.TimeSlots.length > 0 ? 'success' : 'danger'">
                {{ row.TimeSlots && row.TimeSlots.length > 0 ? '已安排' : '未安排' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    
    <template #footer>
      <div class="dialog-footer">
        <el-button type="danger" @click="resultDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmScheduling">确认排课结果</el-button>
      </div>
    </template>
  </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  fetchAllCourses, 
  scheduleCourses 
} from '@/api/scheduleCourse'
import type { CourseInfo } from '@/api/scheduleCourse/types'

const totalStudents = ref(0)

// 管理员信息
const collegeName = ref('同济大学分校')
const adminName = ref('王鹤桥')

// 使用计算属性来获取成功排课数
const successfulSchedules = computed(() => {
  return scheduledCourses.value.filter(c => 
    c.TimeSlots && c.TimeSlots.length > 0 && c.Location !== '未安排' && c.Location !== '无合适教室').length
})

// 格式化时间段显示
const formatTimeSlot = (timeSlot: string) => {
  if (!timeSlot) return ''
  
  const slotIds = timeSlot.split(',').map(Number)
  if (slotIds.length === 0) return ''
  
  // 计算星期几和节次
  const day = Math.floor(slotIds[0] / 12)
  const startSlot = slotIds[0] % 12
  const endSlot = slotIds[slotIds.length - 1] % 12
  
  const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  return `${days[day]}第${startSlot + 1}-${endSlot + 1}节`
}

// 运行排课算法
const runScheduling = async () => {
  try {
    const selectedConstraints = constraints.value
      .filter(c => c.selected)
      .map(c => c.id)
    
    loading.value = true
    const response = await scheduleCourses({
      year: selectedYear.value,
      semester: selectedSemester.value,
      constraints: selectedConstraints
    })
    
    // 处理排课结果
    scheduledCourses.value = response.map((course: any) => ({
      ...course,
      TimeSlots: course.TimeSlots || [] // 确保TimeSlots存在
    }))
    
    totalStudents.value = scheduledCourses.value.reduce((sum, c) => sum + c.SelectedCount, 0)
    
    constraintsDialogVisible.value = false
    resultDialogVisible.value = true
    
  } catch (error) {
    ElMessage.error('排课失败，请稍后重试')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 学年学期选择 - 与后端对齐（0=第一学期，1=第二学期）
const currentYear = new Date().getFullYear()
const availableYears = ref(Array.from({length: 5}, (_, i) => currentYear - 2 + i))
const selectedYear = ref(currentYear.toString())
const semesters = ref([
  { label: '第一学期', value: '0' },  // 0对应第一学期
  { label: '第二学期', value: '1' }   // 1对应第二学期
])
const selectedSemester = ref("0")  // 默认选中第一学期

// 课程数据
const allCourses = ref<CourseInfo[]>([])
const filteredCourses = ref<CourseInfo[]>([])

const constraints = ref([
  { id: 'avoidEvening', label: '晚上尽可能少排课', selected: true },
  { id: 'tuesdayAfternoonOff', label: '周二下午不排课', selected: true },
  { id: 'sameLocationPerCourse', label: '同一课程在同一教室', selected: true },
  { id: 'teacherTimeConflict', label: '避免教师时间冲突', selected: true },
  { id: 'balancedSchedule', label: '平衡每日课程数量', selected: false },
  { id: 'preferMorningClasses', label: '优先安排在上午', selected: false },
  { id: 'avoidLunchBreak', label: '避免午餐时间排课', selected: true },
  { id: 'consecutiveClasses', label: '同一课程连续安排', selected: false },
  { id: 'allowSaturday', label: '允许周六排课', selected: false },
  { id: 'allowSunday', label: '允许周日排课', selected: false }
])

// 对话框控制
const constraintsDialogVisible = ref(false)
const resultDialogVisible = ref(false)
const loading = ref(false)

// 排课结果
const scheduledCourses = ref<CourseInfo[]>([])

// 获取所有课程
const fetchAllCourseData = async () => {
  try {
    const response = await fetchAllCourses()
    allCourses.value = response
    filteredCourses.value = allCourses.value
  } catch (error) {
    ElMessage.error('获取课程数据失败')
    console.error(error)
  }
}

// 筛选课程 - 严格匹配后端学期定义（0=第一学期，1=第二学期）
const fetchCourses = () => {
  if (!selectedYear.value || !selectedSemester.value) {
    ElMessage.warning('请选择学年和学期')
    return
  }
  
  filteredCourses.value = allCourses.value.filter(course => 
    course.Year === selectedYear.value && 
    course.Semester === selectedSemester.value  // 直接匹配（'0'或'1'）
  )
  
  if (filteredCourses.value.length === 0) {
    ElMessage.info('该学年学期没有课程数据')
  }
}

// 打开约束条件对话框
const openConstraintsDialog = () => {
  if (filteredCourses.value.length === 0) {
    ElMessage.warning('当前没有可排课的课程')
    return
  }
  
  constraintsDialogVisible.value = true
}

// 确认排课结果
const confirmScheduling = () => {
  resultDialogVisible.value = false
  ElMessage.success('排课结果已保存！学生现在可以查看课程安排')
}

onMounted(() => {
  fetchAllCourseData()
})
</script>

<style scoped>
/* 修复滚动条问题：将min-height改为auto，移除固定视口高度限制 */
.admin-schedule-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100%;
  height: auto;
  box-sizing: border-box; /* 确保padding不会增加总宽度 */
}

.admin-header {
  background: linear-gradient(135deg, #409EFF, #1a56db);
  color: white;
  padding: 20px 30px;
  border-radius: 10px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.filter-section {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.filter-controls {
  display: flex;
  gap: 15px;
  align-items: center;
}

.filter-input {
  flex: 1;
}

.filter-btn {
  width: 100px;
}

.course-list-section {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.course-list-container {
  margin-top: 15px;
}

/* 关键修复：增加底部边距确保按钮可见，添加内边距防止内容紧贴底部 */
.action-section {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-top: 20px;
  margin-bottom: 150px; /* 增加底部外边距，确保滚动时能完全显示 */
  padding-bottom: 30px; /* 增加内边距，让按钮不紧贴底部 */
}

.action-section p {
  margin-bottom: 20px;
  font-size: 16px;
}

.constraints-container {
  display: flex;
  justify-content: space-between;
}

.constraints-column {
  width: 48%;
}

.constraint-item {
  margin-bottom: 15px;
}

.result-container {
  padding: 10px;
}

.stats-section {
  margin: 20px 0;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.course-result-list {
  margin-top: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.course-table :deep(.el-table__header) th,
.course-table :deep(.el-table__body) td {
  padding: 12px 0;
}

.course-table :deep(.el-table__body) .cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.el-button--primary:hover {
  background-color: #1a56db;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

/* 确保页面可以正常滚动 */
html, body {
  height: 100%;
  overflow-y: auto; /* 允许垂直滚动 */
  margin: 0;
  padding: 0;
}

.no-schedule {
  color: #f56c6c;
  font-style: italic;
}
</style>