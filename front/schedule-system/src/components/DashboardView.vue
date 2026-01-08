<template>
  <div class="dashboard-container">
    <div class="dashboard-grid">
      <!-- 左上：通知公告 -->
      <div class="notice-board">
        <h2 class="section-title">通知公告</h2>
        <div class="notice-list">
          <div v-for="(notice, index) in notices" :key="index" class="notice-item">
            <div class="notice-header">
              <span class="notice-title">{{ notice.title }}</span>
              <span class="notice-date">{{ notice.date }}</span>
            </div>
            <div class="notice-content">{{ notice.content }}</div>
          </div>
        </div>
      </div>
      
      <!-- 右上：快捷访问 -->
      <div class="quick-access">
        <h2 class="section-title">快捷访问</h2>
        <div class="access-grid">
          <div 
            v-for="(item, index) in quickAccessItems" 
            :key="index"
            class="access-item"
            @click="handleAccess(item)">
            <el-icon :size="24" class="access-icon">
              <component :is="item.icon" />
            </el-icon>
            <span class="access-text">{{ item.label }}</span>
          </div>
        </div>
      </div>
      
      <!-- 左下部分容器 -->
      <div class="bottom-left-container">
        <div class="calendar-view">
          <div class="calendar-header">
            <el-button type="text" @click="prevYear">
              <el-icon><d-arrow-left /></el-icon>
            </el-button>
            <el-button type="text" @click="prevMonth">
              <el-icon><arrow-left /></el-icon>
            </el-button>
            <span class="current-date">{{ currentYear }}年{{ currentMonth }}月</span>
            <el-button type="text" @click="nextMonth">
              <el-icon><arrow-right /></el-icon>
            </el-button>
            <el-button type="text" @click="nextYear">
              <el-icon><d-arrow-right /></el-icon>
            </el-button>
          </div>
          <div class="calendar-body">
            <div class="weekdays">
              <div v-for="day in ['日', '一', '二', '三', '四', '五', '六']" :key="day" class="weekday">{{ day }}</div>
            </div>
            <div class="days-grid">
              <div 
                v-for="(day, index) in calendarDays" 
                :key="index"
                :class="['day', { 
                  'today': isToday(day), 
                  'selected': selectedDate === day.fullDate,
                  'other-month': !day.isCurrentMonth,
                  'has-course': hasCourseOnDay(day.fullDate)
                }]"
                @click="selectDate(day)">
                <span>{{ day.date }}</span>
                <div v-if="hasCourseOnDay(day.fullDate)" class="course-dot"></div>
              </div>
            </div>
          </div>
        </div>
        
        <div class="class-schedule">
          <h2 class="section-title">
            {{ selectedDateDisplay }} 课程安排 
            <span v-if="currentWeek" class="week-info">(当前是第{{ currentSemesterDisplay }}学期第{{ currentWeek }}周)</span>
          </h2>
          <div class="schedule-list">
            <div v-for="(course, index) in selectedDayCourses" :key="index" class="schedule-item">
              <div class="course-time">{{ course.time }}</div>
              <div class="course-info">
                <div class="course-name">{{ course.name }}</div>
                <div class="course-location">{{ formatLocation(course.location) }}</div>
              </div>
            </div>
            <div v-if="selectedDayCourses.length === 0" class="no-course">
              今日无课程安排
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右下：智能客服 -->
      <div class="agent-chat">
        <h2 class="section-title">智能客服</h2>
        <div class="chat-container">
          <div class="chat-messages" ref="chatMessagesRef">
            <div v-if="chatMessages.length === 0" class="empty-chat">
              <el-icon class="empty-icon"><ChatDotRound /></el-icon>
              <p class="empty-text">您好！我是智能客服，有什么可以帮助您的吗？</p>
            </div>
            <div 
              v-for="(message, index) in chatMessages" 
              :key="message.id"
              :class="['message-item', message.role]">
              <div class="message-avatar">
                <el-icon v-if="message.role === 'user'"><User /></el-icon>
                <el-icon v-else><Service /></el-icon>
              </div>
              <div class="message-content">
                <div class="message-text">{{ message.content }}</div>
                <div class="message-time">{{ formatTime(message.timestamp) }}</div>
              </div>
            </div>
            <div v-if="isLoading" class="message-item assistant">
              <div class="message-avatar">
                <el-icon><Service /></el-icon>
              </div>
              <div class="message-content">
                <div class="thinking-indicator">
                  <el-icon class="loading-icon"><Loading /></el-icon>
                  <span>正在思考中...</span>
                </div>
              </div>
            </div>
          </div>
          <div class="chat-input-container">
            <el-input
              v-model="inputMessage"
              placeholder="请输入您的问题..."
              class="chat-input"
              @keyup.enter="sendMessage"
              :disabled="isLoading"
              clearable
            >
              <template #suffix>
                <el-button 
                  type="primary" 
                  :icon="Promotion" 
                  circle 
                  size="small"
                  @click="sendMessage"
                  :loading="isLoading"
                  :disabled="!inputMessage.trim() || isLoading"
                />
              </template>
            </el-input>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { 
  ArrowLeft, ArrowRight, DArrowLeft, DArrowRight,
  Calendar, Document, Setting, User, Notebook, 
  QuestionFilled, Bell, Collection, DataBoard, Postcard,
  ChatDotRound, Service, Loading, Promotion} from '@element-plus/icons-vue'
import { getWeeklySchedule, getStudentSchedule, getTeacherSchedule, getAdminSchedule } from '@/api/weeklyCourse/weeklyCourse'
import { getCurrentSemesterInfo, calculateCurrentWeek } from '@/api/yearSemester/yearSemester'
import { WeeklyScheduleItem } from '@/api/weeklyCourse/types'
import { ElMessage } from 'element-plus'
import { sendQuestionToAgent } from '@/api/agentdash/agentdash'
import { ChatMessage } from '@/api/agentdash/types'
import { getMyInformations } from '@/api/myMessage/myMessage'

