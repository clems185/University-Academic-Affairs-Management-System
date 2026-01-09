<template>
  <div class="weekly-course-container">
    <!-- 顶部筛选区域 -->
    <div class="filter-section">
      <!-- 管理员提示 -->
      <div v-if="currentUserType === 'ADMIN'" class="admin-notice">
        <el-alert
          title="管理员权限"
          type="success"
          description="您可以查看所有课程的周课表信息，包括学生数量、教室分配等详细信息"
          show-icon
          :closable="false"
          style="margin-bottom: 20px"
        />
      </div>
      
      <div class="filter-card">
        <h3 class="filter-title">周课表查询</h3>
        <div class="filter-controls">
          <div class="filter-item">
            <span class="filter-label">学年:</span>
            <el-select v-model="searchParams.year" placeholder="请选择学年" clearable style="width: 200px">
              <el-option
                v-for="year in yearOptions"
                :key="year"
                :label="year"
                :value="year"
              />
            </el-select>
          </div>
          <div class="filter-item">
            <span class="filter-label">学期:</span>
            <el-select v-model="searchParams.semester" placeholder="请选择学期" clearable style="width: 200px">
              <el-option
                v-for="semester in availableSemesters"
                :key="semester.value"
                :label="semester.label"
                :value="semester.value"
              />
            </el-select>
          </div>
          <div class="filter-item">
            <span class="filter-label">指定周次:</span>
            <el-input-number 
              v-model="searchParams.week" 
              :min="1" 
              :max="20" 
              placeholder="周次(可选)"
              style="width: 120px"
            />
          </div>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </div>
      </div>
    </div>

    <!-- 周课表统计信息 -->
    <div class="stats-section" v-if="scheduleSummary">
      <div class="stats-card">
        <h3 class="stats-title">周课表统计</h3>
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-number">{{ scheduleSummary.TotalCourses || scheduleSummary.totalCourses || 0 }}</div>
            <div class="stat-label">总课程数</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ scheduleSummary.TotalHours || scheduleSummary.totalHours || 0 }}</div>
            <div class="stat-label">总学时</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ uniqueCoursesCount }}</div>
            <div class="stat-label">课程种类</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ totalCredits }}</div>
            <div class="stat-label">总学分</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 课表主体 -->
    <div class="schedule-container">
      <el-card shadow="never">
        <!-- 课表头部 -->
        <div class="schedule-header">
          <div class="time-header"></div>
          <div v-for="day in weekDays" :key="day.value" class="day-header">
            {{ day.label }}
          </div>
        </div>

        <!-- 课表内容 -->
        <div class="schedule-body">
          <!-- 时间轴 -->
          <div class="time-axis">
            <div v-for="slot in timeSlots" :key="slot.id" class="time-slot">
              {{ slot.displayText }}
            </div>
          </div>

          <!-- 课程内容 -->
          <div class="course-content">
            <div v-for="day in 7" :key="day" class="day-column">
              <!-- 每个时间单元格 -->
              <div v-for="slot in timeSlots" :key="slot.id" class="time-cell">
                <div class="cell-inner">
                  <!-- 只在该课程的开始节次显示课程块 -->
                  <template v-if="shouldDisplayCourse(day, slot.id)">
                    <div v-for="(course, index) in getCoursesByTime(day, slot.id)" 
                         :key="course.courseId + course.className + course.startPeriod"
                         class="course-block"
                         :class="{ 'multi-course': getCoursesByTime(day, slot.id).length > 1 }"
                         :style="getCourseBlockStyle(course, index)"
                         @click="showCourseDetail(course)">
                      <div class="course-info">
                        {{ formatCourseInfo(course) }}
                      </div>
                    </div>
                  </template>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 课程详细信息表格 -->
    <div class="course-detail-section" v-if="weeklySchedule.length > 0">
      <div class="detail-card">
        <h3 class="detail-title" v-if="currentUserType === 'ADMIN'">课程管理列表</h3>
        <h3 class="detail-title" v-else>课程详细信息</h3>
        
        <!-- 管理员端当前查询周次显示 -->
        <div v-if="currentUserType === 'ADMIN'" class="current-week-info">
          <el-alert
            :title="`当前查询：第 ${searchParams.week} 周`"
            type="info"
            :description="`显示第 ${searchParams.week} 周的课程安排，当前周列标识课程是否在该周进行`"
            show-icon
            :closable="false"
            style="margin-bottom: 20px"
          />
        </div>
        
        <el-table 
          :data="weeklySchedule" 
          stripe 
          border
          :max-height="tableMaxHeight"
          style="width: 100%"
          class="course-detail-table"
          :scrollbar-always-on="true"
        >
          <el-table-column label="课程ID" min-width="90" align="center">
            <template #default="scope">
              {{ scope.row.courseId || scope.row.CourseId || '未设置' }}
            </template>
          </el-table-column>
          <el-table-column label="课程名称" min-width="140" align="center">
            <template #default="scope">
              {{ scope.row.courseName || scope.row.CourseName || '未设置' }}
            </template>
          </el-table-column>
          <el-table-column label="课程类型" min-width="90" align="center">
            <template #default="scope">
              <el-tag :type="(scope.row.courseType || scope.row.CourseType) === '选修' ? 'success' : 'primary'" size="small">
                {{ scope.row.courseType || scope.row.CourseType || '必修' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="学分" min-width="70" align="center">
            <template #default="scope">
              {{ scope.row.credits || scope.row.Credits || 0 }}
            </template>
          </el-table-column>
          <el-table-column label="考核方式" min-width="90" align="center">
            <template #default="scope">
              <el-tag :type="(scope.row.assessmentType || scope.row.AssessmentType) === '考试' ? 'danger' : 'warning'" size="small">
                {{ scope.row.assessmentType || scope.row.AssessmentType || '考查' }}
              </el-tag>
            </template>
          </el-table-column>
          <!-- 根据用户类型显示不同列 -->
          <el-table-column 
            v-if="currentUserType === 'student' || currentUserType === 'ADMIN'" 
            label="授课教师" 
            min-width="90" 
            align="center" 
          >
            <template #default="scope">
              {{ scope.row.teacherName || scope.row.TeacherName || '未设置' }}
            </template>
          </el-table-column>
          <el-table-column 
            v-if="currentUserType === 'teacher' || currentUserType === 'ADMIN'" 
            label="学生数量" 
            min-width="90" 
            align="center" 
          >
            <template #default="scope">
              {{ scope.row.studentCount || scope.row.StudentCount || 0 }}人
            </template>
          </el-table-column>
          <el-table-column label="上课地点" min-width="180" align="center">
            <template #default="scope">
              <div class="location-info">
                <div class="campus-building">
                  {{ (scope.row.campus || scope.row.Campus) ? 
                      (scope.row.campus || scope.row.Campus) + (scope.row.building || scope.row.Building ? ' - ' + (scope.row.building || scope.row.Building) : '') : 
                      '未设置' }}
                </div>
                <div class="room-number">
                  {{ scope.row.roomNumber || scope.row.classroom || scope.row.Classroom || '未设置' }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="周次模式" min-width="90" align="center">
            <template #default="scope">
              {{ scope.row.weekPattern || scope.row.WeekPattern || '1-16' }}
            </template>
          </el-table-column>
          <el-table-column label="上课时间" min-width="110" align="center">
            <template #default="scope">
              <div class="time-info">
                <div class="day-label">{{ getDayLabel(scope.row.dayOfWeek) || '未设置' }}</div>
                <div class="period-info">第{{ scope.row.period || '1-2' }}节</div>
                <div class="time-range">
                  {{ (scope.row.startTime || scope.row.StartTime) && (scope.row.endTime || scope.row.EndTime) ? 
                      (scope.row.startTime || scope.row.StartTime) + '-' + (scope.row.endTime || scope.row.EndTime) : 
                      '未设置' }}
                </div>
              </div>
            </template>
          </el-table-column>

        </el-table>
      </div>
    </div>

    <!-- 无数据提示 -->
    <div class="no-data-section" v-if="weeklySchedule.length === 0 && !loading">
      <div class="no-data-card">
        <el-empty description="暂无周课表数据" />
      </div>
    </div>

    <!-- 课程详情弹窗 -->
    <el-dialog v-model="detailDialogVisible" title="课程详细信息" width="700px">
      <div v-if="selectedCourse" class="course-detail-content">
        <div class="detail-row">
          <span class="detail-label">课程ID:</span>
          <span class="detail-value">{{ selectedCourse.courseId || selectedCourse.CourseId || '未设置' }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">课程名称:</span>
          <span class="detail-value">{{ selectedCourse.courseName || selectedCourse.CourseName || '未设置' }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">班级:</span>
          <span class="detail-value">{{ selectedCourse.className || selectedCourse.ClassName || '未设置' }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">授课教师:</span>
          <span class="detail-value">{{ selectedCourse.teacherName || selectedCourse.TeacherName || '未设置' }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">课程类型:</span>
          <span class="detail-value">
            <el-tag :type="(selectedCourse.courseType || selectedCourse.CourseType) === '选修' ? 'success' : 'primary'" size="small">
              {{ selectedCourse.courseType || selectedCourse.CourseType || '必修' }}
            </el-tag>
          </span>
        </div>
        <div class="detail-row">
          <span class="detail-label">学分:</span>
          <span class="detail-value">{{ selectedCourse.credits || selectedCourse.Credits || 0 }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">考核方式:</span>
          <span class="detail-value">
            <el-tag :type="(selectedCourse.assessmentType || selectedCourse.AssessmentType) === '考试' ? 'danger' : 'warning'" size="small">
              {{ selectedCourse.assessmentType || selectedCourse.AssessmentType || '考查' }}
            </el-tag>
          </span>
        </div>
        <div class="detail-row">
          <span class="detail-label">上课地点:</span>
          <span class="detail-value">
            {{ (selectedCourse.campus || selectedCourse.Campus) ? 
                (selectedCourse.campus || selectedCourse.Campus) + (selectedCourse.building || selectedCourse.Building ? ' - ' + (selectedCourse.building || selectedCourse.Building) : '') + ' ' + (selectedCourse.roomNumber || selectedCourse.classroom || selectedCourse.Classroom || '') : 
                '未设置' }}
          </span>
        </div>
        <div class="detail-row">
          <span class="detail-label">上课时间:</span>
          <span class="detail-value">
            {{ (getDayLabel(selectedCourse.dayOfWeek) && (selectedCourse.startTime || selectedCourse.StartTime) && (selectedCourse.endTime || selectedCourse.EndTime)) ? 
                getDayLabel(selectedCourse.dayOfWeek) + ' ' + (selectedCourse.startTime || selectedCourse.StartTime) + '-' + (selectedCourse.endTime || selectedCourse.EndTime) : 
                '未设置' }}
          </span>
        </div>
        <div class="detail-row">
          <span class="detail-label">周次模式:</span>
          <span class="detail-value">{{ selectedCourse.weekPattern || selectedCourse.WeekPattern || '1-16' }}</span>
        </div>

        <div class="detail-row" v-if="currentUserType === 'ADMIN'">
          <span class="detail-label">学生数量:</span>
          <span class="detail-value">{{ selectedCourse.studentCount || selectedCourse.StudentCount || 0 }}人</span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { WeeklyScheduleItem, WeeklyScheduleSearchParams, TimeSlot, CourseBlock } from '@/api/weeklyCourse/types'
import { getWeeklySchedule, getStudentSchedule, getTeacherSchedule, getAdminSchedule } from '@/api/weeklyCourse/weeklyCourse'

// 用户信息
const userType = sessionStorage.getItem('userType') || '3' // 默认学生
const userId = sessionStorage.getItem('userId') || ''

// 搜索参数
const searchParams = ref<WeeklyScheduleSearchParams>({
  year: '2025',
  semester: '0',
  week: undefined,
  userType: userType,
  userId: userId
})

// 学年选项
const yearOptions = ['2023', '2024', '2025', '2026']

// 周几选项 - 修复后的定义，1-7对应周一到周日
const weekDays = [
  { label: '周一', value: 1 },
  { label: '周二', value: 2 },
  { label: '周三', value: 3 },
  { label: '周四', value: 4 },
  { label: '周五', value: 5 },
  { label: '周六', value: 6 },
  { label: '周日', value: 7 }
]

// 时间段配置
const timeSlots = ref<TimeSlot[]>([
  { id: 1, startTime: '08:00', endTime: '08:45', displayText: '第1节' },
  { id: 2, startTime: '08:50', endTime: '09:35', displayText: '第2节' },
  { id: 3, startTime: '10:00', endTime: '10:45', displayText: '第3节' },
  { id: 4, startTime: '10:50', endTime: '11:35', displayText: '第4节' },
  { id: 5, startTime: '13:30', endTime: '14:15', displayText: '第5节' },
  { id: 6, startTime: '14:20', endTime: '15:05', displayText: '第6节' },
  { id: 7, startTime: '15:30', endTime: '16:15', displayText: '第7节' },
  { id: 8, startTime: '16:20', endTime: '17:05', displayText: '第8节' },
  { id: 9, startTime: '19:00', endTime: '19:45', displayText: '第9节' },
  { id: 10, startTime: '19:50', endTime: '20:35', displayText: '第10节' },
  { id: 11, startTime: '20:40', endTime: '21:25', displayText: '第11节' },
  { id: 12, startTime: '21:30', endTime: '22:15', displayText: '第12节' }
])

// 课程数据
const courseData = ref<WeeklyScheduleItem[]>([])
// 处理后的课程块数据
const courseBlocks = ref<CourseBlock[]>([])
// 课程详情数据
const weeklySchedule = ref<any[]>([])
const scheduleSummary = ref<any>(null)
const loading = ref(false)
const currentUserType = ref('ADMIN') // 当前用户类型，用于控制表格列显示
const detailDialogVisible = ref(false)
const selectedCourse = ref<any>(null)

// 计算表格最大高度，确保内容完整显示
const tableMaxHeight = computed(() => {
  // 根据课程数量动态计算表格高度
  const baseHeight = 400 // 基础高度
  const rowHeight = 50 // 每行高度
  const headerHeight = 50 // 表头高度
  const minHeight = 300 // 最小高度
  const maxHeight = 600 // 最大高度，避免过度限制页面滚动
  
  if (weeklySchedule.value.length === 0) {
    return minHeight
  }
  
  // 如果课程数量较少，使用较小的最大高度
  if (weeklySchedule.value.length <= 8) {
    const calculatedHeight = baseHeight + (weeklySchedule.value.length * rowHeight) + headerHeight
    return Math.max(calculatedHeight, minHeight)
  }
  
  // 如果课程数量较多，使用固定的最大高度，让页面可以滚动
  return maxHeight
})

// 可用选项
const availableSemesters = ref([
  { value: '0', label: '第一学期' },
  { value: '1', label: '第二学期' }
])

// 计算属性
const uniqueCourses = computed(() => {
  const courses = weeklySchedule.value.map(item => ({
    courseId: item.courseId || item.CourseId || '',
    courseName: item.courseName || item.CourseName || ''
  }))
  const courseMap = new Map()
  courses.forEach(item => {
    if (item.courseId) {
      courseMap.set(item.courseId, item)
    }
  })
  return Array.from(courseMap.values())
})

const uniqueCoursesCount = computed(() => {
  return uniqueCourses.value.length
})

const totalCredits = computed(() => {
  return weeklySchedule.value.reduce((sum, item) => {
    const credits = item.credits || item.Credits || 0
    return sum + credits
  }, 0)
})

const totalStudents = computed(() => {
  return weeklySchedule.value.reduce((sum, item) => {
    const studentCount = item.studentCount || item.StudentCount || 0
    return sum + studentCount
  }, 0)
})

// 移除原有的getDayName函数，替换为：
const getDayName = (dayOfWeek: number) => {
  const days = ['一', '二', '三', '四', '五', '六', '日']
  return days[dayOfWeek - 1] || '未知'
}

// 格式化课程信息
const formatCourseInfo = (course: CourseBlock) => {
  return `${course.courseName} ${course.teacherName} ${course.campus} ${course.building} ${course.roomNumber} 周${getDayName(course.dayOfWeek)}${course.period}节`
}

// 加载课程数据
const loadData = async () => {
  try {
    let response
    if (userType === '3') {
      // 学生
      response = await getStudentSchedule(searchParams.value)
    } else if (userType === '2') {
      // 教师
      response = await getTeacherSchedule(searchParams.value)
    } else {
      // 管理员
      response = await getAdminSchedule(searchParams.value)
    }

    if (response.data.IsSuccess) {
      // 确保Result是对象，包含Schedule和CourseDetails
      if (response.data.Result && typeof response.data.Result === 'object') {
        courseData.value = Array.isArray(response.data.Result.Schedule) ? response.data.Result.Schedule : []
        weeklySchedule.value = Array.isArray(response.data.Result.Schedule) ? response.data.Result.Schedule : []
        scheduleSummary.value = response.data.Result.Summary || null
      } else {
        courseData.value = []
        weeklySchedule.value = []
        scheduleSummary.value = null
      }
      
      processCourseData()
      ElMessage.success('数据加载成功')
    } else {
      ElMessage.error('数据加载失败: ' + response.data.Message)
      courseData.value = [] // 确保设置为空数组
      weeklySchedule.value = [] // 确保设置为空数组
      scheduleSummary.value = null
    }
  } catch (error) {
    console.error('加载课程数据失败:', error)
    ElMessage.error('数据加载失败')
    courseData.value = [] // 确保设置为空数组
    weeklySchedule.value = [] // 确保设置为空数组
    scheduleSummary.value = null
  }
}

// 修改shouldDisplayCourse和getCoursesByTime函数：
const shouldDisplayCourse = (day: number, period: number) => {
  return courseBlocks.value.some(course => {
    return course.dayOfWeek === day && 
           period === course.startPeriod
  })
}

const getCoursesByTime = (day: number, period: number) => {
  return courseBlocks.value.filter(course => {
    return course.dayOfWeek === day && 
           period === course.startPeriod
  })
}

// 处理课程数据，转换为课程块
const processCourseData = () => {
  courseBlocks.value = []
  const processedCourses = new Set() // 用于跟踪已处理的课程
  
  // 确保courseData.value是数组
  if (!Array.isArray(courseData.value)) {
    console.error('courseData.value is not an array:', courseData.value)
    courseData.value = []
    return
  }
  
  courseData.value.forEach(course => {
    // 检查period是否存在
    if (!course.period) {
      console.warn('课程缺少period属性:', course)
      return // 跳过这个课程
    }
    
    // 生成课程唯一标识，防止重复
    const courseKey = `${course.courseId}-${course.className}-${course.dayOfWeek}-${course.period}`
    if (processedCourses.has(courseKey)) {
      return // 跳过已处理的课程
    }
    processedCourses.add(courseKey)
    
    try {
      // 解析节次字符串，如"5-6"或"7"
      const periodParts = course.period.split('-')
      const startPeriod = parseInt(periodParts[0])
      const endPeriod = periodParts.length > 1 ? parseInt(periodParts[1]) : startPeriod
      const duration = endPeriod - startPeriod + 1
      
      // 为课程分配颜色
      const colorIndex = Math.abs(hashCode(course.courseId)) % courseColors.length
      const color = courseColors[colorIndex]
      
      courseBlocks.value.push({
        ...course,
        startPeriod,
        endPeriod,
        duration,
        color,
        displayText: ''
      })
    } catch (error) {
      console.error('处理课程数据出错:', error, course)
    }
  })
}

// 简单的哈希函数用于生成颜色索引
const hashCode = (str: string): number => {
  let hash = 0
  for (let i = 0; i < str.length; i++) {
    hash = ((hash << 5) - hash) + str.charCodeAt(i)
    hash |= 0 // Convert to 32bit integer
  }
  return hash
}

// 课程颜色
const courseColors = [
  '#FF9E80', '#FFAB91', '#FFCC80', '#FFE082', '#E6EE9C', '#C5E1A5', 
  '#A5D6A7', '#80CBC4', '#80DEEA', '#81D4FA', '#90CAF9', '#9FA8DA', 
  '#B39DDB', '#CE93D8', '#F48FB1', '#EF9A9A'
]

// 在getCourseBlockStyle函数中，确保课程块正确定位
const getCourseBlockStyle = (course: CourseBlock, index: number) => {
  const coursesInCell = getCoursesByTime(course.dayOfWeek, course.startPeriod)
  const courseCount = coursesInCell.length
  
  let styles: any = {
    'background-color': course.color,
    'height': `${course.duration * 40 - 4}px`,
    'top': '2px',
    'z-index': courseCount > 1 ? index + 1 : 'auto'
  }
  
  if (courseCount > 1) {
    const width = 95 / courseCount
    const left = index * width + 2.5
    styles.width = `${width}%`
    styles.left = `${left}%`
  } else {
    styles.width = 'calc(95% - 4px)'
    styles.left = '2.5%'
  }
  
  return styles
}

// 显示课程详情
const showCourseDetail = (course: WeeklyScheduleItem) => {
  selectedCourse.value = course
  detailDialogVisible.value = true
}

// 重置搜索条件
const resetSearch = () => {
  searchParams.value = {
    year: '2025',
    semester: '0',
    week: undefined,
    userType: userType,
    userId: userId
  }
  loadData()
}

// 获取星期标签
const getDayLabel = (day: number) => {
  const labels = ['', '周一', '周二', '周三', '周四', '周五', '周六', '周日']
  return labels[day] || ''
}

// 判断是否为当前周
const isCurrentWeek = (course: any) => {
  // 如果后端已经设置了isCurrentWeek字段，优先使用
  if (course.isCurrentWeek !== undefined) {
    return course.isCurrentWeek === true
  }
  
  // 如果后端没有设置，根据周次模式判断
  const weekPattern = course.weekPattern || course.WeekPattern || ''
  const currentWeek = searchParams.value.week || 1
  
  if (!weekPattern) {
    return false
  }
  
  // 解析周次模式，如 "1-16", "1,3,5", "1-8,10-16"
  try {
    // 处理连续的周次范围，如 "1-16"
    if (weekPattern.includes('-')) {
      const parts = weekPattern.split('-')
      if (parts.length === 2) {
        const startWeek = parseInt(parts[0])
        const endWeek = parseInt(parts[1])
        if (!isNaN(startWeek) && !isNaN(endWeek)) {
          return currentWeek >= startWeek && currentWeek <= endWeek
        }
      }
    }
    
    // 处理离散的周次，如 "1,3,5"
    if (weekPattern.includes(',')) {
      const weeks = weekPattern.split(',').map((w: string) => parseInt(w.trim()))
      return weeks.some((w: number) => !isNaN(w) && w === currentWeek)
    }
    
    // 处理单个周次，如 "5"
    const singleWeek = parseInt(weekPattern)
    if (!isNaN(singleWeek)) {
      return singleWeek === currentWeek
    }
    
    return false
  } catch (error) {
    console.error('解析周次模式失败:', error)
    return false
  }
}

// 页面加载时自动查询（如果有默认参数）
onMounted(() => {
  // 可以设置默认的用户ID和类型
  try {
    const storedUserType = sessionStorage.getItem('userType');
    if (storedUserType) {
      // 将数字类型转换为字符串类型
      const numUserType = parseInt(storedUserType);
      if (numUserType === 3) {
        currentUserType.value = 'student';
      } else if (numUserType === 2) {
        currentUserType.value = 'teacher';
      } else {
        currentUserType.value = 'ADMIN';
      }
    }
  } catch (error) {
    console.error('获取用户信息失败:', error);
  }
  
  if (searchParams.value.year) {
    loadData()
  }
})
</script>

<style scoped lang="scss">
.weekly-course-container {
  padding: 20px;
  background-color: #f5f5f5;
  /* 确保页面可以正常滚动 */
  overflow-y: auto;
  /* 设置合适的最小高度，确保内容完整显示 */
  min-height: calc(100vh - 40px);
  /* 确保页面内容可以完整显示 */
  height: auto;
  /* 添加底部内边距，确保有足够空间 */
  padding-bottom: 40px;
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
  font-size: 18px;
}

.filter-controls {
  display: flex;
  gap: 15px;
  align-items: center;
  flex-wrap: wrap;
}

.filter-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-label {
  font-weight: 500;
  color: #666;
  min-width: 70px;
  font-size: 14px;
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
  font-size: 18px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-item {
  text-align: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 8px;
}

.stat-label {
  color: #666;
  font-size: 14px;
}

.schedule-container {
  width: 100%;
  overflow-x: auto;
  flex-shrink: 0;
  margin-bottom: 20px;
}

.schedule-header {
  display: flex;
  border-bottom: 1px solid #ebeef5;
  background-color: #f5f7fa;
  
  .time-header {
    width: 55px;
    min-width: 55px;
    padding: 12px;
    font-weight: bold;
    text-align: center;
    border-right: 1px solid #ebeef5;
  }
  
  .day-header {
    flex: 1;
    min-width: 120px;
    padding: 12px;
    font-weight: bold;
    text-align: center;
    border-right: 1px solid #ebeef5;
    
    &:last-child {
      border-right: none;
    }
  }
}

.schedule-body {
  display: flex;
  height: calc(12 * 40px); /* 12节课，每节40px */
  position: relative;
}

.time-axis {
  width: 80px;
  min-width: 80px;
  background-color: #f5f7fa;
  border-right: 1px solid #ebeef5;
  
  .time-slot {
    height: 39px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-bottom: 1px solid #ebeef5;
    font-size: 12px;
    color: #606266;
    
    &:last-child {
      border-bottom: none;
    }
  }
}

.course-content {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.day-column {
  flex: 1;
  min-width: 120px;
  position: relative;
  border-right: 1px solid #ebeef5;
  
  &:last-child {
    border-right: none;
  }
}

.time-cell {
  height: 39px;
  border-bottom: 1px solid #ebeef5;
  position: relative;
  
  &:last-child {
    border-bottom: none;
  }
  
  .cell-inner {
    position: relative;
    height: 100%;
    width: 100%;
  }
}

.course-block {
  position: absolute;
  border-radius: 4px;
  padding: 4px;
  overflow: hidden;
  cursor: pointer;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12);
  transition: all 0.2s;
  box-sizing: border-box;
  
  &:hover {
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.16);
    transform: translateY(-1px);
  }
  
  .course-info {
    color: #000;
    font-size: 14px;
    line-height: 1.3;
    font-weight: 500;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: normal;
  }
}

.multi-course {
  z-index: 1;
}

.course-detail-section {
  margin-bottom: 20px;
  /* 确保表格容器不会限制页面滚动 */
  overflow: visible;
}

.detail-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  /* 确保卡片内容可以正常显示 */
  overflow: visible;
}

.detail-title {
  margin: 0 0 20px 0;
  color: #333;
  font-size: 18px;
}

/* 课程详细信息表格样式优化 */
.course-detail-table {
  /* 确保表格内容完整显示 */
  overflow: visible;
  /* 表格拉伸至最右侧 */
  width: 100% !important;
}

.course-detail-table .el-table__body-wrapper {
  /* 优化滚动条样式 */
  scrollbar-width: thin;
  scrollbar-color: #c0c4cc #f5f7fa;
  /* 确保表格内容可以正常滚动 */
  overflow-y: auto;
  max-height: 100%;
}

/* 表格整体样式优化 */
.course-detail-table .el-table {
  font-size: 14px;
  line-height: 1.5;
}

/* 表头样式优化 */
.course-detail-table .el-table__header-wrapper th {
  background-color: #fafafa;
  color: #333;
  font-weight: 600;
  font-size: 14px;
  padding: 16px 12px;
  border-bottom: 2px solid #e8e8e8;
}

/* 表格行样式优化 */
.course-detail-table .el-table__row {
  height: auto;
  min-height: 56px;
  transition: background-color 0.2s ease;
}

.course-detail-table .el-table__row:hover {
  background-color: #f5f7fa !important;
}

/* 表格单元格样式优化 */
.course-detail-table .el-table__cell {
  padding: 16px 12px;
  word-break: break-word;
  white-space: normal;
  font-size: 14px;
  line-height: 1.4;
  vertical-align: middle;
}

.course-detail-table .el-table__body-wrapper::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.course-detail-table .el-table__body-wrapper::-webkit-scrollbar-track {
  background: #f5f7fa;
  border-radius: 4px;
}

.course-detail-table .el-table__body-wrapper::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 4px;
}

.course-detail-table .el-table__body-wrapper::-webkit-scrollbar-thumb:hover {
  background: #909399;
}

/* 确保表格行内容完整显示 */
.course-detail-table .el-table__row {
  height: auto;
  min-height: 50px;
}

.course-detail-table .el-table__cell {
  padding: 12px 8px;
  word-break: break-word;
  white-space: normal;
}

/* 上课地点信息样式 */
.location-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
  align-items: center;
  padding: 4px 0;
}

.campus-building {
  font-size: 13px;
  color: #555;
  font-weight: 500;
  line-height: 1.3;
}

.room-number {
  font-size: 15px;
  color: #1890ff;
  font-weight: 600;
  background: #f0f9ff;
  padding: 4px 8px;
  border-radius: 6px;
  border: 1px solid #bae7ff;
  box-shadow: 0 1px 3px rgba(24, 144, 255, 0.1);
}

/* 上课时间信息样式 */
.time-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
  align-items: center;
  padding: 4px 0;
}

.day-label {
  font-size: 13px;
  color: #1890ff;
  font-weight: 600;
  line-height: 1.3;
}

.period-info {
  font-size: 13px;
  color: #555;
  font-weight: 500;
}

.time-range {
  font-size: 11px;
  color: #666;
  background: #f8f9fa;
  padding: 2px 6px;
  border-radius: 4px;
  border: 1px solid #e9ecef;
}

.no-data-section {
  margin-bottom: 20px;
}

.no-data-card {
  background: white;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
}

/* 确保页面底部有足够空间 */
.weekly-course-container::after {
  content: '';
  display: block;
  height: 40px;
  width: 100%;
}

/* 管理员提示样式 */
.admin-notice {
  margin-bottom: 20px;
}

/* 当前周信息提示样式 */
.current-week-info {
  margin-bottom: 20px;
}

/* 课程详情弹窗样式 */
.course-detail-content {
  padding: 20px;
}

.detail-row {
  display: flex;
  margin-bottom: 15px;
  align-items: center;
}

.detail-label {
  font-weight: 600;
  color: #333;
  min-width: 100px;
  margin-right: 15px;
}

.detail-value {
  color: #666;
  flex: 1;
}

@media (max-width: 768px) {
  .filter-controls {
    flex-direction: column;
    align-items: stretch;
  }
  
  .filter-item {
    justify-content: space-between;
  }
  
  .schedule-header {
    flex-direction: column;
    align-items: stretch;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  /* 移动端表格优化 */
  .course-detail-table {
    font-size: 12px;
  }
  
  .course-detail-table .el-table__cell {
    padding: 8px 4px;
  }
  
  /* 调整表格列宽度以适应小屏幕 */
  .course-detail-table .el-table__header-wrapper th {
    padding: 8px 4px;
    font-size: 12px;
  }
}
</style>