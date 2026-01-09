<template>
  <div>
    <!-- 搜索区域 -->
    <div style="margin-bottom: 20px;">
      <el-card shadow="never">
        <template #header>
          <div class="card-header">
            <span>考试安排查询</span>
          </div>
        </template>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-input 
              v-model="searchForm.examId" 
              placeholder="请输入考试ID" 
              clearable 
              prefix-icon="Search" />
          </el-col>
          <el-col :span="6">
            <el-input 
              v-model="searchForm.courseId" 
              placeholder="请输入课程ID" 
              clearable 
              prefix-icon="Document" />
          </el-col>
          <el-col :span="6">
            <el-input 
              v-model="searchForm.timeSlotId" 
              placeholder="请输入时间段ID" 
              clearable 
              prefix-icon="Clock" />
          </el-col>
          <el-col :span="6">
            <el-input 
              v-model="searchForm.classroomId" 
              placeholder="请输入教室ID" 
              clearable 
              prefix-icon="Location" />
          </el-col>
        </el-row>
        <el-row :gutter="20" style="margin-top: 15px;">
          <el-col :span="6">
            <el-select 
              v-model="searchForm.semester" 
              placeholder="请选择学期" 
              clearable 
              style="width: 100%">
              <el-option label="春季学期" :value="0" />
              <el-option label="秋季学期" :value="1" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-input 
              v-model.number="searchForm.year" 
              placeholder="请输入学年（如：2024）" 
              clearable 
              type="number"
              prefix-icon="Calendar" />
          </el-col>
          <el-col :span="12" style="display: flex; justify-content: center; align-items: center; gap: 12px;">
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-col>
        </el-row>
      </el-card>
    </div>

    <!-- 统计信息 -->
    <div style="margin-bottom: 20px;">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card shadow="hover" style="text-align: center;">
            <div style="font-size: 24px; font-weight: bold; color: #409EFF;">{{ tableData.length }}</div>
            <div style="margin-top: 5px; color: #909399;">考试总数</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" style="text-align: center;">
            <div style="font-size: 24px; font-weight: bold; color: #67C23A;">{{ uniqueCourses.length }}</div>
            <div style="margin-top: 5px; color: #909399;">涉及课程</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" style="text-align: center;">
            <div style="font-size: 24px; font-weight: bold; color: #E6A23C;">{{ uniqueClassrooms.length }}</div>
            <div style="margin-top: 5px; color: #909399;">使用教室</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" style="text-align: center;">
            <div style="font-size: 24px; font-weight: bold; color: #F56C6C;">{{ uniqueTimeSlots.length }}</div>
            <div style="margin-top: 5px; color: #909399;">时间段数</div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table 
        :data="tableData" 
        stripe 
        style="width: 100%" 
        v-loading="loading"
        :default-sort="{ prop: 'year', order: 'descending' }"
        height="500">
        <el-table-column prop="examId" label="考试ID" width="120" sortable />
        <el-table-column prop="courseId" label="课程ID" width="120" sortable />
        <el-table-column prop="courseName" label="课程名称" width="150" sortable />
        <el-table-column prop="timeSlotId" label="时间段ID" width="120" sortable />
        <el-table-column prop="timeSlotDisplay" label="时间段" width="200" sortable />
        <el-table-column prop="classroomId" label="教室ID" width="120" sortable />
        <el-table-column prop="year" label="学年" width="100" sortable />
        <el-table-column prop="semesterDisplay" label="学期" width="120" sortable />
        <el-table-column label="操作" width="150" fixed="right">
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
    <el-dialog v-model="detailDialogVisible" title="考试详情" width="600px">
      <el-descriptions :column="2" border v-if="currentExam">
        <el-descriptions-item label="考试ID">{{ currentExam.examId }}</el-descriptions-item>
        <el-descriptions-item label="课程ID">{{ currentExam.courseId }}</el-descriptions-item>
        <el-descriptions-item label="课程名称">{{ currentExam.courseName }}</el-descriptions-item>
        <el-descriptions-item label="时间段ID">{{ currentExam.timeSlotId }}</el-descriptions-item>
        <el-descriptions-item label="时间段">{{ currentExam.timeSlotDisplay }}</el-descriptions-item>
        <el-descriptions-item label="教室ID">{{ currentExam.classroomId }}</el-descriptions-item>
        <el-descriptions-item label="学年">{{ currentExam.year }}</el-descriptions-item>
        <el-descriptions-item label="学期">{{ currentExam.semesterDisplay }}</el-descriptions-item>
      </el-descriptions>
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
import { getExamScheduleList } from '@/api/examSchedule/examSchedule'
import { ExamScheduleSearchParams, ExamScheduleItem } from '@/api/examSchedule/types'
import { ElMessage } from 'element-plus'

