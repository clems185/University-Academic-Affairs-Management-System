<template>
  <div>
    <!-- 搜索区域 -->
    <div style="margin-bottom: 20px;">
      <el-card shadow="never">
        <template #header>
          <div class="card-header">
            <span>试卷中心查询</span>
          </div>
        </template>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-input 
              v-model="searchForm.examId" 
              placeholder="请输入考试ID" 
              clearable 
              prefix-icon="Search" />
          </el-col>
          <el-col :span="10">
            <el-input 
              v-model="searchForm.comment" 
              placeholder="请输入特殊情况说明" 
              clearable 
              prefix-icon="Document" />
          </el-col>
          <el-col :span="6" style="display: flex; justify-content: center; align-items: center; gap: 12px;">
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-col>
        </el-row>
      </el-card>
    </div>



    <!-- 表格 -->
    <el-card shadow="never">
      <el-table 
        :data="tableData" 
        stripe 
        style="width: 100%" 
        v-loading="loading"
        :default-sort="{ prop: 'examId', order: 'ascending' }"
        height="500">
        <el-table-column prop="examId" label="考试ID" width="200" sortable />
        <el-table-column prop="comment" label="特殊情况说明" min-width="300" show-overflow-tooltip />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" @click="handleViewDetail(scope.row)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div style="margin-top: 20px; text-align: right;">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="filteredData.length"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="试卷详情" width="800px" :close-on-click-modal="false">
      <div v-loading="detailLoading">
        <div v-if="currentPaperDetail">
          <el-descriptions :column="1" border style="margin-bottom: 20px;">
            <el-descriptions-item label="考试ID">{{ currentPaperDetail.examId }}</el-descriptions-item>
            <el-descriptions-item label="特殊情况说明">
              {{ currentPaperDetail.comment || '无' }}
            </el-descriptions-item>
            <el-descriptions-item label="试卷状态">
              <el-tag :type="currentPaperDetail.hasPaper ? 'success' : 'danger'">
                {{ currentPaperDetail.hasPaper ? '有试卷' : '无试卷' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
          
          <!-- 试卷下载区域 -->
          <div v-if="currentPaperDetail.hasPaper" style="text-align: center; padding: 20px;">
            <h4 style="margin-bottom: 15px;">试卷下载</h4>
            <el-button 
              type="primary" 
              size="large" 
              :loading="downloadLoading"
              @click="handleDownloadPaper">
              下载试卷PDF
            </el-button>
            <p style="margin-top: 10px; color: #909399; font-size: 14px;">
              点击按钮下载PDF格式的试卷文件
            </p>
          </div>
          
          <div v-else style="text-align: center; color: #909399; padding: 20px;">
            <el-icon size="48" color="#c0c4cc"><DocumentRemove /></el-icon>
            <p style="margin-top: 10px;">该考试未上传试卷</p>
          </div>
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
import { ref, onMounted, computed } from 'vue'
import { getExamPaperList, getExamPaperDetail, downloadExamPaper } from '@/api/paper/paper'
import { ExamPaperSearchParams, ExamPaperItem, ExamPaperDetail } from '@/api/paper/types'
import { ElMessage } from 'element-plus'
import { DocumentRemove } from '@element-plus/icons-vue'

const allData = ref<ExamPaperItem[]>([])
const filteredData = ref<ExamPaperItem[]>([])
const tableData = ref<ExamPaperItem[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const detailDialogVisible = ref(false)
const currentPaper = ref<ExamPaperItem | null>(null)
const currentPaperDetail = ref<ExamPaperDetail | null>(null)
const detailLoading = ref(false)
const downloadLoading = ref(false)

// 搜索表单
const searchForm = ref<ExamPaperSearchParams>({
  examId: undefined,
  comment: undefined
})





// 获取试卷列表
const fetchExamPaperList = async () => {
  try {
    loading.value = true
    const response = await getExamPaperList(searchForm.value)
    if (response.data.IsSuccess) {
      allData.value = response.data.Result
      filteredData.value = response.data.Result
      currentPage.value = 1
      updateTableData()
    } else {
      ElMessage.error(response.data.Message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取试卷列表失败:', error)
    ElMessage.error('获取数据失败，请检查网络连接')
    allData.value = []
    filteredData.value = []
  } finally {
    loading.value = false
  }
}



// 更新表格数据（分页）
const updateTableData = () => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  tableData.value = filteredData.value.slice(start, end)
}

// 搜索
const handleSearch = () => {
  fetchExamPaperList()
}

// 重置搜索
const handleReset = () => {
  searchForm.value = {
    examId: undefined,
    comment: undefined
  }
  fetchExamPaperList()
}

// 查看详情
const handleViewDetail = async (row: ExamPaperItem) => {
  try {
    detailLoading.value = true
    currentPaper.value = row
    
    const response = await getExamPaperDetail(row.examId)
    if (response.data.IsSuccess) {
      currentPaperDetail.value = response.data.Result
      detailDialogVisible.value = true
    } else {
      ElMessage.error(response.data.Message || '获取试卷详情失败')
    }
  } catch (error) {
    console.error('获取试卷详情失败:', error)
    ElMessage.error('获取试卷详情失败，请检查网络连接')
  } finally {
    detailLoading.value = false
  }
}

// 下载试卷PDF
const handleDownloadPaper = async () => {
  if (!currentPaper.value) {
    ElMessage.error('未选择试卷')
    return
  }
  
  try {
    downloadLoading.value = true
    const response = await downloadExamPaper(currentPaper.value.examId)
    
    if (response.data.IsSuccess && response.data.Result) {
      const { fileContent, fileName } = response.data.Result
      
      // 将Base64转换为Blob
      const byteCharacters = atob(fileContent)
      const byteNumbers = new Array(byteCharacters.length)
      for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i)
      }
      const byteArray = new Uint8Array(byteNumbers)
      const blob = new Blob([byteArray], { type: 'application/pdf' })
      
      // 创建下载链接
      const url = URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = fileName || `试卷_${currentPaper.value.examId}.pdf`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      URL.revokeObjectURL(url)
      
      ElMessage.success('试卷下载成功')
    } else {
      ElMessage.error(response.data.Message || '下载失败')
    }
  } catch (error) {
    console.error('下载试卷失败:', error)
    ElMessage.error('下载失败，请检查网络连接')
  } finally {
    downloadLoading.value = false
  }
}



// 分页大小改变
const handleSizeChange = (newSize: number) => {
  pageSize.value = newSize
  updateTableData()
}

// 当前页改变
const handleCurrentChange = (newPage: number) => {
  currentPage.value = newPage
  updateTableData()
}

// 组件挂载时获取数据
onMounted(() => {
  fetchExamPaperList()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>