const router = useRouter()

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

// 通知公告数据
const notices = ref([
  { title: '校园网维护通知', date: '2025-09-11', content: '9月12日0:00-6:00校园网将进行维护，期间网络服务可能中断。' },
  { title: '图书馆寒假开放时间', date: '2025-09-06', content: '开学期间图书馆开放时间为每周一至周日7:00-22:00。' },
  { title: '关于数据库课设答辩的通知', date: '2025-08-30', content: '本学期数据库课设答辩将于2025年9月14日进行，请各位同学做好准备。' },
  { title: '新学期开学通知', date: '2025-08-30', content: '暑假将于2025年9月14日结束，9月15日正式上课。' },
  { title: '教学评估通知', date: '2025-08-25', content: '本学期教学评估已经开始，请各位同学在9月20日前完成评估。' },
])

// 根据用户类型动态生成快捷访问菜单
const quickAccessItems = computed(() => {
  const currentType = userType.value;
  
  // 系统管理员（userType=0）
  if (currentType === 0) {
    return [
      { label: '我的消息', icon: Bell, path: '/mymessage' },
      { label: '用户信息', icon: User, path: '/allinfo' },
      { label: '考试安排', icon: Calendar, path: '/schedule' },
      { label: '学期课表', icon: Collection, path: '/weeklycourse' },
      { label: '一键排课', icon: DataBoard, path: '/schedulecourse' },
      { label: '教室信息', icon: Postcard, path: '/classroom' },
      { label: '系统设置', icon: Setting, path: '/settings' },
      { label: '个人信息', icon: Notebook, path: '/profile' }
    ];
  }
  
  // 教务管理员（userType=1）
  if (currentType === 1) {
    return [
      { label: '我的消息', icon: Bell, path: '/mymessage' },
      { label: '学生信息', icon: Notebook, path: '/studentinfo' },
      { label: '教师信息', icon: User, path: '/teacherinfo' },
      { label: '考试安排', icon: Calendar, path: '/schedule' },
      { label: '学期课表', icon: Collection, path: '/weeklycourse' },
      { label: '教室信息', icon: Postcard, path: '/classroom' },
      { label: '系统设置', icon: Setting, path: '/settings' },
      { label: '个人信息', icon: Notebook, path: '/profile' }
    ];
  }
  
  // 老师（userType=2）
  if (currentType === 2) {
    return [
      { label: '我的消息', icon: Bell, path: '/mymessage' },
      { label: '考试安排', icon: Calendar, path: '/schedule' },
      { label: '我的授课', icon: Collection, path: '/myteaching' },
      { label: '扫码签到', icon: Calendar, path: '/qrcode' },
      { label: '导师查询', icon: User, path: '/advisorquery' },
      { label: '教室信息', icon: Postcard, path: '/classroom' },
      { label: '系统设置', icon: Setting, path: '/settings' },
      { label: '个人信息', icon: Notebook, path: '/profile' }
    ];
  }
  
  // 学生（userType=3）
  if (currentType === 3) {
    return [
      { label: '我的消息', icon: Bell, path: '/mymessage' },
      { label: '考试安排', icon: Calendar, path: '/schedule' },
      { label: '课表信息', icon: Collection, path: '/weeklycourse' },
      { label: '导师查询', icon: User, path: '/advisorquery' },
      { label: '成绩查询', icon: DataBoard, path: '/gradequery' },
      { label: '教室信息', icon: Postcard, path: '/classroom' },
      { label: '系统设置', icon: Setting, path: '/settings' },
      { label: '个人信息', icon: Notebook, path: '/profile' }
    ];
  }
  
  return [];
})

// 智能客服相关
const chatMessages = ref<ChatMessage[]>([])
const inputMessage = ref('')
const isLoading = ref(false)
const chatMessagesRef = ref<HTMLElement | null>(null)
const conversationId = ref<string>('')

// 生成消息ID
const generateMessageId = () => {
  return `msg_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`
}

// 格式化时间
const formatTime = (timestamp: number) => {
  const date = new Date(timestamp)
  const hours = date.getHours().toString().padStart(2, '0')
  const minutes = date.getMinutes().toString().padStart(2, '0')
  return `${hours}:${minutes}`
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (chatMessagesRef.value) {
      chatMessagesRef.value.scrollTop = chatMessagesRef.value.scrollHeight
    }
  })
}

