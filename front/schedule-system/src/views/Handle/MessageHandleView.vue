<template>
  <div class="review-container">
    <div class="review-layout">
      <!-- 左侧：审核区域 -->
      <div class="review-main">
        <el-card shadow="hover" class="review-card">
          <div class="review-header">
            <h2>消息审核</h2>
            <el-button 
              type="primary" 
              @click="refreshPendingList"
              :loading="loading">
              <el-icon><Refresh /></el-icon> 刷新待审核列表
            </el-button>
          </div>

          <!-- 待审核列表 -->
          <div class="pending-list">
            <h3>待审核申请</h3>
            <el-table 
              :data="pendingList" 
              stripe 
              style="width: 100%"
              v-loading="loading"
              @selection-change="handlePendingSelectionChange">
              
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
              <el-table-column prop="ApplicantId" label="申请人ID" width="120" />
            </el-table>
          </div>

          <!-- 审核操作区域 -->
          <div class="review-action">
            <h3>审核操作</h3>
            <el-form :model="reviewForm" label-width="100px">
              <el-form-item label="审核决定">
                <el-radio-group v-model="reviewForm.FinalDecision">
                  <el-radio label="Y">通过</el-radio>
                  <el-radio label="R">拒绝</el-radio>
                </el-radio-group>
              </el-form-item>
              
              <el-form-item label="审核意见">
                <el-input
                  v-model="reviewForm.ReviewComments"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入审核意见"
                  maxlength="500"
                  show-word-limit
                />
              </el-form-item>
              
              <el-form-item>
                <el-button 
                  type="primary" 
                  @click="submitReview"
                  :disabled="selectedPending.length === 0"
                  :loading="reviewing">
                  提交审核
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </div>

      <!-- 右侧：智能体助手 -->
      <div class="agent-assistant">
        <el-card shadow="hover" class="agent-card">
          <h2 class="agent-title">智能审核助手</h2>
          <div class="chat-container">
            <div class="chat-messages" ref="chatMessagesRef">
              <div v-if="chatMessages.length === 0" class="empty-chat">
                <el-icon class="empty-icon"><ChatDotRound /></el-icon>
                <p class="empty-text">您好！我是智能审核助手，可以帮助您：</p>
                <ul class="empty-features">
                  <li>分析待审核申请内容</li>
                  <li>提供审核建议和意见</li>
                  <li>解答审核相关问题</li>
                </ul>
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
                  <div v-if="message.role === 'assistant' && message.thinking" class="thinking-indicator">
                    <el-icon class="loading-icon"><Loading /></el-icon>
                    <span>正在思考中...</span>
                  </div>
                  <div class="message-text">{{ message.content }}</div>
                  <div class="message-time">{{ formatTime(message.timestamp) }}</div>
                  <!-- 显示智能体的建议操作 -->
                  <div v-if="message.suggestedAction" class="suggested-action">
                    <el-divider />
                    <div class="action-suggestion">
                      <strong>建议操作：</strong>
                      <el-tag :type="message.suggestedAction.action === 'approve' ? 'success' : message.suggestedAction.action === 'reject' ? 'danger' : 'warning'">
                        {{ message.suggestedAction.action === 'approve' ? '通过' : message.suggestedAction.action === 'reject' ? '拒绝' : '需要更多信息' }}
                      </el-tag>
                      <p class="action-reason">{{ message.suggestedAction.reason }}</p>
                      <el-button 
                        v-if="message.suggestedAction.reviewComments"
                        size="small" 
                        type="primary"
                        @click="applySuggestion(message.suggestedAction)">
                        应用建议
                      </el-button>
                    </div>
                  </div>
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
                placeholder="请输入您的问题，例如：帮我分析一下当前待审核的申请..."
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
        </el-card>
      </div>
    </div>
      
    <div class="bottom-space"></div>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, ChatDotRound, Service, User, Loading, Promotion } from '@element-plus/icons-vue'
import { 
  getPendingApplications,
  reviewApplication,
  bulkDeleteReviewed
} from '@/api/messageHandle/messageHandle'
import type { InformationApplyRes } from '@/api/messageApply/types'
import { sendQuestionToMessageAgent } from '@/api/agentmessage/agentmessage'
import type { ChatMessage, MessageChatResponse, SuggestedAction } from '@/api/agentmessage/types'

