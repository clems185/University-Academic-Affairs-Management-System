<template>
  <div class="deferred-apply-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>申请缓考</h2>
      <p>查看即将参加的考试并申请缓考</p>
    </div>

    <!-- 标签页 -->
    <el-tabs v-model="activeTab" type="border-card">
      <!-- 第一个标签：申请缓考 -->
      <el-tab-pane label="申请缓考" name="apply">
        <div class="apply-section">
          <div class="section-header">
            <h3>我的考试列表</h3>
            <el-button 
              type="primary" 
              @click="fetchMyExams"
              :loading="examsLoading">
              刷新列表
            </el-button>
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
            
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button 
                  size="small" 
                  type="primary" 
                  @click="openApplyDialog(scope.row)">
                  申请缓考
                </el-button>
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
      </el-tab-pane>

      <!-- 第二个标签：我的缓考申请 -->
      <el-tab-pane label="我的缓考申请" name="myApplies">
        <div class="my-applies-section">
          <div class="section-header">
            <h3>我的缓考申请记录</h3>
            <el-button 
              type="primary" 
              @click="fetchMyApplies"
              :loading="appliesLoading">
              刷新列表
            </el-button>
          </div>

          <!-- 申请表格 -->
          <el-table 
            :data="appliesData" 
            stripe 
            style="width: 100%" 
            v-loading="appliesLoading"
            empty-text="暂无缓考申请">
            
            <el-table-column prop="ApplyId" label="申请编号" width="120" />
            
            <el-table-column prop="CourseId" label="课程编号" width="120" />
            
            <el-table-column prop="CourseName" label="课程名称" width="200">
              <template #default="scope">
                <el-tag type="info" size="small">{{ scope.row.CourseName || '未知课程' }}</el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="ApplyDate" label="申请日期" width="150">
              <template #default="scope">
                {{ formatDate(scope.row.ApplyDate) }}
              </template>
            </el-table-column>
            
            <el-table-column prop="State" label="审核状态" width="120">
              <template #default="scope">
                <el-tag 
                  :type="getStatusType(scope.row.State)" 
                  size="small">
                  {{ scope.row.StateDescription || getStateDescription(scope.row.State) }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="ReviewTime" label="审核时间" width="150">
              <template #default="scope">
                {{ scope.row.ReviewTime ? formatDate(scope.row.ReviewTime) : '未审核' }}
              </template>
            </el-table-column>
            
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button 
                  size="small" 
                  type="info" 
                  @click="showApplyDetail(scope.row)">
                  详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 申请缓考对话框 -->
    <el-dialog
      v-model="applyDialogVisible"
      title="申请缓考"
      width="600px">
      <div v-if="selectedExam">
        <div class="exam-info">
          <h4>考试信息</h4>
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="考试编号">{{ selectedExam.ExamId }}</el-descriptions-item>
            <el-descriptions-item label="课程编号">{{ selectedExam.CourseId }}</el-descriptions-item>
            <el-descriptions-item label="课程名称">{{ selectedExam.CourseName || '未知课程' }}</el-descriptions-item>
            <el-descriptions-item label="考场">{{ selectedExam.ClassroomId }}</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <el-form 
          :model="applyForm" 
          :rules="applyRules" 
          ref="applyFormRef" 
          label-width="100px"
          style="margin-top: 20px;">
          <el-form-item label="申请理由" prop="information">
            <el-input
              v-model="applyForm.information"
              type="textarea"
              :rows="6"
              placeholder="请详细说明申请缓考的理由..."
              maxlength="4000"
              show-word-limit>
            </el-input>
          </el-form-item>
        </el-form>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="applyDialogVisible = false">取消</el-button>
          <el-button 
            type="primary" 
            @click="submitApply" 
            :loading="submitLoading">
            提交申请
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 考试详情对话框 -->
    <el-dialog
      v-model="examDetailDialogVisible"
      title="考试详情"
      width="500px">
      <div v-if="selectedExam">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="考试编号">{{ selectedExam.ExamId }}</el-descriptions-item>
          <el-descriptions-item label="课程编号">{{ selectedExam.CourseId }}</el-descriptions-item>
          <el-descriptions-item label="课程名称">{{ selectedExam.CourseName || '未知课程' }}</el-descriptions-item>
          <el-descriptions-item label="学期">{{ selectedExam.Year }}年第{{ selectedExam.Semester }}学期</el-descriptions-item>
          <el-descriptions-item label="时间段ID">{{ selectedExam.TimeSlotId }}</el-descriptions-item>
          <el-descriptions-item label="考场">{{ selectedExam.ClassroomId }}</el-descriptions-item>
          <el-descriptions-item label="考试日期">{{ selectedExam.ExamDate || '待定' }}</el-descriptions-item>
          <el-descriptions-item label="考试时间">{{ selectedExam.ExamTime || '待定' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="examDetailDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 申请详情对话框 -->
    <el-dialog
      v-model="applyDetailDialogVisible"
      title="缓考申请详情"
      width="600px">
      <div v-if="selectedApply">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="申请编号">{{ selectedApply.ApplyId }}</el-descriptions-item>
          <el-descriptions-item label="课程编号">{{ selectedApply.CourseId }}</el-descriptions-item>
          <el-descriptions-item label="课程名称">{{ selectedApply.CourseName || '未知课程' }}</el-descriptions-item>
          <el-descriptions-item label="申请日期">{{ formatDate(selectedApply.ApplyDate) }}</el-descriptions-item>
          <el-descriptions-item label="审核状态">
            <el-tag :type="getStatusType(selectedApply.State)">{{ selectedApply.StateDescription || getStateDescription(selectedApply.State) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="审核时间">{{ selectedApply.ReviewTime ? formatDate(selectedApply.ReviewTime) : '未审核' }}</el-descriptions-item>
          <el-descriptions-item label="申请理由">
            <div class="apply-reason">{{ selectedApply.Information }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="审核意见" v-if="selectedApply.ReviewComments">
            <div class="review-comments">{{ selectedApply.ReviewComments }}</div>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="applyDetailDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getExamsForDeferred, submitDeferredApply, getMyDeferredApplies } from '@/api/defferedApply/deferredApply'
import { MyExam, DeferredApply } from '@/api/defferedApply/types'

// 标签页状态
const activeTab = ref('apply')

// 考试数据状态
const examsData = ref<MyExam[]>([])
const examsLoading = ref(false)

// 申请数据状态
const appliesData = ref<DeferredApply[]>([])
const appliesLoading = ref(false)

// 对话框状态
const applyDialogVisible = ref(false)
const examDetailDialogVisible = ref(false)
const applyDetailDialogVisible = ref(false)
const submitLoading = ref(false)

// 选中的数据
const selectedExam = ref<MyExam | null>(null)
const selectedApply = ref<DeferredApply | null>(null)

// 申请表单
interface ApplyForm {
  information: string
}

const applyForm = ref<ApplyForm>({
  information: ''
})

const applyFormRef = ref()

// 表单验证规则
const applyRules = {
  information: [
    { required: true, message: '请填写申请理由', trigger: 'blur' },
    { min: 10, message: '申请理由至少需要10个字符', trigger: 'blur' },
    { max: 4000, message: '申请理由不能超过4000个字符', trigger: 'blur' }
  ]
}

// 获取考试列表
const fetchMyExams = async () => {
  try {
    examsLoading.value = true
    const currentUserId = sessionStorage.getItem('userId') || ''
    console.log('当前登录学生ID:', currentUserId)
    
    if (!currentUserId) {
      ElMessage.error('请先登录')
      return
    }
    
    const response = await getExamsForDeferred(currentUserId)
    console.log('后端返回考试结果:', response.data)
    
    if (response.data.IsSuccess) {
      examsData.value = response.data.Result || []
      console.log('成功获取考试数据:', examsData.value)
      ElMessage.success(`考试列表加载成功，共${examsData.value.length}场考试`)
    } else {
      console.error('后端返回错误:', response.data.Message)
      ElMessage.error(response.data.Message || '获取考试列表失败')
    }
  } catch (error) {
    console.error('获取考试列表失败:', error)
    ElMessage.error('获取考试列表失败，请检查网络连接')
  } finally {
    examsLoading.value = false
  }
}

// 获取我的缓考申请列表
const fetchMyApplies = async () => {
  try {
    appliesLoading.value = true
    const currentUserId = sessionStorage.getItem('userId') || ''
    console.log('当前登录学生ID:', currentUserId)
    
    if (!currentUserId) {
      ElMessage.error('请先登录')
      return
    }
    
    const response = await getMyDeferredApplies(currentUserId)
    console.log('后端返回申请结果:', response.data)
    
    if (response.data.IsSuccess) {
      appliesData.value = response.data.Result || []
      console.log('成功获取申请数据:', appliesData.value)
      ElMessage.success(`缓考申请列表加载成功，共${appliesData.value.length}条记录`)
    } else {
      console.error('后端返回错误:', response.data.Message)
      ElMessage.error(response.data.Message || '获取缓考申请列表失败')
    }
  } catch (error) {
    console.error('获取缓考申请列表失败:', error)
    ElMessage.error('获取缓考申请列表失败，请检查网络连接')
  } finally {
    appliesLoading.value = false
  }
}

// 打开申请对话框
const openApplyDialog = (exam: MyExam) => {
  selectedExam.value = exam
  applyForm.value.information = ''
  applyDialogVisible.value = true
}

// 显示考试详情
const showExamDetail = (exam: MyExam) => {
  selectedExam.value = exam
  examDetailDialogVisible.value = true
}

// 显示申请详情
const showApplyDetail = (apply: DeferredApply) => {
  selectedApply.value = apply
  applyDetailDialogVisible.value = true
}

// 提交申请
const submitApply = async () => {
  if (!applyFormRef.value || !selectedExam.value) return
  
  try {
    const valid = await applyFormRef.value.validate()
    if (!valid) return
    
    const currentUserId = sessionStorage.getItem('userId') || ''
    if (!currentUserId) {
      ElMessage.error('请先登录')
      return
    }
    
    submitLoading.value = true
    
    const params = {
      StudentId: currentUserId,
      CourseId: selectedExam.value.CourseId,
      Information: applyForm.value.information
    }
    
    console.log('提交缓考申请参数:', params)
    const response = await submitDeferredApply(params)
    console.log('缓考申请提交结果:', response.data)
    
    if (response.data.IsSuccess) {
      ElMessage.success('缓考申请提交成功，请等待审核')
      applyDialogVisible.value = false
      
      // 刷新申请列表
      await fetchMyApplies()
    } else {
      ElMessage.error(response.data.Message || response.data.Msg || '缓考申请提交失败')
    }
  } catch (error: any) {
    console.error('提交缓考申请失败:', error)
    const errorMessage = error.response?.data?.Message || error.response?.data?.Msg || '缓考申请提交失败，请重试'
    ElMessage.error(errorMessage)
  } finally {
    submitLoading.value = false
  }
}

// 格式化日期
const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', { hour12: false })
}

// 获取状态类型
const getStatusType = (state: string) => {
  switch (state) {
    case 'P': return 'warning'
    case 'A': return 'success'
    case 'R': return 'danger'
    default: return 'info'
  }
}

// 获取状态描述
const getStateDescription = (state: string) => {
  switch (state) {
    case 'P': return '待审核'
    case 'A': return '已通过'
    case 'R': return '已拒绝'
    default: return '未知状态'
  }
}

// 组件挂载时自动加载数据
onMounted(() => {
  fetchMyExams()
  fetchMyApplies()
})
</script>

<style scoped>
.deferred-apply-container {
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

.apply-section, .my-applies-section {
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

.exam-info {
  margin-bottom: 20px;
}

.exam-info h4 {
  color: #303133;
  margin-bottom: 10px;
}

.apply-reason, .review-comments {
  max-height: 100px;
  overflow-y: auto;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
  white-space: pre-wrap;
  word-break: break-word;
}

.dialog-footer {
  text-align: right;
}

.el-table {
  font-size: 14px;
}

:deep(.el-descriptions__label) {
  width: 100px;
  font-weight: bold;
}

:deep(.el-tabs__content) {
  padding: 0;
}

:deep(.el-tab-pane) {
  background: transparent;
}
</style>