// 检查是否为固定问题并返回答案
const checkFixedQuestions = async (question: string): Promise<string | null> => {
  const normalizedQuestion = question.trim().toLowerCase()
  
  // 1. 检查是否为今天的课程安排问题
  if (normalizedQuestion.includes('今天的课程') || 
      normalizedQuestion.includes('今日课程') || 
      normalizedQuestion.includes('今天课程安排') ||
      normalizedQuestion.includes('今天上什么课') ||
      normalizedQuestion === '我今天的课程安排是什么？' ||
      normalizedQuestion === '今天有什么课') {
    return getTodayCoursesAnswer()
  }
  
  // 2. 检查是否为明天的课程安排问题
  if (normalizedQuestion.includes('明天的课程') || 
      normalizedQuestion.includes('明日课程') || 
      normalizedQuestion.includes('明天课程安排') ||
      normalizedQuestion.includes('明天上什么课') ||
      normalizedQuestion === '我明天的课程安排是什么？' ||
      normalizedQuestion === '明天有什么课') {
    return getTomorrowCoursesAnswer()
  }
  
  // 3. 检查是否为本周课程安排问题
  if (normalizedQuestion.includes('本周的课程') || 
      normalizedQuestion.includes('这周的课程') || 
      normalizedQuestion.includes('本周课程安排') ||
      normalizedQuestion.includes('这周上什么课') ||
      normalizedQuestion === '我本周的课程安排是什么？') {
    return getWeekCoursesAnswer()
  }
  
  // 4. 检查是否为消息查询问题
  if (normalizedQuestion.includes('最近有新消息') || 
      normalizedQuestion.includes('有新消息吗') ||
      normalizedQuestion.includes('最近消息') ||
      normalizedQuestion.includes('查看消息') ||
      normalizedQuestion === '最近有新消息吗？' ||
      normalizedQuestion === '我有新消息吗') {
    return await getRecentMessagesAnswer()
  }
  
  // 5. 检查是否为当前周次问题
  if (normalizedQuestion.includes('当前是第几周') || 
      normalizedQuestion.includes('现在是第几周') || 
      normalizedQuestion.includes('这周是第几周') ||
      normalizedQuestion.includes('第几周了') ||
      normalizedQuestion === '当前周次是多少？') {
    return getCurrentWeekAnswer()
  }
  
  // 6. 检查是否为学期信息问题
  if (normalizedQuestion.includes('当前学期') || 
      normalizedQuestion.includes('现在是哪个学期') || 
      normalizedQuestion.includes('学期信息') ||
      normalizedQuestion.includes('这学期') ||
      normalizedQuestion === '当前是第几学期？') {
    return getSemesterInfoAnswer()
  }
  
  // 7. 检查是否为考试安排问题
  if (normalizedQuestion.includes('考试安排') || 
      normalizedQuestion.includes('考试时间') || 
      normalizedQuestion.includes('什么时候考试') ||
      normalizedQuestion.includes('查看考试') ||
      normalizedQuestion === '如何查看考试安排？' ||
      normalizedQuestion === '我的考试安排') {
    return getExamScheduleAnswer()
  }
  
  // 8. 检查是否为教室信息问题
  if (normalizedQuestion.includes('教室信息') || 
      normalizedQuestion.includes('查看教室') || 
      normalizedQuestion.includes('教室查询') ||
      normalizedQuestion.includes('如何查看教室') ||
      normalizedQuestion === '如何查询教室信息？') {
    return getClassroomInfoAnswer()
  }
  
  // 9. 检查是否为选课问题（学生）
  if ((normalizedQuestion.includes('选课') || 
      normalizedQuestion.includes('如何选课') ||
      normalizedQuestion.includes('怎么选课')) && userType.value === 3) {
    return getSelectCourseAnswer()
  }
  
  // 10. 检查是否为缓考申请问题（学生）
  if ((normalizedQuestion.includes('缓考') || 
      normalizedQuestion.includes('申请缓考') ||
      normalizedQuestion.includes('如何申请缓考')) && userType.value === 3) {
    return getDeferredExamAnswer()
  }
  
  // 11. 检查是否为成绩查询问题（学生）
  if ((normalizedQuestion.includes('成绩') || 
      normalizedQuestion.includes('查看成绩') ||
      normalizedQuestion.includes('成绩查询') ||
      normalizedQuestion.includes('如何查看成绩')) && userType.value === 3) {
    return getGradeQueryAnswer()
  }
  
  // 12. 检查是否为我的授课问题（教师）
  if ((normalizedQuestion.includes('我的授课') || 
      normalizedQuestion.includes('查看授课') ||
      normalizedQuestion.includes('授课信息')) && userType.value === 2) {
    return getMyTeachingAnswer()
  }
  
  // 13. 检查是否为我的监考问题（教师）
  if ((normalizedQuestion.includes('我的监考') || 
      normalizedQuestion.includes('监考安排') ||
      normalizedQuestion.includes('查看监考')) && userType.value === 2) {
    return getMyInvigilateAnswer()
  }
  
  // 14. 检查是否为导师查询问题
  if (normalizedQuestion.includes('导师') || 
      normalizedQuestion.includes('查看导师') ||
      normalizedQuestion.includes('导师信息') ||
      normalizedQuestion.includes('如何查询导师')) {
    return getAdvisorQueryAnswer()
  }
  
  // 15. 检查是否为竞赛信息问题
  if (normalizedQuestion.includes('竞赛') || 
      normalizedQuestion.includes('查看竞赛') ||
      normalizedQuestion.includes('竞赛信息') ||
      normalizedQuestion.includes('如何查看竞赛')) {
    return getCompetitionAnswer()
  }
  
  // 16. 检查是否为课表查询问题
  if (normalizedQuestion.includes('课表') || 
      normalizedQuestion.includes('查看课表') ||
      normalizedQuestion.includes('课程表') ||
      normalizedQuestion.includes('如何查看课表')) {
    return getScheduleAnswer()
  }
  
  // 17. 检查是否为授课申请问题（教师）
  if ((normalizedQuestion.includes('授课申请') || 
      normalizedQuestion.includes('申请授课') ||
      normalizedQuestion.includes('如何申请授课')) && userType.value === 2) {
    return getTeachingApplyAnswer()
  }
  
  // 18. 检查是否为个人信息问题
  if (normalizedQuestion.includes('个人信息') || 
      normalizedQuestion.includes('查看个人信息') ||
      normalizedQuestion.includes('我的信息') ||
      normalizedQuestion.includes('如何查看个人信息')) {
    return getProfileAnswer()
  }
  
  // 19. 检查是否为系统设置问题
  if (normalizedQuestion.includes('系统设置') || 
      normalizedQuestion.includes('设置') ||
      normalizedQuestion.includes('如何设置') ||
      normalizedQuestion === '如何进入系统设置？') {
    return getSettingsAnswer()
  }
  
  // 20. 检查是否为通知公告问题
  if (normalizedQuestion.includes('通知') || 
      normalizedQuestion.includes('公告') ||
      normalizedQuestion.includes('查看通知') ||
      normalizedQuestion.includes('如何查看通知')) {
    return getNoticeAnswer()
  }
  
  // 21. 检查是否为签到问题（教师）
  if ((normalizedQuestion.includes('签到') || 
      normalizedQuestion.includes('扫码签到') ||
      normalizedQuestion.includes('如何签到')) && userType.value === 2) {
    return getSignInAnswer()
  }
  
  // 22. 检查为一键排课问题（管理员）
  if ((normalizedQuestion.includes('排课') || 
      normalizedQuestion.includes('一键排课') ||
      normalizedQuestion.includes('如何排课')) && (userType.value === 0 || userType.value === 1)) {
    return getScheduleCourseAnswer()
  }
  
  return null
}

