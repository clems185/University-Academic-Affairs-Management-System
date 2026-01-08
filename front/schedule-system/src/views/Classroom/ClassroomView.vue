<template>
  <div class="classroom-query-container">
    <!-- 查询条件 -->
    <el-card class="query-card">
      <template #header>
        <div class="card-header">
          <span>教室使用情况查询</span>
        </div>
      </template>
      
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="校区">
          <el-select 
            v-model="queryForm.campusId" 
            placeholder="请选择校区"
            clearable
            @change="handleCampusChange"
            style="width: 200px"
          >
            <el-option
              v-for="campus in campusList"
              :key="campus.ClassroomId"
              :label="campus.CampusName"
              :value="campus.ClassroomId"
            ></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="教学楼">
          <el-select 
            v-model="queryForm.buildingId" 
            placeholder="请选择教学楼"
            :disabled="!queryForm.campusId"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="building in buildingList"
              :key="building.ClassroomId"
              :label="building.BuildingName"
              :value="building.ClassroomId"
            ></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="教室类型">
          <el-select 
            v-model="queryForm.classroomType" 
            placeholder="请选择教室类型"
            clearable
            style="width: 200px"
          >
            <el-option label="普通教室" value="普通教室"></el-option>
            <el-option label="实验室" value="实验室"></el-option>
            <el-option label="机房" value="机房"></el-option>
            <el-option label="体育场所" value="体育场所"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="查询日期">
          <el-date-picker
            v-model="queryForm.date"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 200px"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleQuery" :loading="loading">
            查询
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 查询结果 -->
    <div v-if="!loading && hasQueried" class="result-container">
      <el-card class="result-card" v-if="classroomList.length > 0">
        <template #header>
          <div class="card-header">
            <span>查询结果（共{{ totalCount }}条数据，当前第{{ pageNum }}页）</span>
          </div>
        </template>
        
        <div class="table-scroll-container">
          <el-table :data="classroomList" stripe style="width: 100%">
            <el-table-column prop="ClassroomId" label="教室ID" width="120" />
            <el-table-column prop="CampusName" label="校区" width="100" />
            <el-table-column prop="BuildingName" label="教学楼" width="120" />
            <el-table-column prop="RoomNumber" label="教室编号" width="100" />
            <el-table-column prop="Type" label="教室类型" width="100" />
            <el-table-column prop="Capacity" label="容量" width="80" />
            <el-table-column prop="IsOccupied" label="使用状态" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.IsOccupied ? 'danger' : 'success'">
                  {{ scope.row.IsOccupied ? '已占用' : '空闲' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="课程信息" min-width="200">
              <template #default="scope">
                <div v-if="scope.row.CourseInfo" class="course-info">
                  <div><strong>课程：</strong>{{ scope.row.CourseInfo.CourseName }}</div>
                  <div><strong>班级：</strong>{{ scope.row.CourseInfo.ClassName }}</div>
                  <div><strong>教师：</strong>{{ scope.row.CourseInfo.TeacherName }}</div>
                  <div><strong>时间：</strong>{{ scope.row.CourseInfo.StartTime }}-{{ scope.row.CourseInfo.EndTime }}</div>
                </div>
                <span v-else class="no-course">无课程安排</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
        
        <div class="pagination-container">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="totalCount"
          ></el-pagination>
        </div>
      </el-card>

      <el-card v-else class="empty-card">
        <div class="empty-content">
          <el-icon><Document /></el-icon>
          <span>未找到符合条件的教室</span>
        </div>
      </el-card>
    </div>

    <el-card v-if="loading" class="loading-card">
      <div class="loading-content">
        <el-icon class="is-loading"><Loading /></el-icon>
        <span>正在查询教室使用情况...</span>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Document, Loading } from '@element-plus/icons-vue'
import { 
  getCampusList, 
  getBuildingList, 
  queryClassroomUsage 
} from '@/api/classroom/classroom'

// 校区数据类型
interface CampusItem {
  ClassroomId: string
  CampusName: string
  RoomNumber?: any
  BuildingName?: any
  Capacity?: number
  Type?: any
  IsOccupied?: boolean
  CourseInfo?: any
}

// 教学楼数据类型
interface BuildingItem {
  ClassroomId: string
  BuildingName: string
}

// 教室数据类型
interface ClassroomItem {
  ClassroomId: string
  RoomNumber: string
  BuildingName: string
  CampusName: string
  Capacity: number
  Type: string
  IsOccupied: boolean
  CourseInfo: {
    CourseName: string
    ClassName: string
    TeacherName: string
    StartTime: string
    EndTime: string
  } | null
}

// 查询参数类型
interface ClassroomQueryParams {
  campusId: string
  buildingId: string
  classroomType: string
  date: string
  timeSlotId: string
}

// 状态管理
const queryForm = ref<ClassroomQueryParams>({
  campusId: '',
  buildingId: '',
  classroomType: '',
  date: '',
  timeSlotId: ''
})

// 分页状态
const pageNum = ref(1)
const pageSize = ref(10)
const totalCount = ref(0)

// 数据列表
const campusList = ref<CampusItem[]>([])
const buildingList = ref<BuildingItem[]>([])
const rawClassroomList = ref<ClassroomItem[]>([])
const classroomList = computed(() => {
  const startIndex = (pageNum.value - 1) * pageSize.value
  const endIndex = startIndex + pageSize.value
  return rawClassroomList.value.slice(startIndex, endIndex)
})

// 其他状态
const loading = ref(false)
const hasQueried = ref(false)

// 分页事件处理
const handleSizeChange = (newSize: number) => {
  pageSize.value = newSize
  pageNum.value = 1
}

const handleCurrentChange = (newPage: number) => {
  pageNum.value = newPage
}

// 获取校区列表
const loadCampusList = async () => {
  try {
    const response = await getCampusList()
    if (!Array.isArray(response)) {
      throw new Error('校区数据格式错误，应为数组')
    }
    campusList.value = response
  } catch (error) {
    console.error('获取校区列表失败:', error)
    ElMessage.error('获取校区列表失败')
    campusList.value = []
  }
}

// 获取教学楼列表
const loadBuildingList = async (campusId: string) => {
  try {
    const trimmedCampusId = campusId.trim()
    const buildings = await getBuildingList(trimmedCampusId)
    buildingList.value = Array.isArray(buildings) ? buildings : []
  } catch (error) {
    console.error('获取教学楼列表失败:', error)
    ElMessage.error('获取教学楼列表失败')
  }
}

// 校区变化处理
const handleCampusChange = (campusId: string) => {
  queryForm.value.buildingId = ''
  buildingList.value = []
  if (campusId) {
    loadBuildingList(campusId)
  }
}

// 查询教室使用情况
const handleQuery = async () => {
  try {
    loading.value = true
    hasQueried.value = true
    
    const response = await queryClassroomUsage(queryForm.value)
    
    if (!Array.isArray(response)) {
      throw new Error('后端返回数据格式错误，应为数组')
    }
    
    rawClassroomList.value = response
    totalCount.value = response.length
    pageNum.value = 1
    
    ElMessage.success(`查询完成，共找到 ${totalCount.value} 个教室`)
  } catch (error) {
    console.error('查询教室使用情况失败:', error)
    ElMessage.error('查询失败，请稍后重试')
    rawClassroomList.value = []
    totalCount.value = 0
  } finally {
    loading.value = false
  }
}

// 重置查询条件：关键修复
const handleReset = async () => {
  // 1. 重置查询表单为初始状态
  queryForm.value = {
    campusId: '',
    buildingId: '',
    classroomType: '',
    date: '',
    timeSlotId: ''
  }
  
  // 2. 重置分页状态
  pageNum.value = 1
  pageSize.value = 10
  
  // 3. 清空教学楼列表（校区未选择时）
  buildingList.value = []
  
  // 4. 关键修复：重新执行默认查询（无筛选条件）
  try {
    loading.value = true
    const response = await queryClassroomUsage(queryForm.value) // 传递空条件
    
    if (Array.isArray(response)) {
      rawClassroomList.value = response
      totalCount.value = response.length
      hasQueried.value = true
      ElMessage.success('已重置查询条件，显示全部教室')
    }
  } catch (error) {
    console.error('重置查询失败:', error)
    ElMessage.error('重置失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 监听校区变化
watch(() => queryForm.value.campusId, (newCampusId) => {
  if (newCampusId) {
    handleCampusChange(newCampusId)
  } else {
    queryForm.value.buildingId = ''
    buildingList.value = []
  }
})

// 页面加载
onMounted(async () => {
  try {
    await loadCampusList()
    await handleQuery()
  } catch (error) {
    console.error('页面加载失败:', error)
  }
})
</script>

<style scoped>
/* 样式保持不变 */
.classroom-query-container {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.query-card {
  margin-bottom: 20px;
}

.card-header {
  font-weight: 600;
  font-size: 16px;
}

.query-form {
  margin-top: 16px;
}

.result-container {
  max-height: calc(100vh - 220px);
  overflow-y: auto;
  padding-right: 8px;
  scrollbar-width: thin;
}

.result-container::-webkit-scrollbar {
  width: 6px;
}

.result-container::-webkit-scrollbar-thumb {
  background-color: #ddd;
  border-radius: 3px;
}

.result-container::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}

.result-card {
  margin-bottom: 20px;
  overflow: hidden;
}

.table-scroll-container {
  overflow-x: auto;
  max-width: 100%;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
  padding: 0 16px 16px;
  position: sticky;
  bottom: 0;
  background-color: #fff;
  z-index: 10;
  border-top: 1px solid #eee;
  padding-top: 16px;
}

.empty-card, .loading-card {
  margin-top: 20px;
}

.empty-content, .loading-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px;
  color: #999;
}

.empty-content .el-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.loading-content .el-icon {
  font-size: 32px;
  margin-bottom: 16px;
}

.course-info {
  font-size: 12px;
  line-height: 1.5;
}

.course-info div {
  margin-bottom: 4px;
}

.no-course {
  color: #999;
  font-style: italic;
}

.el-form-item {
  margin-bottom: 16px;
}

@media (max-width: 768px) {
  .classroom-query-container {
    padding: 10px;
  }
  
  .result-container {
    max-height: calc(100vh - 300px);
  }
  
  .el-form-item {
    width: 100%;
    margin-bottom: 10px;
  }
  
  .el-select, .el-date-picker {
    width: 100% !important;
  }
}
</style>
