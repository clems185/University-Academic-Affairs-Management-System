<template>
  <div class="profile-container">
    <!-- 页面头部：标题 + 刷新按钮 -->
    <div class="profile-header">
      <h1>
        <el-icon :size="32" class="header-icon">
          <UserSolid />
        </el-icon>
        个人信息中心
      </h1>
      <el-button 
        type="primary" 
        class="refresh-btn"
        @click="refreshProfile"
        :loading="loading"
      >
        <el-icon><Refresh /></el-icon>
        刷新信息
      </el-button>
    </div>

    <!-- 主体内容：侧边栏 + 主信息区 -->
    <div class="profile-content">
      <!-- 侧边栏：用户头像 + 基础统计 -->
      <div class="profile-sidebar">
        <div class="user-card">
          <!-- 用户头像（修改：教师也可编辑，条件改为包含教师类型2） -->
          <div class="avatar-container" @click="handleAvatarClick">
            <img 
              v-if="userInfo.avatar"
              :src="getAvatarUrl" 
              class="user-avatar" 
              alt="用户头像"
              @error="handleAvatarError"
            >
            <el-icon v-else class="default-avatar-icon">
              <User />
            </el-icon>
            <!-- 头像编辑图标（修改：教师/学生都显示，userType为2或3） -->
            <div v-if="['2','3'].includes(userType)" class="edit-avatar-icon">
              <el-icon><EditPen /></el-icon>
            </div>
          </div>

          <!-- 用户名 + ID（从接口/缓存获取） -->
          <h2 class="user-name">{{ userInfo.name || '未命名用户' }}</h2>
          <div class="user-id">ID: {{ userInfo.id || '未知ID' }}</div>

          <!-- 用户类型标签（条件渲染） -->
          <div class="user-type">
            <el-tag :type="userType === '3' ? 'success' : 'primary'">
              {{ userType === '3' ? '学生' : userType === '2' ? '教师' : '其他' }}
            </el-tag>
          </div>

          <!-- 学生专属统计卡片 -->
          <div class="stats-grid" v-if="userType === '3'">
            <div class="stat-item">
              <div class="stat-value">{{ studentStats.gpa || '0.0' }}</div>
              <div class="stat-label">平均绩点</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ studentStats.currentSemester || '1' }}</div>
              <div class="stat-label">当前学期</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ studentStats.attemptedCredits || '0' }}</div>
              <div class="stat-label">已修学分</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ studentStats.earnedCredit || '0' }}</div>
              <div class="stat-label">实修学分</div>
            </div>
          </div>

          <!-- 教师专属统计卡片 -->
          <div class="stats-grid" v-else-if="userType === '2'">
            <div class="stat-item">
              <div class="stat-value">{{ teacherStats.courseCount || '0' }}</div>
              <div class="stat-label">当前授课</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ teacherStats.studentCount || '0' }}</div>
              <div class="stat-label">指导学生</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ teacherStats.workYear || '0' }}</div>
              <div class="stat-label">教龄</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ teacherStats.title || '未设置' }}</div>
              <div class="stat-label">职称</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 主信息区：分模块显示详细信息 -->
      <div class="profile-main">
        <!-- 1. 基本信息模块 -->
        <div class="info-section">
          <div class="section-header">
            <el-icon :size="20" class="section-icon">
              <User />
            </el-icon>
            <h3 class="section-title">基本信息</h3>
          </div>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><User /></el-icon>
                姓名
              </div>
              <div class="info-value">{{ userInfo.name || '未设置' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><UserFilled /></el-icon>
                昵称
              </div>
              <div class="info-value">{{ userInfo.nickName || '未设置' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><Message /></el-icon>
                邮箱
              </div>
              <div class="info-value">{{ userInfo.email || '未设置' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><Phone /></el-icon>
                电话
              </div>
              <div class="info-value">{{ userInfo.telephone || '未设置' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><UserRole /></el-icon>
                用户类型
              </div>
              <div class="info-value">
                <el-tag :type="userType === '3' ? 'success' : 'primary'">
                  {{ userType === '3' ? '学生' : userType === '2' ? '教师' : '其他' }}
                </el-tag>
              </div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><School /></el-icon>
                专业/院系
              </div>
              <div class="info-value">{{ userInfo.majorName || '未设置' }}</div>
            </div>
          </div>
        </div>

        <!-- 2. 学生学业信息模块 -->
        <div class="info-section" v-if="userType === '3'">
          <div class="section-header">
            <el-icon :size="20" class="section-icon">
              <Notebook />
            </el-icon>
            <h3 class="section-title">学业信息</h3>
          </div>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><Calendar /></el-icon>
                入学年份
              </div>
              <div class="info-value">{{ studentStats.startYear ? `${studentStats.startYear}年9月` : '未设置' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><Calendar /></el-icon>
                预计毕业年份
              </div>
              <div class="info-value">{{ studentStats.endYear ? `${studentStats.endYear}年6月` : '未设置' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><Star /></el-icon>
                平均绩点
              </div>
              <div class="info-value">{{ studentStats.gpa ? `${studentStats.gpa}/5.0` : '0.0/5.0' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><Book /></el-icon>
                已修学分
              </div>
              <div class="info-value">{{ studentStats.earnedCredit ? `${studentStats.earnedCredit}学分` : '0学分' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><BookMarked /></el-icon>
                总学分要求
              </div>
              <div class="info-value">{{ studentStats.totalCredit ? `${studentStats.totalCredit}学分` : '0学分' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><Trophy /></el-icon>
                学业状态
              </div>
              <div class="info-value">
                <el-tag type="success">在读</el-tag>
              </div>
            </div>
          </div>
        </div>

        <!-- 3. 教师工作信息模块 -->
        <div class="info-section" v-else-if="userType === '2'">
          <div class="section-header">
            <el-icon :size="20" class="section-icon">
              <Briefcase />
            </el-icon>
            <h3 class="section-title">工作信息</h3>
          </div>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><Calendar /></el-icon>
                入职年份
              </div>
              <div class="info-value">{{ teacherStats.startYear ? `${teacherStats.startYear}年` : '未设置' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><Clock /></el-icon>
                教龄
              </div>
              <div class="info-value">{{ teacherStats.workYear ? `${teacherStats.workYear}年` : '0年' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><Microphone /></el-icon>
                当前授课
              </div>
              <div class="info-value">{{ teacherStats.courseCount ? `${teacherStats.courseCount}门` : '0门' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><Users /></el-icon>
                指导学生
              </div>
              <div class="info-value">{{ teacherStats.studentCount ? `${teacherStats.studentCount}人` : '0人' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><Star /></el-icon>
                教学评分
              </div>
              <div class="info-value">{{ teacherStats.evaluation ? `${teacherStats.evaluation}/5.0` : '0.0/5.0' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <el-icon :size="14" class="label-icon"><Briefcase /></el-icon>
                职称
              </div>
              <div class="info-value">{{ teacherStats.title || '未设置' }}</div>
            </div>
          </div>
        </div>

        <!-- 4. 个人简介模块（修改：教师也显示编辑按钮，条件改为包含2和3） -->
        <div class="info-section">
          <div class="section-header">
            <el-icon :size="20" class="section-icon">
              <Edit />
            </el-icon>
            <h3 class="section-title">个人简介</h3>
            <!-- 编辑个人简介按钮（修改：教师/学生都显示） -->
            <el-button 
              v-if="['2','3'].includes(userType)"
              type="text" 
              class="edit-intro-btn"
              @click="introDialogVisible = true"
            >
              <el-icon :size="16"><EditPen /></el-icon> 编辑
            </el-button>
          </div>
          <div class="info-item full-width">
            <div class="info-value intro-content">
              {{ userInfo.information || '未填写个人简介，可在编辑模式下补充个人经历、兴趣爱好、专业特长等信息。' }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 头像编辑弹窗（教师/学生共用，无需修改） -->
    <el-dialog v-model="avatarDialogVisible" title="修改头像" width="500px">
      <div class="avatar-edit-container">
        <div class="current-avatar">
          <h4>当前头像</h4>
          <img 
            :src="getAvatarUrl" 
            class="preview-avatar" 
            alt="当前头像"
            v-if="userInfo.avatar"
          >
          <el-icon v-else class="default-avatar-icon preview-avatar">
            <User />
          </el-icon>
        </div>
        
        <div class="new-avatar">
          <h4>新头像</h4>
          <el-upload
            class="avatar-uploader"
            action=""
            :show-file-list="false"
            :on-change="handleAvatarUpload"
            accept="image/*"
            :auto-upload="false"
          >
            <img v-if="newAvatarUrl" :src="newAvatarUrl" class="preview-avatar" alt="新头像">
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </div>
        
        <div class="apply-reason">
          <el-form-item label="修改原因" required>
            <el-input
              type="textarea"
              v-model="avatarApplyReason"
              placeholder="请输入修改头像的原因"
              :rows="3"
            ></el-input>
          </el-form-item>
        </div>
      </div>
      <template #footer>
        <el-button @click="avatarDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAvatarUpdate">提交申请</el-button>
      </template>
    </el-dialog>

    <!-- 个人简介编辑弹窗（教师/学生共用，无需修改） -->
    <el-dialog v-model="introDialogVisible" title="修改个人简介" width="600px">
      <div class="intro-edit-container">
        <div class="current-intro">
          <h4>当前简介</h4>
          <div class="intro-content">
            {{ userInfo.information || '未填写个人简介' }}
          </div>
        </div>
        
        <div class="new-intro">
          <h4>新简介</h4>
          <el-input
            type="textarea"
            v-model="newIntroduction"
            placeholder="请输入新的个人简介"
            :rows="6"
          ></el-input>
        </div>
        
        <div class="apply-reason">
          <el-form-item label="修改原因" required>
            <el-input
              type="textarea"
              v-model="introApplyReason"
              placeholder="请输入修改个人简介的原因"
              :rows="3"
            ></el-input>
          </el-form-item>
        </div>
      </div>
      <template #footer>
        <el-button @click="introDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitIntroUpdate">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
// 1. 导入Vue核心API
import { ref, computed, onMounted } from 'vue'
// 2. 导入Element Plus组件和图标
import { ElMessage, ElTag, ElButton, ElIcon, ElDialog, ElUpload, ElFormItem, ElInput } from 'element-plus'
import { 
  Refresh, User, UserFilled, Message, Phone, School, 
  Notebook, Calendar, Star, Trophy, Briefcase, Clock, 
  Microphone, Edit, Plus, EditPen
} from '@element-plus/icons-vue'
// 3. 导入API
import { getProfileInfo, submitInfoUpdateApply } from '@/api/userInformation/index' 

// 4. 类型定义
interface UserInfo {
  id: string
  name: string
  nickName: string
  email: string
  telephone: string
  majorName: string
  information: string
  avatar?: string | null 
}
interface StudentStats {
  gpa: string
  earnedCredit: string
  attemptedCredits: string
  totalCredit: string
  currentSemester: string
  startYear: string
  endYear: string
  attendanceRate: string
}
interface TeacherStats {
  courseCount: string 
  studentCount: string 
  workYear: string
  startYear: string
  evaluation: string 
  title: string 
}

// 5. 状态管理
const loading = ref(false) 
const userType = ref(sessionStorage.getItem('userType') || '') 
const userInfo = ref<UserInfo>({
  id: '',
  name: '',
  nickName: '',
  email: '',
  telephone: '',
  majorName: '',
  information: '',
  avatar: null
})
const studentStats = ref<StudentStats>({
  gpa: '3.8',
  earnedCredit: '128',
  attemptedCredits: '128',
  totalCredit: '160',
  currentSemester: '4',
  startYear: '2023',
  endYear: '2027',
  attendanceRate: '92'
})
const teacherStats = ref<TeacherStats>({
  courseCount: '0',
  studentCount: '0',
  workYear: '0',
  startYear: '0',
  evaluation: '4.8',
  title: ''
})

// 编辑相关状态（无需修改，教师/学生共用）
const avatarDialogVisible = ref(false) 
const introDialogVisible = ref(false) 
const newAvatarUrl = ref('') 
const newIntroduction = ref('') 
const avatarApplyReason = ref('') 
const introApplyReason = ref('') 

// 6. 头像处理：计算属性
const getAvatarUrl = computed(() => {
  if (userInfo.value.avatar) {
    return `data:image/jpeg;base64,${userInfo.value.avatar}`
  }
  return ''
})

// 7. 处理头像上传（无需修改）
const handleAvatarUpload = (rawFile: any) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    newAvatarUrl.value = e.target?.result as string
  }
  reader.readAsDataURL(rawFile.raw)
}

// 8. 头像错误处理（无需修改）
const handleAvatarError = () => {
  // 错误逻辑保持不变
}

// 9. 头像点击事件（修改：教师/学生都可触发，判断条件包含2和3）
const handleAvatarClick = () => {
  if (['2','3'].includes(userType.value)) {
    avatarDialogVisible.value = true;
  }
};

// 10. 提交头像更新申请（无需修改，后端自动处理用户类型）
const submitAvatarUpdate = async () => {
  if (!newAvatarUrl.value) {
    ElMessage.warning('请选择新头像')
    return
  }
  if (!avatarApplyReason.value.trim()) {
    ElMessage.warning('请输入修改原因')
    return
  }

  try {
    const userId = sessionStorage.getItem('userId') || ''
    if (!userId || !userType.value) {
      ElMessage.error('用户信息不完整')
      return
    }
    const base64Data = newAvatarUrl.value.split(',')[1]
    
    await submitInfoUpdateApply({
      userId,
      userType: userType.value,
      newPhoto: base64Data,
      information: avatarApplyReason.value
    })
    
    ElMessage.success('头像修改申请已提交，等待管理员审核')
    avatarDialogVisible.value = false
    newAvatarUrl.value = ''
    avatarApplyReason.value = ''
  } catch (error: any) {
    ElMessage.error(`提交失败: ${error.message || '未知错误'}`)
  }
}

// 11. 提交个人简介更新申请（无需修改，后端自动处理用户类型）
const submitIntroUpdate = async () => {
  if (!newIntroduction.value.trim()) {
    ElMessage.warning('请输入新的个人简介')
    return
  }
  if (!introApplyReason.value.trim()) {
    ElMessage.warning('请输入修改原因')
    return
  }

  try {
    const userId = sessionStorage.getItem('userId') || ''
    if (!userId || !userType.value) {
      ElMessage.error('用户信息不完整')
      return
    }
    
    await submitInfoUpdateApply({
      userId,
      userType: userType.value,
      newProfile: newIntroduction.value,
      information: introApplyReason.value
    })
    
    ElMessage.success('个人简介修改申请已提交，等待管理员审核')
    introDialogVisible.value = false
    newIntroduction.value = ''
    introApplyReason.value = ''
    refreshProfile()
  } catch (error: any) {
    ElMessage.error(`提交失败: ${error.message || '未知错误'}`)
  }
}

// 12. 核心方法：刷新个人信息（无需修改）
const refreshProfile = async () => {
  loading.value = true
  try {
    const userId = sessionStorage.getItem('userId') || ''
    if (!userId || !userType.value) {
      ElMessage.error('用户信息不完整，请重新登录')
      return
    }

    const response = await getProfileInfo(userId, userType.value)
    
    userInfo.value = {
      id: response.Id,
      name: response.Name,
      nickName: response.NickName || '',
      email: response.Email || '',
      telephone: response.Telephone || '',
      majorName: response.MajorName || '',
      information: response.Information || '',
      avatar: response.Avatar || null 
    }

    if (userType.value === '2') {
      teacherStats.value = {
        courseCount: response.CourseCount?.toString() || '0',
        studentCount: response.StudentCount?.toString() || '0',
        workYear: response.WorkYear?.toString() || '0',
        startYear: response.StartYear?.toString() || '0',
        evaluation: '4.8',
        title: response.Title || '未设置'
      }
    } else if (userType.value === '3') {
      if (response.StartYear) studentStats.value.startYear = response.StartYear.toString()
      if (response.EndYear) studentStats.value.endYear = response.EndYear.toString()
      if (response.Gpa) studentStats.value.gpa = response.Gpa.toString()
      if (response.EarnedCredit) studentStats.value.earnedCredit = response.EarnedCredit.toString()
      if (response.AttemptedCredit) studentStats.value.attemptedCredits = response.AttemptedCredit.toString()
      newIntroduction.value = userInfo.value.information || ''
    }

    ElMessage.success('个人信息已刷新至最新')
  } catch (error: any) {
    ElMessage.error(`刷新失败: ${error.message || '未知错误'}`)
  } finally {
    loading.value = false
  }
}

// 13. 页面挂载初始化（无需修改）
onMounted(() => {
  refreshProfile()
})
</script>

<style scoped>
/* 原有样式保持不变，无需额外修改 */
.edit-avatar-icon {
  position: absolute;
  bottom: 0;
  right: 0;
  background-color: #409EFF;
  color: white;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.edit-intro-btn {
  margin-left: auto;
  color: #409EFF;
  padding: 5px 10px;
}

.avatar-edit-container, .intro-edit-container {
  padding: 20px 0;
}

.current-avatar, .new-avatar, .current-intro, .new-intro {
  margin-bottom: 20px;
}

.preview-avatar {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  margin-top: 10px;
}

.avatar-uploader {
  border: 1px dashed #ccc;
  border-radius: 50%;
  width: 150px;
  height: 150px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  overflow: hidden;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.intro-content {
  padding: 10px 14px;
  background: #f9fafb;
  border-radius: 8px;
  min-height: 80px;
  margin-top: 10px;
  white-space: pre-wrap;
  line-height: 1.6;
}

.apply-reason {
  margin-top: 20px;
}

/* 原有样式继续保留 */
.profile-container {
  max-width: 1500px;
  margin: 0 auto;
  padding: 20px;
  overflow-x: hidden;
  width: 100%;
  min-height: calc(100vh - 64px);
  background: transparent !important;
  color: #303133;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  height: 1450px;
}

.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  background: linear-gradient(135deg, #2a7ed2 0%, #409EFF 100%);
  padding: 25px 30px;
  border-radius: 16px;
  color: white;
  box-shadow: 0 10px 30px rgba(64, 158, 255, 0.2);
}
.profile-header h1 {
  font-size: 28px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 12px;
}
.refresh-btn {
  border-radius: 10px;
  padding: 10px 20px;
  font-weight: 500;
}

.profile-content {
  display: grid;
  grid-template-columns: 300px 1fr;
  gap: 24px;
}

.profile-sidebar {
  background: white;
  border-radius: 16px;
  padding: 25px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  height: fit-content;
}
.user-card {
  text-align: center;
  padding: 20px 0;
}
.avatar-container {
  position: relative;
  width: 100px;
  height: 100px;
  margin: 0 auto 15px;
}
.default-avatar-icon {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background-color: #e6f7ff;
  color: #409EFF;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 50px;
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.3);
}
.user-avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  border: 4px solid #e6f7ff;
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.3);
  object-fit: cover;
}
.user-name {
  font-size: 20px;
  font-weight: 600;
  margin: 10px 0 5px;
}
.user-id {
  color: #909399;
  font-size: 14px;
  margin-bottom: 15px;
}
.user-type {
  display: inline-block;
  margin-bottom: 25px;
}

.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
}
.stat-item {
  text-align: center;
  padding: 15px;
  border-radius: 12px;
  background: #f9fafb;
  transition: background 0.3s;
}
.stat-item:hover {
  background: #ecf5ff;
}
.stat-value {
  font-size: 20px;
  font-weight: 600;
  color: #2a7ed2;
}
.stat-label {
  font-size: 13px;
  color: #909399;
  margin-top: 5px;
}

.profile-main {
  display: flex;
  flex-direction: column;
  gap: 24px;
  flex-grow: 1;
  padding-bottom: 20px;
}
.info-section {
  background: white;
  border-radius: 16px;
  padding: 25px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 25px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
  gap: 10px;
}
.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}
.info-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.info-label {
  font-size: 13px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 6px;
}
.info-value {
  font-size: 15px;
  font-weight: 500;
  color: #303133;
  padding: 10px 14px;
  background: #f9fafb;
  border-radius: 8px;
  min-height: 42px;
  display: flex;
  align-items: center;
}

.full-width {
  grid-column: 1 / -1;
}

@media (max-width: 992px) {
  .profile-content {
    grid-template-columns: 1fr;
  }
  .profile-header {
    flex-direction: column;
    text-align: center;
    gap: 20px;
  }
  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>