// 获取今日课程安排的回答
const getTodayCoursesAnswer = (): string => {
  const today = new Date()
  const todayStr = `${today.getFullYear()}-${today.getMonth() + 1}-${today.getDate()}`
  
  // 获取今日课程
  const todayCourses = formattedCourses.value.filter(course => course.date === todayStr)
  
  if (todayCourses.length === 0) {
    return `今天（${today.getMonth() + 1}月${today.getDate()}日）您没有课程安排，可以好好休息一下！`
  }
  
  // 按时间排序
  todayCourses.sort((a, b) => {
    const timeA = a.time.split('-')[0]
    const timeB = b.time.split('-')[0]
    return timeA.localeCompare(timeB)
  })
  
  let answer = `今天（${today.getMonth() + 1}月${today.getDate()}日）您共有${todayCourses.length}门课程：\n\n`
  
  todayCourses.forEach((course, index) => {
    answer += `${index + 1}. ${course.time} - ${course.name}\n`
    answer += `   地点：${course.location}\n`
    if (course.teacher) {
      answer += `   教师：${course.teacher}\n`
    }
    answer += `\n`
  })
  
  return answer.trim()
}

// 获取最近消息的回答
const getRecentMessagesAnswer = async (): Promise<string> => {
  try {
    const response = await getMyInformations(undefined, undefined, 1, 3)
    
    if (response.IsSuccess && response.Result && response.Result.data.length > 0) {
      const messages = response.Result.data
      let answer = `您最近有${messages.length}条消息：\n\n`
      
      messages.forEach((msg, index) => {
        const publishTime = new Date(msg.PublishTime)
        const timeStr = `${publishTime.getFullYear()}-${String(publishTime.getMonth() + 1).padStart(2, '0')}-${String(publishTime.getDate()).padStart(2, '0')} ${String(publishTime.getHours()).padStart(2, '0')}:${String(publishTime.getMinutes()).padStart(2, '0')}`
        
        answer += `${index + 1}. 【${msg.Types}】${msg.Title}\n`
        answer += `   发布时间：${timeStr}\n`
        if (msg.Content) {
          const contentPreview = msg.Content.length > 50 ? msg.Content.substring(0, 50) + '...' : msg.Content
          answer += `   内容：${contentPreview}\n`
        }
        answer += `\n`
      })
      
      return answer.trim()
    } else {
      return '您最近没有新消息。'
    }
  } catch (error) {
    console.error('获取消息失败:', error)
    return '抱歉，获取消息时出现错误，请稍后再试。'
  }
}

// 获取明天课程安排的回答
const getTomorrowCoursesAnswer = (): string => {
  const tomorrow = new Date()
  tomorrow.setDate(tomorrow.getDate() + 1)
  const tomorrowStr = `${tomorrow.getFullYear()}-${tomorrow.getMonth() + 1}-${tomorrow.getDate()}`
  
  // 获取明天课程
  const tomorrowCourses = formattedCourses.value.filter(course => course.date === tomorrowStr)
  
  if (tomorrowCourses.length === 0) {
    return `明天（${tomorrow.getMonth() + 1}月${tomorrow.getDate()}日）您没有课程安排，可以好好休息一下！`
  }
  
  // 按时间排序
  tomorrowCourses.sort((a, b) => {
    const timeA = a.time.split('-')[0]
    const timeB = b.time.split('-')[0]
    return timeA.localeCompare(timeB)
  })
  
  let answer = `明天（${tomorrow.getMonth() + 1}月${tomorrow.getDate()}日）您共有${tomorrowCourses.length}门课程：\n\n`
  
  tomorrowCourses.forEach((course, index) => {
    answer += `${index + 1}. ${course.time} - ${course.name}\n`
    answer += `   地点：${course.location}\n`
    if (course.teacher) {
      answer += `   教师：${course.teacher}\n`
    }
    answer += `\n`
  })
  
  return answer.trim()
}

// 获取本周课程安排的回答
const getWeekCoursesAnswer = (): string => {
  const today = new Date()
  const dayOfWeek = today.getDay() === 0 ? 7 : today.getDay() // 转换为1-7（周一到周日）
  const monday = new Date(today)
  monday.setDate(today.getDate() - (dayOfWeek - 1))
  const sunday = new Date(monday)
  sunday.setDate(monday.getDate() + 6)
  
  // 获取本周所有课程
  const weekCourses: any[] = []
  for (let i = 0; i < 7; i++) {
    const date = new Date(monday)
    date.setDate(monday.getDate() + i)
    const dateStr = `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`
    const dayCourses = formattedCourses.value.filter(course => course.date === dateStr)
    if (dayCourses.length > 0) {
      weekCourses.push({ date: dateStr, dateObj: date, courses: dayCourses })
    }
  }
  
  if (weekCourses.length === 0) {
    return `本周（${monday.getMonth() + 1}月${monday.getDate()}日 - ${sunday.getMonth() + 1}月${sunday.getDate()}日）您没有课程安排。`
  }
  
  let answer = `本周（${monday.getMonth() + 1}月${monday.getDate()}日 - ${sunday.getMonth() + 1}月${sunday.getDate()}日）您的课程安排：\n\n`
  
  weekCourses.forEach(day => {
    const dayName = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'][day.dateObj.getDay()]
    answer += `${dayName}（${day.dateObj.getMonth() + 1}月${day.dateObj.getDate()}日）：\n`
    day.courses.sort((a: any, b: any) => {
      const timeA = a.time.split('-')[0]
      const timeB = b.time.split('-')[0]
      return timeA.localeCompare(timeB)
    })
    day.courses.forEach((course: any, index: number) => {
      answer += `  ${index + 1}. ${course.time} - ${course.name}\n`
      answer += `     地点：${course.location}\n`
    })
    answer += `\n`
  })
  
  return answer.trim()
}

