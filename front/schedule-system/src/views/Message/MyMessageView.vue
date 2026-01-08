<template>
  <div class="my-information-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>我的消息</h2>
      <p>查看系统通知、公告等各类消息</p>
    </div>

    <!-- 消息列表区域 -->
    <div class="information-section">
      <div class="section-header">
        <h3>消息列表</h3>
        <!-- 刷新按钮添加提示 -->
        <el-tooltip content="重新加载消息列表，获取最新数据" placement="left">
          <el-button 
            type="primary" 
            @click="fetchMyInformations"
            :loading="infoLoading">
            刷新列表
          </el-button>
        </el-tooltip>
      </div>

      <!-- 筛选条件 - 只保留开始时间 -->
      <div class="filter-section">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-select 
              v-model="selectedTypes" 
              multiple 
              placeholder="选择消息类型"
              clearable
              @change="handleTypeChange">
              <el-option 
                v-for="type in validTypes" 
                :key="type" 
                :label="type" 
                :value="type" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-date-picker
              v-model="startTime"
              type="date"
              placeholder="选择开始时间"
              value-format="YYYY-MM-DD"
              :max-date="maxDate"
              @change="handleDateChange"
            />
          </el-col>
          <el-col :span="4">
            <el-button 
              type="default" 
              @click="resetFilter">
              重置筛选
            </el-button>
          </el-col>
        </el-row>
      </div>

      <!-- 数据统计 -->
      <div class="stats-section" v-if="informationResult?.Result?.data && informationResult.Result.data.length > 0">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-statistic title="消息总数" :value="informationResult.Result?.total" suffix="条" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="当前页" :value="informationResult.Result?.page" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="消息类型" :value="uniqueTypesCount" suffix="种" />
          </el-col>
        </el-row>
      </div>

      <!-- 消息表格 -->
      <el-table 
        :data="informationResult?.Result?.data || []" 
        stripe 
        style="width: 100%" 
        v-loading="infoLoading"
        empty-text="暂无消息">
        
        <el-table-column prop="InfoId" label="消息ID" width="120" />
        
        <el-table-column prop="Types" label="消息类型" width="100">
          <template #default="scope">
            <el-tag 
              :type="getTagType(scope.row.Types)" 
              size="small">
              {{ scope.row.Types }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="Title" label="标题" min-width="200">
          <template #default="scope">
            <span class="title-link" @click="showDetail(scope.row)">{{ scope.row.Title }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="发布时间" width="180">
          <template #default="scope">
            <div>
              <i class="el-icon-time"></i> {{ formatTime(scope.row.PublishTime) }}
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button 
              size="small" 
              type="text" 
              @click="showDetail(scope.row)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页控件 -->
      <div class="pagination-section" v-if="informationResult?.Result && informationResult.Result.total > 0">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="informationResult.Result?.page || 1"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="informationResult.Result?.pageSize || 10"
          layout="total, sizes, prev, pager, next, jumper"
          :total="informationResult.Result?.total || 0">
        </el-pagination>
      </div>

      <!-- 底部空白区域，确保分页导航完整显示 -->
      <div class="bottom-space"></div>
    </div>

    <!-- 消息详情对话框 - 优化布局，突出主要内容 -->
    <el-dialog
      v-model="detailDialogVisible"
      :title="selectedInformation?.Title || '消息详情'"
      width="800px"
      :height="800"
      :fullscreen="false">
      <div v-if="selectedInformation" class="dialog-content-container">
        <!-- 精简信息，只保留关键项 -->
        <div class="detail-header">
          <div class="detail-meta">
            <el-tag 
              :type="getTagType(selectedInformation.Types)" 
              size="small"
              class="type-tag">
              {{ selectedInformation.Types }}
            </el-tag>
            <span class="publish-time">
              <i class="el-icon-time"></i> {{ formatTime(selectedInformation.PublishTime) }}
            </span>
          </div>
        </div>
        
        <!-- 扩大内容区域，作为主体 -->
        <div class="main-content">
          <pre>{{ selectedInformation.Content }}</pre>
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElTooltip } from 'element-plus'
import { getMyInformations } from '@/api/myMessage/myMessage'
import type { MyInformation, MyInformationResult } from '@/api/myMessage/types'

// 有效消息类型（与后端ValidTypes对应）
const validTypes = ["通知", "公告", "新闻", "文件", "活动"] as const

// 类型与标签样式映射
const typeTagTypeMap = {
  "通知": "primary",
  "公告": "success",
  "新闻": "info",
  "文件": "warning",
  "活动": "danger"
} as const

// 获取标签类型
const getTagType = (type: string) => {
  if (validTypes.includes(type as any)) {
    return typeTagTypeMap[type as keyof typeof typeTagTypeMap]
  }
  return 'info' // 默认类型
}

// 数据状态
const informationResult = ref<MyInformationResult>()
const infoLoading = ref(false)

// 筛选条件 - 只保留开始时间
const selectedTypes = ref<string[]>([])
const startTime = ref<string>() // 仅保留开始时间
const currentPage = ref(1)
const pageSize = ref(10)
const maxDate = ref(new Date()) // 最大可选时间为当前时间

// 对话框状态
const detailDialogVisible = ref(false)
const selectedInformation = ref<MyInformation | null>(null)

// 计算属性
const uniqueTypesCount = computed(() => {
  if (!informationResult.value?.Result?.data) return 0
  const types = new Set(informationResult.value.Result.data.map(info => info.Types))
  return types.size
})

// 格式化时间
const formatTime = (timeString: string) => {
  const date = new Date(timeString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 获取消息列表 - 仅使用开始时间
const fetchMyInformations = async () => {
  try {
    infoLoading.value = true
    // 处理类型参数（转为逗号分隔字符串）
    const types = selectedTypes.value.length > 0 
      ? selectedTypes.value.join(',') 
      : undefined
    
    const response = await getMyInformations(
      types,
      startTime.value, // 仅传递开始时间
      currentPage.value,
      pageSize.value
    )
    
    if (response.IsSuccess) {
      informationResult.value = response
      ElMessage.success(`消息加载成功，共${response.Result?.total || 0}条`)
    } else {
      console.error('后端返回错误:', response.Message || response.Msg)
      ElMessage.error(response.Message || response.Msg || '获取消息失败')
    }
  } catch (error) {
    console.error('获取消息列表失败:', error)
    ElMessage.error('获取消息列表失败，请检查网络连接')
  } finally {
    infoLoading.value = false
  }
}

// 显示消息详情
const showDetail = (info: MyInformation) => {
  selectedInformation.value = info
  detailDialogVisible.value = true
}

// 筛选条件变化处理
const handleTypeChange = () => {
  currentPage.value = 1 // 类型变化重置页码
  fetchMyInformations()
}

// 开始时间变化处理
const handleDateChange = () => {
  currentPage.value = 1 // 日期变化重置页码
  fetchMyInformations()
}

// 分页处理
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1 // 重置到第一页
  fetchMyInformations()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchMyInformations()
}

// 重置筛选条件
const resetFilter = () => {
  selectedTypes.value = []
  startTime.value = undefined // 重置开始时间
  currentPage.value = 1
  fetchMyInformations()
}

// 组件挂载时加载数据
onMounted(() => {
  fetchMyInformations()
})
</script>

<style scoped>
.my-information-container {
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

.information-section {
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

.filter-section {
  margin-bottom: 20px;
  padding: 10px 0;
}

.stats-section {
  margin-bottom: 20px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 6px;
}

.pagination-section {
  margin-top: 20px;
  text-align: right;
}

.bottom-space {
  height: 100px;
}

.title-link {
  color: #409eff;
  cursor: pointer;
  text-decoration: underline;
}

.title-link:hover {
  color: #66b1ff;
}

/* 详情对话框样式重构 */
.dialog-content-container {
  height: calc(100% - 50px);
  display: flex;
  flex-direction: column;
  padding: 10px 0;
}

/* 精简的头部信息 */
.detail-header {
  padding: 0 10px 15px;
  border-bottom: 1px solid #eee;
  margin-bottom: 15px;
}

.detail-meta {
  display: flex;
  align-items: center;
  gap: 15px;
  color: #606266;
  font-size: 14px;
}

.type-tag {
  vertical-align: middle;
}

/* 主要内容区域 - 作为主体放大显示 */
.main-content {
  flex: 1;
  padding: 10px;
  overflow-y: auto; /* 添加滚动条 */
  background-color: #fafafa;
  border-radius: 6px;
  min-height: 400px; /* 确保有足够大的阅读区域 */
  line-height: 1.8;
  font-size: 15px;
}

.main-content pre {
  white-space: pre-wrap;
  word-break: break-all;
  margin: 0;
  font-family: inherit;
}

:deep(.el-dialog__body) {
  padding: 15px 20px;
  height: 600px; /* 固定对话框内容区域高度 */
  overflow: hidden;
}

:deep(.el-dialog__header) {
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
}

:deep(.el-dialog__title) {
  font-size: 18px;
  font-weight: 600;
}

.el-statistic {
  text-align: center;
}
</style>
    