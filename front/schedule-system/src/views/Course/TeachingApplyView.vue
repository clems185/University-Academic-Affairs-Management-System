<template>
  <div>
    <!-- 标签页切换 -->
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <!-- 申请课程标签页 -->
      <el-tab-pane label="申请授课" name="apply">
        <!-- 搜索区域 -->
        <div style="margin-bottom: 20px;">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input 
                v-model="courseSearchForm.courseName" 
                placeholder="请输入课程名称" 
                clearable />
            </el-col>
            <el-col :span="6">
              <el-input 
                v-model="courseSearchForm.courseId" 
                placeholder="请输入课程编号" 
                clearable />
            </el-col>
            <el-col :span="6">
              <el-select 
                v-model="courseSearchForm.credits" 
                placeholder="请选择学分" 
                clearable>
                <el-option label="1学分" :value="1" />
                <el-option label="2学分" :value="2" />
                <el-option label="3学分" :value="3" />
                <el-option label="4学分" :value="4" />
                <el-option label="5学分" :value="5" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="handleCourseSearch">搜索</el-button>
              <el-button @click="handleCourseReset">重置</el-button>
            </el-col>
          </el-row>
        </div>

        <!-- 操作按钮 -->
        <div style="margin-bottom: 20px;">
          <el-button 
            type="primary" 
            @click="fetchCourseList"
            :loading="courseLoading">
            查看课程
          </el-button>
          <el-button 
            type="success" 
            @click="handleBatchApply" 
            :disabled="selectedCourses.length === 0"
            style="margin-left: 10px;">
            批量申请
          </el-button>
        </div>

        <!-- 课程表格 -->
        <el-table 
          :data="filteredCourseData" 
          stripe 
          style="width: 100%" 
          v-loading="courseLoading"
          @selection-change="handleCourseSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="CourseId" label="课程编号" width="120" />
          <el-table-column prop="Name" label="课程名称" width="180" />
          <el-table-column prop="Credits" label="学分" width="80" />
          <el-table-column prop="Information" label="课程信息" />
          <el-table-column prop="CourseBeginWeek" label="开始周" width="80" />
          <el-table-column prop="CourseEndWeek" label="结束周" width="80" />
          <el-table-column label="是否选修" width="80">
            <template #default="scope">
              <el-tag :type="scope.row.IsSelect === 'Y' ? 'success' : 'info'">
                {{ scope.row.IsSelect === 'Y' ? '选修' : '必修' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="是否有考试" width="90">
            <template #default="scope">
              <el-tag :type="scope.row.IsExam === 'Y' ? 'warning' : 'info'">
                {{ scope.row.IsExam === 'Y' ? '有考试' : '无考试' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160">
            <template #default="scope">
              <el-button 
                size="small" 
                type="primary" 
                @click="handleApply(scope.row)"
                :disabled="isAlreadyApplied(scope.row.CourseId)">
                {{ isAlreadyApplied(scope.row.CourseId) ? '已申请' : '申请教授' }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 申请记录标签页 -->
      <el-tab-pane label="申请记录" name="records">
        <!-- 申请记录搜索区域 -->
        <div style="margin-bottom: 20px;">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input 
                v-model="applySearchForm.ApplyId" 
                placeholder="请输入申请ID" 
                clearable />
            </el-col>
            <el-col :span="6">
              <el-input 
                v-model="applySearchForm.CourseId" 
                placeholder="请输入课程编号" 
                clearable />
            </el-col>
            <el-col :span="6">
              <el-select 
                v-model="applySearchForm.State" 
                placeholder="请选择状态" 
                clearable>
                <el-option label="处理中" value="P" />
                <el-option label="已同意" value="Y" />
                <el-option label="已拒绝" value="N" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="handleApplySearch">搜索</el-button>
              <el-button @click="handleApplyReset">重置</el-button>
            </el-col>
          </el-row>
        </div>

        <!-- 申请记录操作按钮 -->
        <div style="margin-bottom: 20px;">
          <el-button 
            type="primary" 
            @click="fetchApplyList"
            :loading="applyLoading">
            刷新记录
          </el-button>
          <el-button 
            type="warning" 
            @click="handleBatchWithdraw" 
            :disabled="selectedApplies.length === 0"
            style="margin-left: 10px;">
            批量撤回
          </el-button>
        </div>

        <!-- 申请记录表格 -->
        <el-table 
          :data="applyTableData" 
          stripe 
          style="width: 100%" 
          v-loading="applyLoading"
          @selection-change="handleApplySelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="ApplyId" label="申请ID" width="120" />
          <el-table-column prop="CourseId" label="课程编号" width="120" />
          <el-table-column prop="CourseName" label="课程名称" width="150" />
          <el-table-column prop="Information" label="申请理由" />
          <el-table-column prop="ApplyTime" label="申请时间" width="150">
            <template #default="scope">
              {{ formatDateTime(scope.row.ApplyTime) }}
            </template>
          </el-table-column>
          <el-table-column label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getStateTagType(scope.row.State)">
                {{ getStateText(scope.row.State) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="ReviewComments" label="审核意见" width="150" />
          <el-table-column prop="ReviewTime" label="审核时间" width="150">
            <template #default="scope">
              {{ scope.row.ReviewTime ? formatDateTime(scope.row.ReviewTime) : '-' }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button 
                v-if="scope.row.State === 'P'"
                size="small" 
                type="warning" 
                @click="handleWithdraw(scope.row)">
                撤回
              </el-button>
              <span v-else>-</span>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 申请确认对话框 -->
    <el-dialog
      v-model="applyDialogVisible"
      title="申请教授课程"
      width="500px">
      <div v-if="selectedCourse">
        <p><strong>课程编号：</strong>{{ selectedCourse.CourseId }}</p>
        <p><strong>课程名称：</strong>{{ selectedCourse.Name }}</p>
        <p><strong>学分：</strong>{{ selectedCourse.Credits }}</p>
        <p><strong>课程信息：</strong>{{ selectedCourse.Information }}</p>
        <p><strong>授课周次：</strong>第{{ selectedCourse.CourseBeginWeek }}周 - 第{{ selectedCourse.CourseEndWeek }}周</p>
        
        <el-form :model="applyForm" :rules="applyRules" ref="applyFormRef" label-width="100px">
          <el-form-item label="申请理由" prop="reason">
            <el-input
              v-model="applyForm.reason"
              type="textarea"
              :rows="4"
              placeholder="请输入申请教授该课程的理由"
            />
          </el-form-item>
        </el-form>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="applyDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmApply" :loading="submitLoading">
            确认申请
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  getCourseList, 
  submitTeachingApply, 
  getTeachingApplyList, 
  withdrawTeachingApply,
  batchSubmitTeachingApply 
} from '@/api/teachingApply/teachingApply'
import { 
  CourseInfo, 
  TeachingApplySearchParams, 
  TeachingApplyItem,
  SubmitTeachingApplyParams 
} from '@/api/teachingApply/types'

// 标签页状态
const activeTab = ref('apply')

// 课程相关数据
const courseTableData = ref<CourseInfo[]>([])
const courseLoading = ref(false)
const selectedCourses = ref<CourseInfo[]>([])

// 申请记录相关数据
const applyTableData = ref<TeachingApplyItem[]>([])
const applyLoading = ref(false)
const selectedApplies = ref<TeachingApplyItem[]>([])

// 对话框和表单
const applyDialogVisible = ref(false)
const selectedCourse = ref<CourseInfo | null>(null)
const submitLoading = ref(false)
const appliedCourseIds = ref<string[]>([]) // 存储已申请的课程ID

// 课程搜索表单
interface CourseSearchParams {
  courseName?: string
  courseId?: string
  credits?: number
}

const courseSearchForm = ref<CourseSearchParams>({
  courseName: undefined,
  courseId: undefined,
  credits: undefined
})

// 申请记录搜索表单
const applySearchForm = ref<TeachingApplySearchParams>({
  ApplyId: undefined,
  CourseId: undefined,
  State: undefined
})

// 申请表单
interface ApplyForm {
  reason: string
}

const applyForm = ref<ApplyForm>({
  reason: ''
})

// 申请表单验证规则
const applyRules = {
  reason: [
    { required: true, message: '请输入申请理由', trigger: 'blur' },
    { min: 10, message: '申请理由至少10个字符', trigger: 'blur' }
  ]
}

const applyFormRef = ref()

// 计算属性：根据搜索条件过滤课程数据
const filteredCourseData = computed(() => {
  return courseTableData.value.filter(course => {
    let match = true
    
    if (courseSearchForm.value.courseName) {
      match = match && course.Name.includes(courseSearchForm.value.courseName)
    }
    
    if (courseSearchForm.value.courseId) {
      match = match && course.CourseId.includes(courseSearchForm.value.courseId)
    }
    
    if (courseSearchForm.value.credits !== undefined) {
      match = match && course.Credits === courseSearchForm.value.credits
    }
    
    return match
  })
})

// 获取课程列表
const fetchCourseList = async () => {
  try {
    courseLoading.value = true
    const response = await getCourseList()
    if (response.data.IsSuccess) {
      courseTableData.value = response.data.Result
      ElMessage.success('课程列表加载成功')
    } else {
      ElMessage.error(response.data.Message || '获取课程数据失败')
    }
  } catch (error) {
    console.error('获取课程列表失败:', error)
    ElMessage.error('获取课程数据失败，请检查网络连接')
  } finally {
    courseLoading.value = false
  }
}

// 获取申请记录列表
const fetchApplyList = async () => {
  try {
    applyLoading.value = true
    const currentUserId = sessionStorage.getItem('userId') || ''
    const params: TeachingApplySearchParams = {
      ...applySearchForm.value,
      TeacherId: currentUserId
    }
    const response = await getTeachingApplyList(params)
    if (response.data.IsSuccess) {
      applyTableData.value = response.data.Result
      // 更新已申请课程ID列表
      appliedCourseIds.value = response.data.Result.map((item: TeachingApplyItem) => item.CourseId)
      ElMessage.success('申请记录加载成功')
    } else {
      ElMessage.error(response.data.Message || '获取申请记录失败')
    }
  } catch (error) {
    console.error('获取申请记录失败:', error)
    ElMessage.error('获取申请记录失败，请检查网络连接')
  } finally {
    applyLoading.value = false
  }
}

// 标签页切换处理
const handleTabChange = (tabName: string) => {
  if (tabName === 'apply') {
    fetchCourseList()
  } else if (tabName === 'records') {
    fetchApplyList()
  }
}

// 课程搜索
const handleCourseSearch = () => {
  ElMessage.success('搜索完成')
}

// 重置课程搜索
const handleCourseReset = () => {
  courseSearchForm.value = {
    courseName: undefined,
    courseId: undefined,
    credits: undefined
  }
  ElMessage.success('搜索条件已重置')
}

// 申请记录搜索
const handleApplySearch = () => {
  fetchApplyList()
}

// 重置申请记录搜索
const handleApplyReset = () => {
  applySearchForm.value = {
    ApplyId: undefined,
    CourseId: undefined,
    State: undefined
  }
  fetchApplyList()
}

// 处理课程表格选择变化
const handleCourseSelectionChange = (selection: CourseInfo[]) => {
  selectedCourses.value = selection
}

// 处理申请记录表格选择变化
const handleApplySelectionChange = (selection: TeachingApplyItem[]) => {
  selectedApplies.value = selection
}

// 判断课程是否已申请
const isAlreadyApplied = (courseId: string) => {
  return appliedCourseIds.value.includes(courseId)
}

// 单个申请
const handleApply = (course: CourseInfo) => {
  if (isAlreadyApplied(course.CourseId)) {
    ElMessage.warning('您已申请教授该课程')
    return
  }
  
  selectedCourse.value = course
  applyForm.value.reason = ''
  applyDialogVisible.value = true
}

// 批量申请
const handleBatchApply = async () => {
  if (selectedCourses.value.length === 0) {
    ElMessage.warning('请先选择要申请的课程')
    return
  }

  const notAppliedCourses = selectedCourses.value.filter(course => 
    !isAlreadyApplied(course.CourseId)
  )

  if (notAppliedCourses.length === 0) {
    ElMessage.warning('选中的课程都已申请')
    return
  }

  try {
    const { value: reason } = await ElMessageBox.prompt(
      `确定要申请教授选中的 ${notAppliedCourses.length} 门课程吗？请输入申请理由：`,
      '批量申请确认',
      {
        confirmButtonText: '确定申请',
        cancelButtonText: '取消',
        inputType: 'textarea',
        inputValidator: (value) => {
          if (!value || value.length < 10) {
            return '申请理由至少10个字符'
          }
          return true
        }
      }
    )

    const courseIds = notAppliedCourses.map(course => course.CourseId)
    const params = {
      teacherId: sessionStorage.getItem('userId') || '', // 确保这里的 'userId' 能拿到教师主键ID
      courseIds: courseIds,
      reason: reason
    }
    const response = await batchSubmitTeachingApply(params)
    
    if (response.data.IsSuccess) {
      ElMessage.success(`成功申请 ${notAppliedCourses.length} 门课程`)
      appliedCourseIds.value.push(...courseIds)
      selectedCourses.value = []
    } else {
      ElMessage.error(response.data.Message || '批量申请失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量申请失败:', error)
      ElMessage.error('批量申请失败，请重试')
    }
  }
}

// 确认申请
const confirmApply = async () => {
  if (!applyFormRef.value || !selectedCourse.value) return
  
  try {
    const valid = await applyFormRef.value.validate()
    if (!valid) return
    
    submitLoading.value = true
    
    const params: SubmitTeachingApplyParams = {
      TeacherId: sessionStorage.getItem('userId') || '', // 确保这里的 'userId' 能拿到教师主键ID
      CourseId: selectedCourse.value.CourseId,
      Information: applyForm.value.reason
    }
    
    const response = await submitTeachingApply(params)
    
    if (response.data.IsSuccess) {
      ElMessage.success('申请提交成功')
      appliedCourseIds.value.push(selectedCourse.value.CourseId)
      applyDialogVisible.value = false
      selectedCourse.value = null
    } else {
      ElMessage.error(response.data.Message || '申请失败')
    }
  } catch (error) {
    console.error('申请失败:', error)
    ElMessage.error('申请失败，请重试')
  } finally {
    submitLoading.value = false
  }
}

// 撤回申请
const handleWithdraw = async (row: TeachingApplyItem) => {
  try {
    await ElMessageBox.confirm('确定要撤回该申请吗？', '确认撤回', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    
    const response = await withdrawTeachingApply(row.ApplyId)
    if (response.data.IsSuccess) {
      ElMessage.success('撤回成功')
      fetchApplyList()
      // 从已申请列表中移除
      const index = appliedCourseIds.value.indexOf(row.CourseId)
      if (index > -1) {
        appliedCourseIds.value.splice(index, 1)
      }
    } else {
      ElMessage.error(response.data.Message || '撤回失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('撤回失败:', error)
      ElMessage.error('撤回失败，请重试')
    }
  }
}

// 批量撤回
const handleBatchWithdraw = async () => {
  if (selectedApplies.value.length === 0) {
    ElMessage.warning('请先选择要撤回的申请')
    return
  }

  const pendingApplies = selectedApplies.value.filter(apply => apply.State === 'P')
  if (pendingApplies.length === 0) {
    ElMessage.warning('只能撤回处理中的申请')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要撤回选中的 ${pendingApplies.length} 个申请吗？`,
      '确认批量撤回',
      {
        confirmButtonText: '确定撤回',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )

    // 依次撤回申请
    for (const apply of pendingApplies) {
      await withdrawTeachingApply(apply.ApplyId)
    }
    
    ElMessage.success(`成功撤回 ${pendingApplies.length} 个申请`)
    fetchApplyList()
    selectedApplies.value = []
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量撤回失败:', error)
      ElMessage.error('批量撤回失败，请重试')
    }
  }
}

// 格式化日期时间
const formatDateTime = (dateTime: string) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN')
}

// 获取状态标签类型
const getStateTagType = (state: string) => {
  switch (state) {
    case 'Y': return 'success'
    case 'N': return 'danger'
    case 'P': return 'warning'
    default: return 'info'
  }
}

// 获取状态文本
const getStateText = (state: string) => {
  switch (state) {
    case 'Y': return '已同意'
    case 'N': return '已拒绝'
    case 'P': return '处理中'
    default: return '未知'
  }
}

// 组件挂载时自动加载课程列表
onMounted(() => {
  fetchCourseList()
})
</script> 

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>