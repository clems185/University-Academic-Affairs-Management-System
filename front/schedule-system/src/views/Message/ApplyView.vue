<template>
  <div class="message-apply-container">
    <!-- 标签页切换 -->
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <!-- 提交申请标签页 -->
      <el-tab-pane label="提交申请" name="submit">
        <el-card shadow="hover" class="form-card">
          <el-form 
            :model="applyForm" 
            :rules="applyRules" 
            ref="applyFormRef" 
            label-width="100px"
            label-position="top">
            
            <el-form-item label="标题" prop="Title">
              <el-input 
                v-model="applyForm.Title" 
                placeholder="请输入标题" 
                maxlength="100"
                show-word-limit />
            </el-form-item>
            
            <el-form-item label="内容" prop="Content">
              <el-input
                v-model="applyForm.Content"
                type="textarea"
                :rows="6"
                placeholder="请输入内容"
                maxlength="4000"
                show-word-limit
              />
            </el-form-item>
            
            <el-form-item label="类型" prop="Types">
              <el-select 
                v-model="applyForm.Types" 
                placeholder="请选择类型"
                style="width: 100%">
                <el-option label="通知" value="通知" />
                <el-option label="公告" value="公告" />
                <el-option label="新闻" value="新闻" />
                <el-option label="文件" value="文件" />
                <el-option label="活动" value="活动" />
              </el-select>
            </el-form-item>
            
            <el-form-item label="申请理由（可选）" prop="ApplyComment">
              <el-input
                v-model="applyForm.ApplyComment"
                type="textarea"
                :rows="3"
                placeholder="请输入申请理由"
                maxlength="255"
                show-word-limit
              />
            </el-form-item>
            
            <el-form-item>
              <el-button 
                type="primary" 
                @click="submitApply"
                :loading="submitting">
                提交申请
              </el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
      
      <!-- 申请记录标签页 -->
      <el-tab-pane label="申请记录" name="records">
        <el-card shadow="hover" class="table-card">
       <div class="content-spacing"> 
          <!-- 操作按钮 -->
          <div class="action-buttons">
            <el-button 
              type="primary" 
              @click="fetchApplyList"
              :loading="loading">
              <el-icon><Refresh /></el-icon> 刷新
            </el-button>
            <el-button 
              type="danger" 
              @click="batchWithdraw"
              :disabled="selectedRows.length === 0 || !hasPendingSelected">
              <el-icon><Delete /></el-icon> 批量撤回
            </el-button>
          </div>
          
          <!-- 申请记录表格 -->
          <el-table 
            :data="applyList" 
            stripe 
            style="width: 100%" 
            v-loading="loading"
            @selection-change="handleSelectionChange">
            
            <el-table-column type="selection" width="55" />
            
            <el-table-column prop="Title" label="标题" min-width="180" />
            
            <el-table-column prop="Types" label="类型" width="100">
              <template #default="{ row }">
                <el-tag :type="getTypeTagType(row.Types)">
                  {{ row.Types }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="ApplyTime" label="申请时间" width="180">
              <template #default="{ row }">
                {{ formatDateTime(row.ApplyTime) }}
              </template>
            </el-table-column>
            
            <!-- 状态列 - 修复属性名错误 -->
            <el-table-column prop="StatusText" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusTagType(row.FinalDecision)">
                  {{ row.StatusText || getStatusText(row.FinalDecision) }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="ReviewTime" label="审核时间" width="180">
              <template #default="{ row }">
                {{ row.ReviewTime ? formatDateTime(row.ReviewTime) : '-' }}
              </template>
            </el-table-column>
            
            <!-- 新增审核人ID列 -->
            <el-table-column prop="ReviewId" label="审核人ID" width="120">
              <template #default="{ row }">
                {{ row.ReviewId || '-' }}
              </template>
            </el-table-column>
            
            <!-- 操作列 - 修复属性名错误 -->
            <el-table-column label="操作" width="120" fixed="right">
              <template #default="{ row }">
                <el-button 
                  v-if="row.FinalDecision === 'P'"
                  size="small" 
                  type="warning" 
                  @click="withdrawApply(row.InfoApplyId)">
                  撤回
                </el-button>
                <span v-else>-</span>
              </template>
            </el-table-column>
          </el-table>
          
          <!-- 分页控件 -->
          <el-pagination
            class="pagination"
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="totalItems"
            :page-sizes="[5, 10, 20, 50]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { Refresh, Delete } from '@element-plus/icons-vue'
import { 
  submitInformationApply, 
  getInformationApplyList, 
  withdrawInformationApply 
} from '@/api/messageApply/messageApply'
import type { InformationApplyReq, InformationApplyRes } from '@/api/messageApply/types'

// 当前激活的标签页
const activeTab = ref('submit')

// 申请表单
const applyForm = reactive<InformationApplyReq>({
  Title: '',
  Content: '',
  Types: '',
  ApplyComment: ''
})

// 表单验证规则
const applyRules: FormRules = {
  Title: [
    { required: true, message: '标题不能为空', trigger: 'blur' },
    { min: 2, max: 100, message: '标题长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  Content: [
    { required: true, message: '内容不能为空', trigger: 'blur' },
    { min: 10, max: 4000, message: '内容长度在 10 到 4000 个字符', trigger: 'blur' }
  ],
  Types: [
    { required: true, message: '请选择消息类型', trigger: 'change' }
  ]
}

// 表单引用
const applyFormRef = ref<FormInstance>()

// 提交状态
const submitting = ref(false)

// 搜索表单
const searchForm = reactive({
  Title: '',
  Types: '',
  Status: ''
})

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const totalItems = ref(0)

// 申请记录数据
const applyList = ref<InformationApplyRes[]>([])
const loading = ref(false)

// 选中的行
const selectedRows = ref<InformationApplyRes[]>([])

// 提交申请
const submitApply = async () => {
  if (!applyFormRef.value) return
  
  try {
    const valid = await applyFormRef.value.validate()
    if (!valid) return
    
    submitting.value = true
    const response = await submitInformationApply(applyForm)
    
    if (response.data.IsSuccess) {
      ElMessage.success('申请提交成功')
      resetForm()
      // 切换到记录页并刷新
      activeTab.value = 'records'
      fetchApplyList()
    } else {
      ElMessage.error(response.data.Msg || '申请提交失败')
    }
  } catch (error) {
    console.error('提交申请失败:', error)
    ElMessage.error('申请提交失败，请重试')
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  applyFormRef.value?.resetFields()
}

// 获取申请记录
const fetchApplyList = async () => {
  try {
    loading.value = true
    const response = await getInformationApplyList()
    
    if (response.data.IsSuccess) {
      // 确保数据是数组类型
      const data = response.data.Result || []
      applyList.value = Array.isArray(data) ? data : []
      totalItems.value = applyList.value.length
      
      // 模拟后端分页
      applyList.value = applyList.value.slice(
        (currentPage.value - 1) * pageSize.value,
        currentPage.value * pageSize.value
      )
    } else {
      ElMessage.error(response.data.Msg || '获取申请记录失败')
    }
  } catch (error) {
    console.error('获取申请记录失败:', error)
    ElMessage.error('获取申请记录失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}

// 撤回单个申请 - 移除confirm确认
const withdrawApply = async (applyId: string) => {
  try {
    const response = await withdrawInformationApply(applyId)
    
    if (response.data.IsSuccess) {
      ElMessage.success('撤回成功')
      fetchApplyList()
    } else {
      ElMessage.error(response.data.Msg || '撤回失败')
    }
  } catch (error) {
    console.error('撤回失败:', error)
    ElMessage.error('撤回失败，请重试')
  }
}

// 批量撤回 - 移除confirm确认
const batchWithdraw = async () => {
  // 筛选出待审核状态的申请
  const pendingApplies = selectedRows.value.filter(row => row.FinalDecision === 'P')
  
  if (pendingApplies.length === 0) {
    ElMessage.warning('选中的申请中没有待审核的记录')
    return
  }
  
  try {
    // 依次撤回申请
    const results = await Promise.allSettled(
      pendingApplies.map(apply => 
        withdrawInformationApply(apply.InfoApplyId)
    ))
    
    // 计算成功和失败的数量
    const successCount = results.filter(r => r.status === 'fulfilled').length
    const failedCount = results.length - successCount
    
    if (successCount > 0) {
      ElMessage.success(`成功撤回 ${successCount} 个申请`)
    }
    if (failedCount > 0) {
      ElMessage.warning(`${failedCount} 个申请撤回失败`)
    }
    
    selectedRows.value = []
    fetchApplyList()
  } catch (error) {
    console.error('批量撤回失败:', error)
    ElMessage.error('批量撤回失败，请重试')
  }
}

// 表格选择变化
const handleSelectionChange = (selection: InformationApplyRes[]) => {
  selectedRows.value = selection
}

// 标签页切换
const handleTabChange = (tabName: string) => {
  if (tabName === 'records') {
    fetchApplyList()
  }
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  fetchApplyList()
}

// 当前页变化
const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchApplyList()
}

// 格式化日期时间
const formatDateTime = (dateString: string) => {
  if (!dateString) return ''
  try {
    const date = new Date(dateString)
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    }).replace(/\//g, '-')
  } catch (e) {
    console.error('日期格式化错误:', e)
    return dateString // 返回原始字符串
  }
}

// 获取状态标签类型
const getStatusTagType = (status: string) => {
  switch (status) {
    case 'P': return 'warning' // 待审核
    case 'Y': return 'success' // 已通过
    case 'R': return 'danger'  // 已拒绝
    default: return 'info'
  }
}

// 获取状态文本
const getStatusText = (status: string) => {
  switch (status) {
    case 'P': return '待审核'
    case 'Y': return '已通过'
    case 'R': return '已拒绝'
    default: return status
  }
}

// 获取类型标签类型
const getTypeTagType = (type: string) => {
  switch (type) {
    case '通知': return 'primary'
    case '公告': return 'success'
    case '新闻': return 'warning'
    case '文件': return 'info'
    case '活动': return 'danger'
    default: return ''
  }
}

// 检查是否有待审核的选中项
const hasPendingSelected = computed(() => {
  return selectedRows.value.some(row => row.FinalDecision === 'P')
})

// 组件挂载时获取申请记录
onMounted(() => {
  if (activeTab.value === 'records') {
    fetchApplyList()
  }
})
</script>

<style scoped>
.content-spacing {
  margin-top: 28px; /* 整体下移距离 */
}

.message-apply-container {
  padding: 20px;
}

.form-card, .table-card {
  margin-top: 20px;
  border-radius: 8px;
}

.action-buttons {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-end;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.el-tag {
  margin-right: 5px;
}
</style>