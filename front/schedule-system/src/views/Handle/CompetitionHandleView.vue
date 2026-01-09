<template>
  <div>
    <!-- 搜索区域 -->
    <div style="margin-bottom: 20px;">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input 
            v-model="searchForm.contestId" 
            placeholder="请输入竞赛ID" 
            clearable />
        </el-col>
        <el-col :span="6">
          <el-input 
            v-model="searchForm.name" 
            placeholder="请输入竞赛名称" 
            clearable />
        </el-col>
        <el-col :span="6">
          <el-select 
            v-model="searchForm.grade" 
            placeholder="请选择竞赛等级" 
            clearable>
            <el-option label="国家级" value="国家级" />
            <el-option label="省级" value="省级" />
            <el-option label="市级" value="市级" />
            <el-option label="校级" value="校级" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select 
            v-model="searchForm.state" 
            placeholder="请选择处理状态" 
            clearable>
            <el-option label="同意" value="Y" />
            <el-option label="拒绝" value="N" />
            <el-option label="处理中" value="P" />
          </el-select>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 10px;">
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 批量操作按钮 -->
    <div style="margin-bottom: 20px;">
      <el-button 
        type="primary" 
        @click="handleBatchApprove" 
        :disabled="selectedRows.length === 0"
        style="margin-right: 10px;">
        批量同意
      </el-button>
      <el-button 
        type="warning" 
        @click="handleBatchReject" 
        :disabled="selectedRows.length === 0"
        style="margin-right: 10px;">
        批量拒绝
      </el-button>

    </div>

    <!-- 表格 -->
    <el-table 
      :data="tableData" 
      stripe 
      style="width: 100%" 
      v-loading="loading"
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="contestId" label="竞赛ID" width="120" />
      <el-table-column prop="name" label="竞赛名称" width="200" />
      <el-table-column prop="contestTime" label="竞赛时间" width="120" />
      <el-table-column prop="grade" label="竞赛等级" width="100" />
      <el-table-column prop="numbers" label="报名人数" width="100" />
      <el-table-column prop="state" label="处理状态" width="100">
        <template #default="scope">
          <el-tag 
            :type="scope.row.state === 'Y' ? 'success' : scope.row.state === 'N' ? 'danger' : 'warning'">
            {{ scope.row.state === 'Y' ? '同意' : scope.row.state === 'N' ? '拒绝' : '处理中' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reviewComments" label="审核意见" />
      <el-table-column prop="reviewTime" label="审核时间" width="120" />
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button size="small" type="primary" @click="handleApprove(scope.row)">同意</el-button>
          <el-button size="small" type="warning" @click="handleReject(scope.row)">拒绝</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 审核意见对话框 -->
    <el-dialog v-model="dialogVisible" title="审核意见" width="500px">
      <el-form :model="reviewForm" label-width="100px">
        <el-form-item label="审核意见">
          <el-input 
            v-model="reviewForm.reviewComments" 
            type="textarea" 
            :rows="4"
            placeholder="请输入审核意见" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmReview">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { 
  getCompetitionApplyList, 
  batchApproveCompetitionApply, 
  batchRejectCompetitionApply,
  approveCompetitionApply, 
  rejectCompetitionApply
} from '@/api/competitionHandle/competitionHandle'
import { CompetitionHandleParams, CompetitionHandleSearchParams, CompetitionHandleItem } from '@/api/competitionHandle/types'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref<CompetitionHandleItem[]>([])
const loading = ref(false)
const selectedRows = ref<CompetitionHandleItem[]>([])
const dialogVisible = ref(false)
const reviewForm = ref({
  reviewComments: '',
  action: '',
  currentRow: null as CompetitionHandleItem | null
})

// 搜索表单
const searchForm = ref<CompetitionHandleSearchParams>({
  contestId: undefined,
  name: undefined,
  grade: undefined,
  state: undefined
})

// 获取竞赛申请列表
const fetchCompetitionApplyList = async () => {
  try {
    loading.value = true
    const response = await getCompetitionApplyList(searchForm.value)
    if (response.data.IsSuccess) {
      tableData.value = response.data.Result
    } else {
      ElMessage.error(response.data.Message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取竞赛申请列表失败:', error)
    ElMessage.error('获取数据失败，请检查网络连接')
    tableData.value = []
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  fetchCompetitionApplyList()
}

// 重置搜索
const handleReset = () => {
  searchForm.value = {
    contestId: undefined,
    name: undefined,
    grade: undefined,
    state: undefined
  }
  fetchCompetitionApplyList()
}

// 处理表格选择变化
const handleSelectionChange = (selection: CompetitionHandleItem[]) => {
  selectedRows.value = selection
}

// 批量同意
const handleBatchApprove = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要同意的申请')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要同意选中的 ${selectedRows.value.length} 条申请吗？`,
      '确认操作',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )

    const ids = selectedRows.value.map((row: CompetitionHandleItem) => row.contestId)
    const response = await batchApproveCompetitionApply(ids)
    
    if (response.data.IsSuccess) {
      ElMessage.success('批量同意成功')
      await fetchCompetitionApplyList()
      selectedRows.value = []
    } else {
      ElMessage.error(response.data.Message || '批量同意失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量同意失败:', error)
      ElMessage.error('批量同意失败，请重试')
    }
  }
}

// 批量拒绝
const handleBatchReject = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要拒绝的申请')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要拒绝选中的 ${selectedRows.value.length} 条申请吗？`,
      '确认操作',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )

    const ids = selectedRows.value.map((row: CompetitionHandleItem) => row.contestId)
    const response = await batchRejectCompetitionApply(ids)
    
    if (response.data.IsSuccess) {
      ElMessage.success('批量拒绝成功')
      await fetchCompetitionApplyList()
      selectedRows.value = []
    } else {
      ElMessage.error(response.data.Message || '批量拒绝失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量拒绝失败:', error)
      ElMessage.error('批量拒绝失败，请重试')
    }
  }
}



// 同意申请
const handleApprove = (row: CompetitionHandleItem) => {
  reviewForm.value = {
    reviewComments: '',
    action: 'approve',
    currentRow: row
  }
  dialogVisible.value = true
}

// 拒绝申请
const handleReject = (row: CompetitionHandleItem) => {
  reviewForm.value = {
    reviewComments: '',
    action: 'reject',
    currentRow: row
  }
  dialogVisible.value = true
}

// 确认审核
const confirmReview = async () => {
  if (!reviewForm.value.currentRow) return
  
  try {
    const { contestId } = reviewForm.value.currentRow
    const { reviewComments, action } = reviewForm.value
    const reviewerId = 1 // 这里应该从用户登录信息中获取

    let response
    if (action === 'approve') {
      response = await approveCompetitionApply(contestId, reviewComments, reviewerId)
    } else {
      response = await rejectCompetitionApply(contestId, reviewComments, reviewerId)
    }

    if (response.data.IsSuccess) {
      ElMessage.success(action === 'approve' ? '同意成功' : '拒绝成功')
      dialogVisible.value = false
      await fetchCompetitionApplyList()
    } else {
      ElMessage.error(response.data.Message || (action === 'approve' ? '同意失败' : '拒绝失败'))
    }
  } catch (error) {
    ElMessage.error('操作失败，请重试')
  }
}



// 组件挂载时获取数据
onMounted(() => {
  fetchCompetitionApplyList()
})
</script>