// 获取当前周次的回答
const getCurrentWeekAnswer = (): string => {
  if (currentWeek.value > 0) {
    return `当前是第${currentWeek.value}周，${currentSchoolYear.value}学年第${currentSemesterDisplay.value}学期。`
  } else {
    return '抱歉，无法获取当前周次信息，请稍后再试。'
  }
}

// 获取学期信息的回答
const getSemesterInfoAnswer = (): string => {
  if (currentSchoolYear.value && currentSemester.value !== '') {
    return `当前是${currentSchoolYear.value}学年第${currentSemesterDisplay.value}学期，当前是第${currentWeek.value}周。`
  } else {
    return '抱歉，无法获取学期信息，请稍后再试。'
  }
}

// 获取考试安排的回答
const getExamScheduleAnswer = (): string => {
  return `您可以通过以下方式查看考试安排：\n\n1. 点击左侧菜单栏的"考试信息" -> "考试安排"\n2. 或者在快捷访问中点击"考试安排"图标\n\n在考试安排页面，您可以查看所有考试的时间、地点、课程等信息。`
}

// 获取教室信息的回答
const getClassroomInfoAnswer = (): string => {
  return `您可以通过以下方式查看教室信息：\n\n1. 点击左侧菜单栏的"课程信息"或"教室信息"\n2. 或者在快捷访问中点击"教室信息"图标\n\n在教室信息页面，您可以查询教室的使用情况、容量、类型等信息。`
}

// 获取选课的回答（学生）
const getSelectCourseAnswer = (): string => {
  return `您可以通过以下方式进行选课：\n\n1. 点击左侧菜单栏的"课程信息" -> "学生选课"\n2. 在选课页面浏览可选课程\n3. 选择您想要学习的课程并提交选课申请\n\n请注意选课时间，及时完成选课操作。`
}

// 获取缓考申请的回答（学生）
const getDeferredExamAnswer = (): string => {
  return `您可以通过以下方式申请缓考：\n\n1. 点击左侧菜单栏的"考试信息" -> "申请缓考"\n2. 选择需要申请缓考的考试\n3. 填写缓考原因并提交申请\n\n申请提交后，需要等待教务管理员审核。`
}

// 获取成绩查询的回答（学生）
const getGradeQueryAnswer = (): string => {
  return `您可以通过以下方式查询成绩：\n\n1. 点击左侧菜单栏的"成绩查询"\n2. 或者在快捷访问中点击"成绩查询"图标\n\n在成绩查询页面，您可以查看所有已修课程的成绩信息。`
}

// 获取我的授课的回答（教师）
const getMyTeachingAnswer = (): string => {
  return `您可以通过以下方式查看授课信息：\n\n1. 点击左侧菜单栏的"课程信息" -> "我的授课"\n2. 或者在快捷访问中点击"我的授课"图标\n\n在我的授课页面，您可以查看您所教授的所有课程信息。`
}

// 获取我的监考的回答（教师）
const getMyInvigilateAnswer = (): string => {
  return `您可以通过以下方式查看监考安排：\n\n1. 点击左侧菜单栏的"考试信息" -> "我的监考"\n2. 在我的监考页面，您可以查看所有需要您监考的考试安排\n\n请按时到达指定考场进行监考工作。`
}

// 获取导师查询的回答
const getAdvisorQueryAnswer = (): string => {
  return `您可以通过以下方式查询导师信息：\n\n1. 点击左侧菜单栏的"导师查询"\n2. 或者在快捷访问中点击"导师查询"图标\n\n在导师查询页面，您可以查看导师的基本信息和联系方式。`
}

// 获取竞赛信息的回答
const getCompetitionAnswer = (): string => {
  return `您可以通过以下方式查看竞赛信息：\n\n1. 点击左侧菜单栏的"竞赛信息" -> "竞赛查询"\n2. 在竞赛查询页面，您可以浏览所有可参加的竞赛\n\n如果您是教师，还可以通过"竞赛申请"提交新的竞赛申请。`
}

// 获取课表查询的回答
const getScheduleAnswer = (): string => {
  return `您可以通过以下方式查看课表：\n\n1. 点击左侧菜单栏的"课程信息" -> "每周课表"\n2. 或者在快捷访问中点击"课表信息"或"学期课表"图标\n3. 在首页的日历视图中也可以查看课程安排\n\n课表页面会显示您当前学期的所有课程安排。`
}

// 获取授课申请的回答（教师）
const getTeachingApplyAnswer = (): string => {
  return `您可以通过以下方式申请授课：\n\n1. 点击左侧菜单栏的"课程信息" -> "授课申请"\n2. 填写授课申请信息，包括课程、时间、地点等\n3. 提交申请等待审核\n\n申请提交后，需要等待教务管理员审核通过。`
}

// 获取个人信息的回答
const getProfileAnswer = (): string => {
  return `您可以通过以下方式查看个人信息：\n\n1. 点击左侧菜单栏的"个人信息"\n2. 或者在快捷访问中点击"个人信息"图标\n\n在个人信息页面，您可以查看和修改您的基本信息。`
}

// 获取系统设置的回答
const getSettingsAnswer = (): string => {
  return `您可以通过以下方式进入系统设置：\n\n1. 点击左侧菜单栏的"系统设置"\n2. 或者在快捷访问中点击"系统设置"图标\n\n在系统设置页面，您可以进行系统相关的配置和管理。`
}

// 获取通知公告的回答
const getNoticeAnswer = (): string => {
  return `您可以在首页的"通知公告"区域查看最新的通知信息。\n\n通知公告会显示学校发布的重要通知，包括：\n- 校园网维护通知\n- 图书馆开放时间\n- 考试安排通知\n- 教学评估通知\n- 其他重要公告\n\n请及时查看通知，以免错过重要信息。`
}