const allData = ref<ExamScheduleItem[]>([])
const filteredData = ref<ExamScheduleItem[]>([])
const tableData = ref<ExamScheduleItem[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const detailDialogVisible = ref(false)
const currentExam = ref<ExamScheduleItem | null>(null)

// 搜索表单
const searchForm = ref<ExamScheduleSearchParams>({
  examId: undefined,
  courseId: undefined,
  timeSlotId: undefined,
  classroomId: undefined,
  semester: undefined,
  year: undefined
})

// 计算属性：统计信息
const uniqueCourses = computed(() => {
  return [...new Set(filteredData.value.map(item => item.courseId))]
})

const uniqueClassrooms = computed(() => {
  return [...new Set(filteredData.value.map(item => item.classroomId))]
})

const uniqueTimeSlots = computed(() => {
  return [...new Set(filteredData.value.map(item => item.timeSlotId))]
})

// 获取考试安排列表
const fetchExamScheduleList = async () => {
  try {
    loading.value = true
    const response = await getExamScheduleList()
    if (response.data.IsSuccess) {
      allData.value = response.data.Result
      applyFilters()
    } else {
      ElMessage.error(response.data.Message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取考试安排列表失败:', error)
    ElMessage.error('获取数据失败，请检查网络连接')
    allData.value = []
  } finally {
    loading.value = false
  }
}

// 应用筛选条件
const applyFilters = () => {
  let filtered = [...allData.value]

  if (searchForm.value.examId) {
    filtered = filtered.filter(item => 
      item.examId.toLowerCase().includes(searchForm.value.examId!.toLowerCase())
    )
  }

  if (searchForm.value.courseId) {
    filtered = filtered.filter(item => 
      item.courseId.toLowerCase().includes(searchForm.value.courseId!.toLowerCase())
    )
  }

  if (searchForm.value.timeSlotId) {
    filtered = filtered.filter(item => 
      item.timeSlotId.toLowerCase().includes(searchForm.value.timeSlotId!.toLowerCase())
    )
  }

  if (searchForm.value.classroomId) {
    filtered = filtered.filter(item => 
      item.classroomId.toLowerCase().includes(searchForm.value.classroomId!.toLowerCase())
    )
  }

  if (searchForm.value.semester !== undefined) {
    filtered = filtered.filter(item => item.semester === searchForm.value.semester)
  }

  if (searchForm.value.year) {
    filtered = filtered.filter(item => item.year === searchForm.value.year)
  }

  filteredData.value = filtered
  currentPage.value = 1
  updateTableData()
}

// 更新表格数据（分页）
const updateTableData = () => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  tableData.value = filteredData.value.slice(start, end)
}

// 搜索
const handleSearch = () => {
  applyFilters()
}

// 重置搜索
const handleReset = () => {
  searchForm.value = {
    examId: undefined,
    courseId: undefined,
    timeSlotId: undefined,
    classroomId: undefined,
    semester: undefined,
    year: undefined
  }
  applyFilters()
}

// 查看详情
const handleViewDetail = (row: ExamScheduleItem) => {
  currentExam.value = row
  detailDialogVisible.value = true
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
  fetchExamScheduleList()
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