// 待审核列表
const pendingList = ref<InformationApplyRes[]>([])
const selectedPending = ref<InformationApplyRes[]>([])
const loading = ref(false)

// 审核表单
const reviewForm = reactive({
  FinalDecision: 'Y', // 默认通过
  ReviewComments: ''
})
const reviewing = ref(false)

// 已审核列表
const reviewedList = ref<InformationApplyRes[]>([])
const selectedReviewed = ref<InformationApplyRes[]>([])
const loadingReviewed = ref(false)
const deleting = ref(false)

// 获取待审核列表
const refreshPendingList = async () => {
  try {
    loading.value = true
    const response = await getPendingApplications()
    
    if (response.data.IsSuccess) {
      pendingList.value = response.data.Result || []
      ElMessage.success(`获取到 ${pendingList.value.length} 条待审核记录`)
    } else {
      ElMessage.error(response.data.Msg || '获取待审核列表失败')
    }
  } catch (error) {
    console.error('获取待审核列表失败:', error)
    ElMessage.error('获取待审核列表失败')
  } finally {
    loading.value = false
  }
}

// 提交审核
const submitReview = async () => {
  if (selectedPending.value.length === 0) {
    ElMessage.warning('请选择要审核的申请')
    return
  }
  
  try {
    reviewing.value = true
    
    // 逐个审核选中的申请
    const results = await Promise.allSettled(
      selectedPending.value.map(apply => 
        reviewApplication({
          InfoApplyId: apply.InfoApplyId,
          FinalDecision: reviewForm.FinalDecision,
          ReviewComments: reviewForm.ReviewComments
        })
      )
    )
    
    // 计算成功和失败的数量
    const successCount = results.filter(r => r.status === 'fulfilled' && r.value.data.IsSuccess).length
    const failedCount = results.length - successCount
    
    if (successCount > 0) {
      ElMessage.success(`成功审核 ${successCount} 条申请`)
    }
    if (failedCount > 0) {
      ElMessage.warning(`${failedCount} 条申请审核失败`)
    }
    
    // 刷新列表
    refreshPendingList()
    loadReviewedList()
  } catch (error) {
    console.error('提交审核失败:', error)
    ElMessage.error('提交审核失败')
  } finally {
    reviewing.value = false
  }
}

// 加载已审核列表
const loadReviewedList = async () => {
  try {
    loadingReviewed.value = true
    // 实际项目中应有专门接口获取已审核列表
    // 这里简化为从pendingList中筛选已审核的
    reviewedList.value = pendingList.value.filter(
      item => item.FinalDecision === 'Y' || item.FinalDecision === 'R'
    )
  } catch (error) {
    console.error('加载已审核列表失败:', error)
    ElMessage.error('加载已审核列表失败')
  } finally {
    loadingReviewed.value = false
  }
}