// 获取签到的回答（教师）
const getSignInAnswer = (): string => {
  return `您可以通过以下方式进行扫码签到：\n\n1. 点击左侧菜单栏的"课程信息" -> "扫码签到"\n2. 或者在快捷访问中点击"扫码签到"图标\n3. 在签到页面生成签到二维码\n4. 学生扫描二维码即可完成签到\n\n您还可以在"签到列表"中查看学生的签到情况。`
}

// 获取一键排课的回答（管理员）
const getScheduleCourseAnswer = (): string => {
  return `您可以通过以下方式进行一键排课：\n\n1. 点击左侧菜单栏的"课程信息" -> "一键排课"\n2. 或者在快捷访问中点击"一键排课"图标\n3. 选择学年和学期\n4. 设置排课约束条件\n5. 运行排课算法\n\n系统会自动为所有课程安排合适的时间和教室。`
}

// 发送消息
const sendMessage = async () => {
  const question = inputMessage.value.trim()
  if (!question || isLoading.value) return

  // 添加用户消息
  const userMessage: ChatMessage = {
    id: generateMessageId(),
    role: 'user',
    content: question,
    timestamp: Date.now()
  }
  chatMessages.value.push(userMessage)
  inputMessage.value = ''
  scrollToBottom()

  // 检查是否为固定问题
  const fixedAnswer = await checkFixedQuestions(question)
  
  if (fixedAnswer) {
    // 如果是固定问题，直接回复
    const assistantMessage: ChatMessage = {
      id: generateMessageId(),
      role: 'assistant',
      content: fixedAnswer,
      timestamp: Date.now()
    }
    chatMessages.value.push(assistantMessage)
    scrollToBottom()
    return
  }

  // 添加思考中的助手消息
  const thinkingMessage: ChatMessage = {
    id: generateMessageId(),
    role: 'assistant',
    content: '',
    timestamp: Date.now()
  }
  chatMessages.value.push(thinkingMessage)
  scrollToBottom()

  isLoading.value = true

  try {
    const userId = sessionStorage.getItem('userId') || ''
    const response = await sendQuestionToAgent({
      question: question,
      userId: userId,
      conversationId: conversationId.value || undefined
    })

    if (response.data.IsSuccess && response.data.Result) {
      // 更新助手消息
      thinkingMessage.content = response.data.Result.answer
      if (response.data.Result.conversationId) {
        conversationId.value = response.data.Result.conversationId
      }
      scrollToBottom()
    } else {
      thinkingMessage.content = response.data.Message || '抱歉，我暂时无法回答您的问题，请稍后再试。'
      ElMessage.error(response.data.Message || '获取回答失败')
    }
  } catch (error: any) {
    console.error('发送消息失败:', error)
    thinkingMessage.content = '抱歉，网络连接出现问题，请稍后再试。'
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    isLoading.value = false
    scrollToBottom()
  }
}

// 日历相关逻辑
const currentDate = ref(new Date())
const selectedDate = ref('')
const allCourses = ref<WeeklyScheduleItem[]>([])
const formattedCourses = ref<any[]>([])
const currentSemester = ref('')
const currentSemesterDisplay = computed(() => {
  return currentSemester.value === '0' ? '一' : '二'
})
const currentWeek = ref(0)
const semesterStartDate = ref('')
const currentSchoolYear = ref('')

const formatLocation = (location: string) => {
  return location;
}

// 获取当前学期信息和周次
const loadSemesterInfo = async () => {
  try {
    const response = await getCurrentSemesterInfo()
    if (response.data.IsSuccess) {
      // 修复字段名匹配问题 - 使用后端返回的实际字段名
      const result = response.data.Result
      const year = result.Year || result.year
      const semester = result.Semester || result.semester
      const startDate = result.StartTime || result.startDate || result.START_DATE
      
      currentSemester.value = semester
      currentSchoolYear.value = year
      semesterStartDate.value = startDate
      
      // 计算当前周次
      if (semesterStartDate.value) {
        currentWeek.value = calculateCurrentWeek(semesterStartDate.value)
      }
      
      // 加载课程数据
      await loadCourseData(year, semester)
    } else {
      ElMessage.error('获取学期信息失败: ' + response.data.Message)
    }
  } catch (error) {
    console.error('获取学期信息失败:', error)
    ElMessage.error('获取学期信息失败')
  }
}

// 加载课程数据
const loadCourseData = async (year: string, semester: string) => {
  try {
    const userId = sessionStorage.getItem('userId') || ''
    const params = {
      year: year,
      semester: semester,
      userType: userType.value.toString(),
      userId: userId
    }

    console.log('请求参数:', params) // 调试信息

    let response
    if (userType.value === 3) {
      response = await getStudentSchedule(params)
    } else if (userType.value === 2) {
      response = await getTeacherSchedule(params)
    } else {
      response = await getAdminSchedule(params)
    }

    if (response.data.IsSuccess) {
      allCourses.value = response.data.Result?.Schedule || []
      formatCourses()
      
      // 设置默认选中日期为今天
      const today = new Date()
      selectedDate.value = `${today.getFullYear()}-${today.getMonth() + 1}-${today.getDate()}`
    } else {
      ElMessage.error('加载课程数据失败: ' + response.data.Message)
    }
  } catch (error) {
    console.error('加载课程数据失败:', error)
    ElMessage.error('加载课程数据失败')
  }
}

