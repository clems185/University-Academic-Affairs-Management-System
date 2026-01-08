<template>
  <div class="my-teaching-container">
    <!-- 顶部筛选区域 -->
    <div class="filter-section">
      <div class="filter-card">
        <h3 class="filter-title">我的授课 <span v-if="currentUser.userId" class="user-info">（{{ currentUser.userId }}）</span></h3>
        <div class="filter-controls">
          <div class="filter-item">
            <span class="filter-label">学年:</span>
            <el-select v-model="queryParams.year" placeholder="请选择学年" clearable style="width: 200px">
              <el-option
                v-for="year in availableYears"
                :key="year"
                :label="year"
                :value="year"
              />
            </el-select>
          </div>
          <div class="filter-item">
            <span class="filter-label">学期:</span>
            <el-select v-model="queryParams.semester" placeholder="请选择学期" clearable style="width: 200px">
              <el-option
                v-for="semester in availableSemesters"
                :key="semester.value"
                :label="semester.label"
                :value="semester.value"
              />
            </el-select>
          </div>
          <el-button type="primary" @click="loadTeachingData">查询授课信息</el-button>
        </div>
      </div>
    </div>

    <!-- 授课统计信息 -->
    <div class="stats-section" v-if="teachingStats">
      <div class="stats-card">
        <h3 class="stats-title">授课统计</h3>
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-number">{{ teachingStats.totalCourses }}</div>
            <div class="stat-label">授课课程数</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ teachingStats.totalClasses }}</div>
            <div class="stat-label">授课班级数</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ teachingStats.totalStudents }}</div>
            <div class="stat-label">总学生数</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ queryParams.year }}学年</div>
            <div class="stat-label">当前学年</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 授课班级列表 -->
    <div class="teaching-list-section">
      <div class="teaching-card">
        <h3 class="teaching-title">授课班级列表</h3>
        
        <!-- 班级列表状态提示 -->
        <div class="teaching-status-info" v-if="teachingClasses.length === 0">
          <el-alert
            title="暂无授课班级信息"
            type="info"
            :closable="false"
            show-icon
          >
            <template #default>
              <p>当前学年：{{ queryParams.year }}学年</p>
              <p>当前学期：{{ queryParams.semester === '0' ? '第一学期' : '第二学期' }}</p>
              <p>可能的原因：</p>
              <ul>
                <li>该学期暂无授课安排</li>
                <li>课程信息尚未录入系统</li>
                <li>请检查学年和学期选择是否正确</li>
              </ul>
            </template>
          </el-alert>
        </div>
        
        <el-table 
          :data="teachingClasses" 
          stripe 
          border
          height="500"
          style="width: 100%"
          @row-click="handleClassClick"
          :header-cell-style="{ 'background-color': '#f5f7fa', 'color': '#333', 'font-size': '16px', 'font-weight': '600' }"
          :cell-style="{ 'font-size': '15px' }"
          :empty-text="teachingClasses.length === 0 ? '暂无授课班级信息' : '暂无数据'"
        >
          <el-table-column prop="courseId" label="课程编号" width="140" align="center" />
          <el-table-column prop="courseName" label="课程名称" min-width="200" align="center" show-overflow-tooltip />
          <el-table-column prop="className" label="班级名称" width="160" align="center" />
          <el-table-column prop="courseType" label="课程类型" width="120" align="center">
            <template #default="scope">
              <el-tag :type="scope.row.courseType === '必修' ? 'danger' : 'success'" size="small">
                {{ scope.row.courseType }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="credits" label="学分" width="100" align="center" />
          <el-table-column prop="assessmentType" label="考核方式" width="120" align="center">
            <template #default="scope">
              <el-tag :type="scope.row.assessmentType === '考试' ? 'warning' : 'info'" size="small">
                {{ scope.row.assessmentType }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="周次" width="140" align="center">
            <template #default="scope">
              {{ scope.row.startWeek }}-{{ scope.row.endWeek }}周
            </template>
          </el-table-column>
          <el-table-column prop="year" label="学年" width="120" align="center" />
          <el-table-column prop="semester" label="学期" width="120" align="center" />
          <el-table-column label="操作" width="120" align="center" fixed="right">
            <template #default="scope">
              <el-button 
                type="primary" 
                size="default" 
                @click.stop="viewClassStudents(scope.row)"
              >
                查看学生
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <!-- 学生信息弹窗 -->
    <el-dialog 
      v-model="studentDialogVisible" 
      :title="`${currentClass.courseName} - ${currentClass.className} 学生列表`"
      width="95%"
      :before-close="handleCloseStudentDialog"
      top="3vh"
    >
      <!-- 学生信息状态显示 - 只在没有学生时显示 -->
      <div class="student-status-info" v-if="studentsLoaded && classStudents.length === 0">
        <el-alert
          title="该班级暂无学生信息"
          type="info"
          :closable="false"
          show-icon
        >
          <template #default>
            <p>当前班级：{{ currentClass.className }}</p>
            <p>课程：{{ currentClass.courseName }}</p>
            <p>可能的原因：</p>
            <ul>
              <li>该班级本学期暂无学生选课</li>
              <li>学生信息尚未录入系统</li>
              <li>数据同步延迟</li>
            </ul>
          </template>
        </el-alert>
      </div>
      <el-table 
        :data="classStudents" 
        stripe 
        border
        height="600"
        style="width: 100%"
        :header-cell-style="{ 'background-color': '#f5f7fa', 'color': '#333', 'font-size': '16px', 'font-weight': '600' }"
        :cell-style="{ 'font-size': '15px' }"
        :empty-text="getEmptyText()"
      >
        <el-table-column type="index" label="序号" width="100" align="center" />
        <el-table-column prop="studentId" label="学生学号" width="180" align="center" />
        <el-table-column prop="studentName" label="姓名" width="150" align="center" />
        <el-table-column prop="studentSex" label="性别" width="120" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.studentSex === '男' ? 'primary' : 'danger'" size="default">
              {{ scope.row.studentSex }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="studentMajorName" label="专业" min-width="250" align="center" show-overflow-tooltip />
        <el-table-column prop="studentStartYear" label="入学年份" width="150" align="center">
          <template #default="scope">
            {{ scope.row.studentStartYear }}级
          </template>
        </el-table-column>
      </el-table>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="studentDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyTeachingClasses, getClassStudents } from '@/api/myTeaching/myTeaching'

// 响应式数据
const queryParams = ref({
  year: '2025',
  semester: '0'
})
const teachingClasses = ref<any[]>([])
const classStudents = ref<any[]>([])
const currentClass = ref<any>({})
const studentDialogVisible = ref(false)
const teachingStats = ref<any>(null)
const currentUser = ref<any>({})
const studentsLoading = ref(false)
const studentsLoaded = ref(false)

// 可用选项
const availableYears = ref(['2023', '2024', '2025','2026','2027'])
const availableSemesters = ref([
  { value: '0', label: '第一学期' },
  { value: '1', label: '第二学期' }
])

// 获取我的授课信息
const loadTeachingData = async () => {
  try {
    // 从sessionStorage获取当前登录用户的ID
    const teacherId = sessionStorage.getItem('userId')
    
    if (!teacherId) {
      ElMessage.error('未获取到用户信息，请重新登录')
      return
    }
    
    // 获取课程班级列表
    const classesResult = await getMyTeachingClasses({
      teacherId,
      year: queryParams.value.year,
      semester: queryParams.value.semester
    })
    
    // 后端使用 IsSuccess 属性，前端需要适配
    if (classesResult.isSuccess || classesResult.IsSuccess) {
      teachingClasses.value = classesResult.result || classesResult.Result || []
      console.log('课程班级数据加载成功:', teachingClasses.value)
      console.log('课程班级数据长度:', teachingClasses.value.length)
      if (teachingClasses.value.length > 0) {
        console.log('第一条课程班级数据:', teachingClasses.value[0])
      }
    } else {
      teachingClasses.value = []
      console.error('获取课程班级信息失败:', classesResult.msg || classesResult.Msg)
    }

    // 生成统计信息
    generateTeachingStats()
    
    // 获取所有班级的学生总数
    await getAllStudentsCount()
    
    ElMessage.success('授课班级信息加载成功')
  } catch (error) {
    console.error('加载授课班级信息失败:', error)
    ElMessage.error('加载授课班级信息失败')
    teachingClasses.value = []
  }
}

// 生成授课统计信息
const generateTeachingStats = () => {
  const totalCourses = new Set(teachingClasses.value.map(item => item.courseId)).size
  const totalClasses = new Set(teachingClasses.value.map(item => item.className)).size
  
  // 计算所有班级的学生总数（这里暂时设为0，因为需要单独查询每个班级的学生）
  const totalStudents = 0
  
  teachingStats.value = {
    totalCourses,
    totalClasses,
    totalStudents
  }
}

// 获取所有班级的学生总数
const getAllStudentsCount = async () => {
  if (teachingClasses.value.length === 0) {
    return 0
  }
  
  try {
    const teacherId = sessionStorage.getItem('userId')
    if (!teacherId) return 0
    
    let totalCount = 0
    
    // 遍历所有班级，获取学生数量
    for (const classInfo of teachingClasses.value) {
      const studentsResult = await getClassStudents({
        teacherId,
        year: queryParams.value.year,
        semester: queryParams.value.semester,
        courseId: classInfo.courseId,
        className: classInfo.className
      })
      
      if (studentsResult.isSuccess || studentsResult.IsSuccess) {
        const students = studentsResult.result || studentsResult.Result || []
        totalCount += students.length
      }
    }
    
    // 更新统计信息
    if (teachingStats.value) {
      teachingStats.value.totalStudents = totalCount
    }
    
    return totalCount
  } catch (error) {
    console.error('获取学生总数失败:', error)
    return 0
  }
}



// 处理班级点击
const handleClassClick = (row: any) => {
  console.log('点击班级:', row)
}

// 查看班级学生信息
const viewClassStudents = async (classInfo: any) => {
  try {
    currentClass.value = classInfo
    studentDialogVisible.value = true
    studentsLoading.value = true
    studentsLoaded.value = false
    
    // 从sessionStorage获取当前登录用户的ID
    const teacherId = sessionStorage.getItem('userId')
    
    if (!teacherId) {
      ElMessage.error('未获取到用户信息，请重新登录')
      studentDialogVisible.value = false
      return
    }
    
    // 获取班级学生信息
    const studentsResult = await getClassStudents({
      teacherId,
      year: queryParams.value.year,
      semester: queryParams.value.semester,
      courseId: classInfo.courseId,
      className: classInfo.className
    })
    
    if (studentsResult.isSuccess || studentsResult.IsSuccess) {
      classStudents.value = studentsResult.result || studentsResult.Result || []
      studentsLoaded.value = true
      console.log('班级学生数据加载成功:', classStudents.value)
      console.log('班级学生数据长度:', classStudents.value.length)
      
      // 根据学生数量显示不同的提示信息
      if (classStudents.value.length === 0) {
        ElMessage.info('该班级暂无学生信息')
      } else {
        ElMessage.success(`已加载 ${classStudents.value.length} 名学生`)
      }
      
      // 调试信息：显示实际的学生数据
      console.log('学生数据详情:', classStudents.value.map(s => ({
        studentId: s.studentId,
        studentName: s.studentName,
        studentSex: s.studentSex
      })))
    } else {
      classStudents.value = []
      studentsLoaded.value = true
      console.error('获取班级学生信息失败:', studentsResult.msg || studentsResult.Msg)
      ElMessage.error(studentsResult.msg || studentsResult.Msg || '获取班级学生信息失败')
    }
  } catch (error) {
    console.error('加载班级学生信息失败:', error)
    ElMessage.error('加载班级学生信息失败')
    classStudents.value = []
    studentsLoaded.value = true
  } finally {
    studentsLoading.value = false
  }
}

// 关闭学生信息弹窗
const handleCloseStudentDialog = () => {
  studentDialogVisible.value = false
  classStudents.value = []
  currentClass.value = {}
  studentsLoading.value = false
  studentsLoaded.value = false
}

// 获取表格空状态文本
const getEmptyText = () => {
  if (studentsLoading.value) {
    return '正在加载学生信息...'
  }
  if (studentsLoaded.value && classStudents.value.length === 0) {
    return '该班级暂无学生信息'
  }
  if (!studentsLoaded.value) {
    return '请点击"查看学生"按钮加载学生信息'
  }
  return '暂无数据'
}

// 初始化用户信息
const initUserInfo = () => {
  const userId = sessionStorage.getItem('userId')
  const userType = sessionStorage.getItem('userType')
  
  currentUser.value = {
    userId: userId || '',
    userType: userType || ''
  }
  
  console.log('当前用户信息:', currentUser.value)
  
  // 验证用户类型是否为教师
  if (userType !== '2') { // 教师类型为2
    ElMessage.warning('当前用户不是教师，无法查看授课信息')
    return false
  }
  
  if (!userId) {
    ElMessage.error('未获取到用户ID，请重新登录')
    return false
  }
  
  return true
}

// 页面加载时自动查询
onMounted(() => {
  if (initUserInfo()) {
    loadTeachingData()
  }
})
</script>

<style scoped>
.my-teaching-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
  width: 100%;
  max-width: 100%;
}

.filter-section {
  margin-bottom: 20px;
}

.filter-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.filter-title {
  margin: 0 0 20px 0;
  color: #333;
  font-size: 20px;
  font-weight: 600;
}

.user-info {
  font-size: 16px;
  color: #1890ff;
  font-weight: 500;
}

.filter-controls {
  display: flex;
  gap: 20px;
  align-items: center;
  flex-wrap: wrap;
}

.filter-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.filter-label {
  font-weight: 600;
  color: #333;
  min-width: 60px;
  font-size: 16px;
}

.stats-section {
  margin-bottom: 20px;
}

.stats-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.stats-title {
  margin: 0 0 20px 0;
  color: #333;
  font-size: 20px;
  font-weight: 600;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 20px;
}

.stat-item {
  text-align: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.stat-number {
  font-size: 36px;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 8px;
}

.stat-label {
  color: #666;
  font-size: 16px;
  font-weight: 500;
}

.teaching-list-section {
  margin-bottom: 20px;
}

.teaching-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.teaching-title {
  margin: 0 0 20px 0;
  color: #333;
  font-size: 20px;
  font-weight: 600;
}



/* 表格优化 */
:deep(.el-table th) {
  font-weight: 600;
}

:deep(.el-table .el-table__row:hover > td) {
  background-color: #f5f7fa !important;
}

/* 弹窗样式优化 */
:deep(.el-dialog__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px 24px;
  margin: 0;
  border-radius: 8px 8px 0 0;
}

/* 状态信息样式 */
.student-status-info,
.teaching-status-info {
  margin-bottom: 20px;
}

.student-status-info .el-alert,
.teaching-status-info .el-alert {
  margin-bottom: 15px;
}

.student-status-info .el-alert ul,
.teaching-status-info .el-alert ul {
  margin: 10px 0 0 20px;
  padding: 0;
}

.student-status-info .el-alert li,
.teaching-status-info .el-alert li {
  margin: 5px 0;
  color: #666;
}

:deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
  font-size: 18px;
}

:deep(.el-dialog__body) {
  padding: 20px 24px;
}

:deep(.el-dialog__footer) {
  padding: 15px 24px 20px;
  border-top: 1px solid #e8e8e8;
}

.schedule-section {
  margin-bottom: 20px;
}

.schedule-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.schedule-header {
  margin-bottom: 20px;
}

.schedule-title {
  margin: 0;
  color: #333;
  font-size: 18px;
}

.schedule-table {
  overflow-x: auto;
}

.schedule-table table {
  width: 100%;
  border-collapse: collapse;
  border: 1px solid #e8e8e8;
}

.schedule-table th,
.schedule-table td {
  border: 1px solid #e8e8e8;
  padding: 8px;
  text-align: center;
  vertical-align: middle;
}

.time-header {
  background-color: #fafafa;
  font-weight: 500;
  color: #333;
}

.time-cell {
  background-color: #fafafa;
  min-width: 80px;
}

.period-name {
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.period-time {
  font-size: 12px;
  color: #666;
}

.course-slot {
  padding: 8px;
  border-radius: 4px;
  min-height: 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #333;
  font-size: 12px;
  line-height: 1.3;
}

.teaching-slot {
  border: 2px solid #1890ff;
}

.course-id {
  font-weight: bold;
  margin-bottom: 2px;
}

.course-name {
  margin-bottom: 2px;
  word-break: break-word;
}

.classroom {
  font-size: 11px;
  color: #666;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .my-teaching-container {
    padding: 15px;
  }
  
  .filter-controls {
    flex-direction: column;
    align-items: stretch;
  }
  
  .filter-item {
    justify-content: space-between;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  

  
  :deep(.el-dialog) {
    width: 95% !important;
    margin: 0 auto;
  }
  
  :deep(.el-table .el-table__cell) {
    padding: 8px 4px;
  }
}

@media (max-width: 480px) {
  .stat-number {
    font-size: 24px;
  }
  
  .filter-card,
  .stats-card,
  .teaching-card {
    padding: 15px;
  }
  
  .info-item {
    font-size: 13px;
  }
}
</style> 