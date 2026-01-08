<template>
  <div>
    <!-- 搜索条件 -->
    <el-card shadow="never" style="margin-bottom: 20px">
      <template #header>
        <div class="card-header">
          <span>头像审批管理</span>
        </div>
      </template>
      
      <el-form :model="searchForm" inline>
        <el-form-item label="申请人ID">
          <el-input 
            v-model="searchForm.applicantId" 
            placeholder="请输入申请人ID" 
            clearable 
            style="width: 200px" 
          />
        </el-form-item>
        
        <el-form-item label="申请人类型">
          <el-select 
            v-model="searchForm.applicantType" 
            placeholder="请选择类型" 
            clearable 
            style="width: 150px"
          >
            <el-option label="学生" value="学生" />
            <el-option label="教师" value="教师" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="审核状态">
          <el-select 
            v-model="searchForm.finalDecision" 
            placeholder="请选择状态" 
            clearable 
            style="width: 150px"
          >
            <el-option label="处理中" value="P" />
            <el-option label="已同意" value="Y" />
            <el-option label="已拒绝" value="N" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="申请时间">
          <el-date-picker
            v-model="applyTimeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            style="width: 350px"
            @change="handleTimeRangeChange"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            搜索
          </el-button>
          <el-button @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <div style="margin-bottom: 20px">
      <el-button 
        type="success" 
        @click="handleBatchApprove"
        :disabled="selectedRows.length === 0"
      >
        批量同意
      </el-button>
      <el-button 
        type="warning" 
        @click="handleBatchReject"
        :disabled="selectedRows.length === 0"
      >
        批量拒绝
      </el-button>
      <span style="margin-left: 10px; color: #999">
        已选择 {{ selectedRows.length }} 项
      </span>
    </div>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table 
        :data="tableData" 
        stripe 
        style="width: 100%" 
        v-loading="loading"
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="ApplyId" label="申请ID" width="150" sortable />
        <el-table-column prop="ApplicantId" label="申请人ID" width="120" sortable />
        <el-table-column prop="ApplicantTypeDisplay" label="申请人类型" width="100" sortable />
        <el-table-column prop="Information" label="申请原因" width="200" show-overflow-tooltip />
        <el-table-column label="头像状态" width="120">
          <template #default="scope">
            <el-tag type="primary" size="small">
              有头像
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ApplyTime" label="申请时间" width="160" sortable>
          <template #default="scope">
            {{ formatDateTime(scope.row.ApplyTime) }}
          </template>
        </el-table-column>
        <el-table-column label="审核状态" width="100">
          <template #default="scope">
            <el-tag 
              :type="getStatusTagType(scope.row.FinalDecision)"
              size="small"
            >
              {{ scope.row.FinalDecisionDisplay }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ReviewTime" label="审核时间" width="160">
          <template #default="scope">
            {{ scope.row.ReviewTime ? formatDateTime(scope.row.ReviewTime) : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" @click="handleViewDetail(scope.row)">
              查看详情
            </el-button>
            <el-button 
              v-if="scope.row.FinalDecision === 'P'"
              size="small" 
              type="success" 
              @click="handleApprove(scope.row)"
            >
              同意
            </el-button>
            <el-button 
              v-if="scope.row.FinalDecision === 'P'"
              size="small" 
              type="warning" 
              @click="handleReject(scope.row)"
            >
              拒绝
            </el-button>
            
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="申请详情" width="800px">
      <div v-if="currentApply">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="申请ID">
            {{ currentApply.ApplyId }}
          </el-descriptions-item>
          <el-descriptions-item label="申请人ID">
            {{ currentApply.ApplicantId }}
          </el-descriptions-item>
          <el-descriptions-item label="申请人类型">
            {{ currentApply.ApplicantTypeDisplay }}
          </el-descriptions-item>
          <el-descriptions-item label="申请时间">
            {{ formatDateTime(currentApply.ApplyTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="申请原因" :span="2">
            {{ currentApply.Information }}
          </el-descriptions-item>
          <el-descriptions-item label="新头像" :span="2" v-if="currentApply.NewPhotoBase64">
            <img 
              :src="`data:image/jpeg;base64,${currentApply.NewPhotoBase64}`" 
              alt="新头像"
              style="max-width: 200px; max-height: 200px; border-radius: 8px;"
            />
          </el-descriptions-item>
          <el-descriptions-item label="审核状态">
            <el-tag :type="getStatusTagType(currentApply.FinalDecision)">
              {{ currentApply.FinalDecisionDisplay }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="审核时间">
            {{ currentApply.ReviewTime ? formatDateTime(currentApply.ReviewTime) : '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="审核意见" :span="2" v-if="currentApply.ReviewComments">
            {{ currentApply.ReviewComments }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog v-model="reviewDialogVisible" :title="reviewMode === 'approve' ? '同意头像申请' : '拒绝头像申请'" width="500px">
      <el-form :model="reviewForm" label-width="80px">
        <el-form-item label="审核意见">
          <el-input
            v-model="reviewForm.reviewComments"
            type="textarea"
            :rows="4"
            placeholder="请输入审核意见"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="reviewDialogVisible = false">取消</el-button>
          <el-button 
            type="primary" 
            @click="confirmReview"
            :loading="reviewing"
          >
            {{ reviewMode === 'approve' ? '同意头像' : '拒绝头像' }}
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { 
  getUpdateInfoApplyList, 
  getUpdateInfoApplyDetail, 
  reviewUpdateInfoApply,
  batchReviewUpdateInfoApply
} from '@/api/imageHandle/imageHandle'
import { 
  UpdateInfoApplySearchParams, 
  UpdateInfoApplyItem, 
  UpdateInfoApplyDetail,
  ReviewUpdateInfoApplyParams,
  BatchReviewUpdateInfoApplyParams
} from '@/api/imageHandle/types'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref<UpdateInfoApplyItem[]>([])
const loading = ref(false)
const selectedRows = ref<UpdateInfoApplyItem[]>([])
const detailDialogVisible = ref(false)
const reviewDialogVisible = ref(false)
const currentApply = ref<UpdateInfoApplyDetail | null>(null)
const reviewing = ref(false)
const reviewMode = ref<'approve' | 'reject'>('approve')
const currentReviewApplyId = ref('')
const applyTimeRange = ref<[Date, Date] | null>(null)

// 搜索表单
const searchForm = ref<UpdateInfoApplySearchParams>({
  applicantId: undefined,
  applicantType: undefined,
  finalDecision: undefined,
  applyTimeStart: undefined,
  applyTimeEnd: undefined
})

// 审核表单
const reviewForm = ref({
  reviewComments: ''
})

// 获取头像审批列表
const fetchApplyList = async () => {
  try {
    loading.value = true
    const response = await getUpdateInfoApplyList(searchForm.value)
    if (response.data.IsSuccess) {
      tableData.value = response.data.Result
            } else {
          ElMessage.error(response.data.Message || '获取数据失败')
        }
  } catch (error) {
    console.error('获取头像审批列表失败:', error)
    ElMessage.error('获取数据失败，请检查网络连接')
    tableData.value = []
  } finally {
    loading.value = false
  }
}

// 时间范围变化处理
const handleTimeRangeChange = (value: [Date, Date] | null) => {
  if (value) {
    searchForm.value.applyTimeStart = value[0].toISOString()
    searchForm.value.applyTimeEnd = value[1].toISOString()
  } else {
    searchForm.value.applyTimeStart = undefined
    searchForm.value.applyTimeEnd = undefined
  }
}

// 搜索
const handleSearch = () => {
  fetchApplyList()
}

// 重置搜索
const handleReset = () => {
  searchForm.value = {
    applicantId: undefined,
    applicantType: undefined,
    finalDecision: undefined,
    applyTimeStart: undefined,
    applyTimeEnd: undefined
  }
  applyTimeRange.value = null
  fetchApplyList()
}

// 查看详情
const handleViewDetail = async (row: UpdateInfoApplyItem) => {
  try {
    const response = await getUpdateInfoApplyDetail(row.ApplyId)
    if (response.data.IsSuccess) {
      currentApply.value = response.data.Result
      detailDialogVisible.value = true
    } else {
      ElMessage.error(response.data.Message || '获取详情失败')
    }
  } catch (error) {
    console.error('获取头像申请详情失败:', error)
    ElMessage.error('获取详情失败，请检查网络连接')
  }
}

// 同意申请
const handleApprove = (row: UpdateInfoApplyItem) => {
  reviewMode.value = 'approve'
  currentReviewApplyId.value = row.ApplyId
  reviewForm.value.reviewComments = ''
  reviewDialogVisible.value = true
}

// 拒绝申请
const handleReject = (row: UpdateInfoApplyItem) => {
  reviewMode.value = 'reject'
  currentReviewApplyId.value = row.ApplyId
  reviewForm.value.reviewComments = ''
  reviewDialogVisible.value = true
}

// 确认审核
const confirmReview = async () => {
  try {
    reviewing.value = true
    const params: ReviewUpdateInfoApplyParams = {
      ApplyId: currentReviewApplyId.value,
      ReviewComments: reviewForm.value.reviewComments,
      FinalDecision: reviewMode.value === 'approve' ? 'Y' : 'N'
    }
    
    const response = await reviewUpdateInfoApply(params)
    if (response.data.IsSuccess) {
      ElMessage.success(response.data.Message || '审核成功')
      reviewDialogVisible.value = false
      fetchApplyList()
    } else {
      ElMessage.error(response.data.Message || '审核失败')
    }
  } catch (error) {
    console.error('审核失败:', error)
    ElMessage.error('审核失败，请检查网络连接')
  } finally {
    reviewing.value = false
  }
}

// 批量同意
const handleBatchApprove = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要同意的申请')
    return
  }

      const pendingRows = selectedRows.value.filter(row => row.FinalDecision === 'P')
    if (pendingRows.length === 0) {
      ElMessage.warning('请选择处理中的头像申请')
      return
    }

    try {
    const { value: reviewComments } = await ElMessageBox.prompt(
        `确定要同意选中的 ${pendingRows.length} 个头像申请吗？`,
        '批量同意头像',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: '请输入审核意见'
      }
    )

    const params: BatchReviewUpdateInfoApplyParams = {
      ApplyIds: pendingRows.map(row => row.ApplyId),
      ReviewComments: reviewComments || '',
      FinalDecision: 'Y'
    }

    const response = await batchReviewUpdateInfoApply(params)
          if (response.data.IsSuccess) {
        ElMessage.success(response.data.Message || '批量同意头像成功')
        selectedRows.value = []
        fetchApplyList()
      } else {
        ElMessage.error(response.data.Message || '批量同意头像失败')
      }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量同意头像失败:', error)
      ElMessage.error('批量同意头像失败，请检查网络连接')
    }
  }
}

// 批量拒绝
const handleBatchReject = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要拒绝的申请')
    return
  }

      const pendingRows = selectedRows.value.filter(row => row.FinalDecision === 'P')
  if (pendingRows.length === 0) {
    ElMessage.warning('请选择处理中的头像申请')
    return
  }

  try {
    const { value: reviewComments } = await ElMessageBox.prompt(
      `确定要拒绝选中的 ${pendingRows.length} 个头像申请吗？`,
      '批量拒绝头像',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: '请输入审核意见'
      }
    )

    const params: BatchReviewUpdateInfoApplyParams = {
      ApplyIds: pendingRows.map(row => row.ApplyId),
      ReviewComments: reviewComments || '',
      FinalDecision: 'N'
    }

    const response = await batchReviewUpdateInfoApply(params)
    if (response.data.IsSuccess) {
      ElMessage.success(response.data.Message || '批量拒绝头像成功')
      selectedRows.value = []
      fetchApplyList()
    } else {
      ElMessage.error(response.data.Message || '批量拒绝头像失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
          console.error('批量拒绝头像失败:', error)
    ElMessage.error('批量拒绝头像失败，请检查网络连接')
    }
  }
}

// 删除与批量删除功能已移除

// 选择变化
const handleSelectionChange = (selection: UpdateInfoApplyItem[]) => {
  selectedRows.value = selection
}

// 格式化日期时间
const formatDateTime = (dateString: string) => {
  return new Date(dateString).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 获取状态标签类型
const getStatusTagType = (status: string) => {
  switch (status) {
    case 'Y': return 'success'
    case 'N': return 'danger'
    case 'P': return 'warning'
    default: return 'info'
  }
}

// 组件挂载时获取数据
onMounted(() => {
  fetchApplyList()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}
</style>