// 格式化课程数据
const formatCourses = () => {
  formattedCourses.value = []
  
  // 如果没有学期开始日期，无法计算具体日期
  if (!semesterStartDate.value) {
    console.error('无法格式化课程：学期开始日期为空')
    return
  }
  
  const startDate = new Date(semesterStartDate.value)
  // 确保开学日期是周一（0=周一，6=周日）
  const startDayOfWeek = startDate.getDay() === 0 ? 6 : startDate.getDay() ;
  if (startDayOfWeek !== 0) {
    // 调整开学日期到周一
    startDate.setDate(startDate.getDate() - startDayOfWeek);
  }
  
  allCourses.value.forEach(course => {
    const dayOfWeek = course.dayOfWeek
    // 使用课程的起始周和结束周，而不是解析周次模式
    const beginWeek =  1
    const endWeek =  17
    
    for (let weekNum = beginWeek; weekNum <= endWeek; weekNum++) {
      const courseDate = new Date(startDate)
      // 正确计算课程日期：开学日期 + (周数-1)*7 + 星期几
      courseDate.setDate(startDate.getDate() + (weekNum - 1) * 7 + dayOfWeek)
      
      const dateStr = `${courseDate.getFullYear()}-${courseDate.getMonth() + 1}-${courseDate.getDate()}`
      const time = `${course.startTime}-${course.endTime}`
      const location = `[${course.period}节] ${course.building}${course.roomNumber} ${course.campus}`
      
      formattedCourses.value.push({
        date: dateStr,
        time: time,
        name: course.courseName,
        location: location,
        teacher: course.teacherName,
        className: course.className
      })
    }
  })
}

// 解析周次模式字符串
const parseWeekPattern = (pattern: string): number[] => {
  if (!pattern) return [1] // 默认第1周
  
  const weeks: number[] = []
  const parts = pattern.split(',')
  
  parts.forEach(part => {
    if (part.includes('-')) {
      // 处理连续周次，如"3-5"
      const [start, end] = part.split('-').map(Number)
      for (let i = start; i <= end; i++) {
        weeks.push(i)
      }
    } else {
      // 处理单个周次
      weeks.push(Number(part))
    }
  })
  
  return weeks.length > 0 ? weeks : [1] // 默认第1周
}

// 检查某天是否有课程
const hasCourseOnDay = (date: string) => {
  return formattedCourses.value.some(course => course.date === date)
}

// 计算选中日期是第几周
const calculateWeekForDate = (dateStr: string) => {
  if (!semesterStartDate.value) return 1
  
  const startDate = new Date(semesterStartDate.value)
  const selectedDate = new Date(dateStr)
  
  // 计算相差的天数
  const diffTime = selectedDate.getTime() - startDate.getTime()
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))+1
  
  // 计算周数
  return Math.ceil(diffDays / 7)
}

const currentYear = computed(() => currentDate.value.getFullYear())
const currentMonth = computed(() => currentDate.value.getMonth() + 1)

const calendarDays = computed(() => {
  const year = currentDate.value.getFullYear()
  const month = currentDate.value.getMonth()
  
  const firstDay = new Date(year, month, 1)
  const lastDay = new Date(year, month + 1, 0)
  const firstDayOfWeek = firstDay.getDay() // 0=周日,1=周一,...,6=周六
  const lastDayOfWeek = lastDay.getDay()
  
  const days = []
  
  // 上个月的最后几天
  const prevMonthLastDay = new Date(year, month, 0).getDate()
  for (let i = firstDayOfWeek - 1; i >= 0; i--) {
    const date = prevMonthLastDay - i
    days.push({
      date,
      fullDate: `${year}-${month}-${date}`,
      isCurrentMonth: false
    })
  }
  
  // 当前月的所有天
  const daysInMonth = lastDay.getDate()
  for (let i = 1; i <= daysInMonth; i++) {
    days.push({
      date: i,
      fullDate: `${year}-${month + 1}-${i}`,
      isCurrentMonth: true
    })
  }
  
  // 下个月的前几天
  const nextMonthDays = 7 - lastDayOfWeek - 1
  for (let i = 1; i <= nextMonthDays; i++) {
    days.push({
      date: i,
      fullDate: `${year}-${month + 2}-${i}`,
      isCurrentMonth: false
    })
  }
  
  return days
})

const selectedDateDisplay = computed(() => {
  if (!selectedDate.value) return '今日'
  const dateObj = new Date(selectedDate.value)
  return `${dateObj.getMonth() + 1}月${dateObj.getDate()}日`
})

const selectedDayCourses = computed(() => {
  if (!selectedDate.value) {
    const today = new Date()
    const todayStr = `${today.getFullYear()}-${today.getMonth() + 1}-${today.getDate()}`
    return formattedCourses.value.filter(course => course.date === todayStr)
  }
  return formattedCourses.value.filter(course => course.date === selectedDate.value)
})

const prevMonth = () => {
  currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() - 1, 1)
}

const nextMonth = () => {
  currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() + 1, 1)
}

const prevYear = () => {
  currentDate.value = new Date(currentDate.value.getFullYear() - 1, currentDate.value.getMonth(), 1)
}

const nextYear = () => {
  currentDate.value = new Date(currentDate.value.getFullYear() + 1, currentDate.value.getMonth(), 1)
}

const isToday = (day: any) => {
  const today = new Date()
  return (
    day.date === today.getDate() &&
    currentDate.value.getMonth() === today.getMonth() &&
    currentDate.value.getFullYear() === today.getFullYear()
  )
}

const selectDate = (day: any) => {
  selectedDate.value = day.fullDate
  // 更新当前周次为选中日期所在的周
  if (semesterStartDate.value) {
    currentWeek.value = calculateWeekForDate(day.fullDate)
  }
}

// 修复路由跳转逻辑：去掉/workbench前缀
const handleAccess = (item: { path: string; label: string }) => {
  try {
    // 直接使用item.path作为路由路径，不再添加/workbench前缀
    router.push(item.path)
    console.log(`成功跳转至「${item.label}」页面，路径：${item.path}`)
  } catch (error) {
    console.error(`跳转「${item.label}」失败：`, error)
    alert(`无法跳转到「${item.label}」页面，请检查路由配置！`)
  }
}

onMounted(() => {
  loadSemesterInfo()
})
</script>

<style scoped>
/* 样式保持不变，与之前相同 */
.dashboard-container {
  padding: 0px 0;
  color:#EFF2F5
}

.dashboard-grid {
  display: grid;
  grid-template-columns: 1150px 500px;
  grid-template-rows: 323.4px 395px;
  gap: 16px;
  align-items: stretch;
}