// 批量删除已审核记录
const bulkDelete = async () => {
  if (selectedReviewed.value.length === 0) {
    ElMessage.warning('请选择要删除的记录')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedReviewed.value.length} 条记录吗？`,
      '确认删除',
      { type: 'warning' }
    )
    
    deleting.value = true
    const applyIds = selectedReviewed.value.map(item => item.InfoApplyId)
    const response = await bulkDeleteReviewed(applyIds)
    
    if (response.data.IsSuccess) {
      ElMessage.success(response.data.Msg || '删除成功')
      // 刷新已审核列表
      loadReviewedList()
    } else {
      ElMessage.error(response.data.Msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  } finally {
    deleting.value = false
  }
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
      minute: '2-digit'
    }).replace(/\//g, '-')
  } catch (e) {
    return dateString
  }
}

// 获取类型标签样式
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

// 选择变化处理
const handlePendingSelectionChange = (selection: InformationApplyRes[]) => {
  selectedPending.value = selection
}

const handleReviewedSelectionChange = (selection: InformationApplyRes[]) => {
  selectedReviewed.value = selection
}

// ========== 智能体相关功能 ==========
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

// 发送消息给智能体
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

  // 添加思考中的助手消息
  const thinkingMessage: ChatMessage = {
    id: generateMessageId(),
    role: 'assistant',
    content: '',
    timestamp: Date.now(),
    thinking: true
  }
  chatMessages.value.push(thinkingMessage)
  scrollToBottom()

  isLoading.value = true

  try {
    const userId = sessionStorage.getItem('userId') || ''
    
    // 准备上下文信息：当前待审核的申请
    const pendingContext = pendingList.value.map(apply => ({
      InfoApplyId: apply.InfoApplyId,
      Title: apply.Title,
      Content: apply.Content,
      Types: apply.Types,
      ApplyTime: apply.ApplyTime,
      ApplicantId: apply.ApplicantId
    }))

    const response = await sendQuestionToMessageAgent({
      question: question,
      userId: userId,
      conversationId: conversationId.value || undefined,
      pendingApplications: pendingContext
    })

    if (response.data.IsSuccess && response.data.Result) {
      // 更新助手消息
      thinkingMessage.content = response.data.Result.answer
      thinkingMessage.thinking = false
      if (response.data.Result.conversationId) {
        conversationId.value = response.data.Result.conversationId
      }
      // 如果有建议操作，添加到消息中
      if (response.data.Result.suggestedAction) {
        thinkingMessage.suggestedAction = response.data.Result.suggestedAction
      }
      scrollToBottom()
    } else {
      thinkingMessage.content = response.data.Message || '抱歉，我暂时无法回答您的问题，请稍后再试。'
      thinkingMessage.thinking = false
      ElMessage.error(response.data.Message || '获取回答失败')
    }
  } catch (error: any) {
    console.error('发送消息失败:', error)
    thinkingMessage.content = '抱歉，网络连接出现问题，请稍后再试。'
    thinkingMessage.thinking = false
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    isLoading.value = false
    scrollToBottom()
  }
}

// 应用智能体的建议
const applySuggestion = (suggestion: SuggestedAction) => {
  if (!suggestion) return
  
  // 自动填充审核表单
  reviewForm.FinalDecision = suggestion.action === 'approve' ? 'Y' : suggestion.action === 'reject' ? 'R' : 'Y'
  reviewForm.ReviewComments = suggestion.reviewComments || suggestion.reason
  
  ElMessage.success('已应用智能体建议，请确认后提交审核')
  
  // 滚动到审核表单区域
  nextTick(() => {
    const reviewActionEl = document.querySelector('.review-action')
    if (reviewActionEl) {
      reviewActionEl.scrollIntoView({ behavior: 'smooth', block: 'start' })
    }
  })
}

// 初始化
onMounted(() => {
  refreshPendingList()
  loadReviewedList()
})
</script>

<style scoped>
.review-container {
  padding: 20px;
}

.review-layout {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.review-main {
  flex: 1;
  min-width: 0;
}

.agent-assistant {
  width: 450px;
  flex-shrink: 0;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.reviewed-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.pending-list, .review-action, .reviewed-card {
  margin-top: 30px;
}

.review-action {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-top: 30px;
}

/* 智能体助手样式 */
.agent-card {
  height: calc(100vh - 120px);
  display: flex;
  flex-direction: column;
}

.agent-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 16px;
  padding-left: 8px;
  border-left: 4px solid #409EFF;
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
  margin-bottom: 16px;
}

.empty-features {
  text-align: left;
  color: #606266;
  font-size: 13px;
  line-height: 1.8;
  margin: 0;
  padding-left: 20px;
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
  max-width: 80%;
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

.suggested-action {
  margin-top: 12px;
  padding-top: 12px;
}

.action-suggestion {
  background: #f0f9ff;
  padding: 12px;
  border-radius: 6px;
  border-left: 3px solid #409EFF;
}

.action-suggestion strong {
  display: block;
  margin-bottom: 8px;
  color: #303133;
  font-size: 13px;
}

.action-reason {
  margin: 8px 0;
  color: #606266;
  font-size: 13px;
  line-height: 1.5;
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

.bottom-space {
  height: 200px;
}

/* 响应式布局 */
@media (max-width: 1200px) {
  .review-layout {
    flex-direction: column;
  }
  
  .agent-assistant {
    width: 100%;
  }
  
  .agent-card {
    height: 600px;
  }
}
</style>