.dashboard-grid > * {
  box-sizing: border-box;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 16px;
  font-family: "Microsoft YaHei", sans-serif;
  padding-left: 8px;
  border-left: 4px solid #409EFF;
}

.week-info {
  font-size: 14px;
  color: #606266;
  font-weight: normal;
  margin-left: 10px;
}

/* 通知公告样式 */
.notice-board {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 16px 24px 24px;
  grid-column: 1;
  grid-row: 1;
  display: flex;
  flex-direction: column;
  font-family: "Microsoft YaHei", sans-serif;
}

.notice-list {
  overflow-y: auto;
  flex-grow: 1;
  padding-right: 8px;
}

.notice-item {
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
}

.notice-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.notice-title {
  font-weight: bold;
  font-size: 16px;
  color: #303133;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 70%;
}

.notice-date {
  color: #909399;
  font-size: 14px;
}

.notice-content {
  color: #606266;
  font-size: 14px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.quick-access {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 16px 24px 24px;
  grid-column: 2;
  grid-row: 1;
  font-family: "Microsoft YaHei", sans-serif;
}

.access-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.access-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background-color: #f5f7fa;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.access-item:hover {
  background-color: #ecf5ff;
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
}

.access-icon {
  margin-right: 12px;
  color: #409EFF;
}

.access-text {
  font-size: 16px;
  color: #606266;
}

.calendar-header {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.current-date {
  font-size: 18px;
  font-weight: bold;
  margin: 0 20px;
}

.weekdays {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  text-align: center;
  font-weight: bold;
  margin-bottom: 10px;
  color: #a8c7ff;
}

.days-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 8px;
  flex-grow: 1;
}

.day {
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
}

.day:hover {
  background: rgba(64, 158, 255, 0.3);
}

.day.other-month {
  color: #888;
}

.day.today {
  background: rgba(64, 158, 255, 0.5);
  font-weight: bold;
}

.day.selected {
  background: #409EFF;
  font-weight: bold;
}

.day.has-course::after {
  content: '';
  position: absolute;
  bottom: 4px;
  left: 50%;
  transform: translateX(-50%);
  width: 6px;
  height: 6px;
  background-color: #409EFF;
  border-radius: 50%;
}

.schedule-list {
  height: 300px;
  overflow-y: auto;
  padding-right: 8px;
}

.schedule-item {
  display: flex;
  padding: 12px;
  margin-bottom: 8px;
  background-color: #f6f9f9;
  border-radius: 6px;
  gap: 10px;
}

.course-time {
  width: 100px;
  color: #409EFF;
  font-weight: bold;
}

.course-info {
  flex: 1;
}

.course-name {
  font-weight: bold;
  margin-bottom: 6px;
  padding: 4px 8px;
  background-color: #187bde;
  color: white;
  font-size: 12px;
  border-radius: 4px;
  display: inline-block;
}

.course-location {
  color: #000;
  font-size: 14px;
}

.no-course {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #909399;
  font-style: italic;
}

/* 智能客服样式 */
.agent-chat {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 16px 24px;
  grid-column: 2;
  grid-row: 2;
  font-family: "Microsoft YaHei", sans-serif;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  min-height: 0;
  max-height: 100%;
}

.agent-chat .section-title {
  flex-shrink: 0;
  margin-bottom: 12px;
}

.chat-container {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-height: 0;
  overflow: hidden;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  padding-right: 8px;
  padding-bottom: 12px;
  min-height: 0;
  /* 确保滚动条样式 */
  scrollbar-width: thin;
  scrollbar-color: #c0c4cc #f5f7fa;
}

.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.chat-messages::-webkit-scrollbar-track {
  background: #f5f7fa;
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: #909399;
}

.empty-chat {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #909399;
  padding: 40px 20px;
}

.empty-icon {
  font-size: 48px;
  color: #c0c4cc;
  margin-bottom: 16px;
}

.empty-text {
  font-size: 14px;
  color: #909399;
  text-align: center;
}

.message-item {
  display: flex;
  margin-bottom: 16px;
  animation: fadeIn 0.3s ease-in;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.message-item.user {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin: 0 8px;
}

.message-item.user .message-avatar {
  background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);
  color: white;
}

.message-item.assistant .message-avatar {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  color: white;
}

.message-content {
  max-width: 70%;
  display: flex;
  flex-direction: column;
}

.message-item.user .message-content {
  align-items: flex-end;
}

.message-item.assistant .message-content {
  align-items: flex-start;
}

.message-text {
  padding: 10px 14px;
  border-radius: 8px;
  font-size: 14px;
  line-height: 1.6;
  word-wrap: break-word;
  white-space: pre-wrap;
}

.message-item.user .message-text {
  background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);
  color: white;
  border-bottom-right-radius: 4px;
}

.message-item.assistant .message-text {
  background: #f5f7fa;
  color: #303133;
  border-bottom-left-radius: 4px;
}

.message-time {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  padding: 0 4px;
}

.thinking-indicator {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  background: #f5f7fa;
  border-radius: 8px;
  font-size: 13px;
  color: #909399;
  margin-bottom: 8px;
}

.loading-icon {
  margin-right: 6px;
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.chat-input-container {
  flex-shrink: 0;
  padding-top: 12px;
  border-top: 1px solid #ebeef5;
  background: #fff;
  position: relative;
  z-index: 10;
}

.chat-input {
  width: 100%;
}

:deep(.chat-input .el-input__wrapper) {
  border-radius: 20px;
  padding-right: 8px;
}

:deep(.chat-input .el-input__inner) {
  padding-left: 16px;
  padding-right: 8px;
}

.bottom-left-container {
  grid-column: 1; 
  grid-row: 2;    
  display: flex;
  gap: 16px;
}

.calendar-view {
  flex: 1;
  background: #F0F6FD;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 16px;
  color: #000000;
  font-family: "Microsoft YaHei", sans-serif;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.class-schedule {
  flex: 1; 
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 16px;
  font-family: "Microsoft YaHei", sans-serif;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.calendar-body {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}
</style>