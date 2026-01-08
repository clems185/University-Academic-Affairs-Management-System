<template>
  <div class="my-invigilate-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>我的监考</h2>
      <p>查看您负责监考的课程并生成座位表</p>
    </div>

    <!-- 监考课程列表 -->
    <div class="courses-section">
      <div class="section-header">
        <h3>监考课程列表</h3>
        <el-button 
          type="primary" 
          @click="fetchInvigilateCourses"
          :loading="coursesLoading">
          刷新列表
        </el-button>
      </div>

      <el-table 
        :data="coursesData" 
        stripe 
        style="width: 100%" 
        v-loading="coursesLoading">
        <el-table-column prop="CourseId" label="课程编号" width="120" />
        <el-table-column prop="CourseName" label="课程名称" width="200" />
        <el-table-column prop="ClassId" label="班级" width="100" />
        <el-table-column label="学期" width="150">
          <template #default="scope">
            {{ scope.row.Year }}年第{{ scope.row.Semester }}学期
          </template>
        </el-table-column>
        <el-table-column prop="StudentCount" label="参考人数" width="100">
          <template #default="scope">
            <el-tag type="info">{{ scope.row.StudentCount }}人</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button 
              size="small" 
              type="primary" 
              @click="openSeatingDialog(scope.row)">
              生成座位表
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 生成座位表对话框 -->
    <el-dialog
      v-model="seatingDialogVisible"
      title="生成座位表"
      width="600px">
      <div v-if="selectedCourse">
        <div class="course-info">
          <p><strong>课程：</strong>{{ selectedCourse.CourseName }} ({{ selectedCourse.CourseId }})</p>
          <p><strong>班级：</strong>{{ selectedCourse.ClassId }}</p>
          <p><strong>参考人数：</strong>{{ selectedCourse.StudentCount }}人</p>
        </div>
        
        <el-form :model="seatingForm" :rules="seatingRules" ref="seatingFormRef" label-width="100px">
          <el-form-item label="行数" prop="rows">
            <el-input-number
              v-model="seatingForm.rows"
              :min="1"
              :max="20"
              placeholder="请输入行数"
            />
          </el-form-item>
          
          <el-form-item label="列数" prop="columns">
            <el-input-number
              v-model="seatingForm.columns"
              :min="1"
              :max="20"
              placeholder="请输入列数"
            />
          </el-form-item>
          
          <el-form-item>
            <div class="seats-info">
              <span>总座位数：{{ totalSeats }}个</span>
              <span v-if="totalSeats > 0" :class="{ 'warning-text': totalSeats < selectedCourse.StudentCount }">
                {{ totalSeats >= selectedCourse.StudentCount ? '✓ 座位充足' : '⚠ 座位不足' }}
              </span>
            </div>
          </el-form-item>
        </el-form>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="seatingDialogVisible = false">取消</el-button>
          <el-button 
            type="primary" 
            @click="generateSeatingChart" 
            :loading="generateLoading"
            :disabled="totalSeats < (selectedCourse?.StudentCount || 0)">
            生成座位表
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 座位表显示对话框 -->
    <el-dialog
      v-model="seatingChartVisible"
      title="座位表"
      width="90%"
      class="seating-chart-dialog">
      <div v-if="seatingChart" class="seating-chart-container">
        <div class="chart-header">
          <h3>{{ seatingChart.CourseName }} - {{ seatingChart.ClassId }}班</h3>
          <p>
            座位布局：{{ seatingChart.Rows }}行 × {{ seatingChart.Columns }}列 
            (共{{ seatingChart.TotalSeats }}个座位，{{ seatingChart.StudentCount }}名学生)
          </p>
        </div>
        
        <!-- 讲台标识 -->
        <div class="podium">
          <span>讲台</span>
        </div>
        
        <!-- 座位表格 -->
        <div class="seating-grid">
          <table class="seating-table">
            <tbody>
              <tr v-for="(row, rowIndex) in seatingChart.Seats" :key="rowIndex">
                <td class="row-number">{{ rowIndex + 1 }}</td>
                <td 
                  v-for="(seat, colIndex) in row" 
                  :key="colIndex"
                  :class="['seat', { 'occupied': seat.StudentName, 'empty': !seat.StudentName }]">
                  <div class="seat-content">
                    <div class="seat-number">{{ seat.Row }}-{{ seat.Column }}</div>
                    <div class="student-name">{{ seat.StudentName }}</div>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        
        <!-- 图例 -->
        <div class="legend">
          <div class="legend-item">
            <span class="legend-color occupied"></span>
            <span>已分配座位</span>
          </div>
          <div class="legend-item">
            <span class="legend-color empty"></span>
            <span>空座位</span>
          </div>
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="seatingChartVisible = false">关闭</el-button>
          <el-button type="primary" @click="regenerateSeating">重新生成</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  getInvigilateCourses, 
  generateSeatingChart as generateSeatingChartAPI
} from '@/api/myInvigilate/myInvigilate'
import { 
  InvigilateCourse,
  GenerateSeatingParams,
  SeatingChart
} from '@/api/myInvigilate/types'

// 数据状态
const coursesData = ref<InvigilateCourse[]>([])
const coursesLoading = ref(false)
const generateLoading = ref(false)

// 对话框状态
const seatingDialogVisible = ref(false)
const seatingChartVisible = ref(false)
const selectedCourse = ref<InvigilateCourse | null>(null)
const seatingChart = ref<SeatingChart | null>(null)

// 座位表单
interface SeatingForm {
  rows: number
  columns: number
}

const seatingForm = ref<SeatingForm>({
  rows: 5,
  columns: 10
})

// 表单验证规则
const seatingRules = {
  rows: [
    { required: true, message: '请输入行数', trigger: 'blur' },
    { type: 'number', min: 1, max: 20, message: '行数必须在1-20之间', trigger: 'blur' }
  ],
  columns: [
    { required: true, message: '请输入列数', trigger: 'blur' },
    { type: 'number', min: 1, max: 20, message: '列数必须在1-20之间', trigger: 'blur' }
  ]
}

const seatingFormRef = ref()

// 计算总座位数
const totalSeats = computed(() => {
  return seatingForm.value.rows * seatingForm.value.columns
})

// 获取监考课程列表
const fetchInvigilateCourses = async () => {
  try {
    coursesLoading.value = true
    const currentUserId = sessionStorage.getItem('userId') || ''
    console.log('当前登录用户ID:', currentUserId) // 调试日志
    
    if (!currentUserId) {
      ElMessage.error('请先登录')
      return
    }
    
    console.log('发送请求获取监考课程，teacherId:', currentUserId) // 调试日志
    const response = await getInvigilateCourses(currentUserId)
    console.log('后端返回结果:', response.data) // 调试日志
    
    if (response.data.IsSuccess) {
      coursesData.value = response.data.Result || []
      console.log('成功获取课程数据:', coursesData.value) // 调试日志
      ElMessage.success(`监考课程列表加载成功，共${coursesData.value.length}门课程`)
    } else {
      console.error('后端返回错误:', response.data.Message) // 调试日志
      ElMessage.error(response.data.Message || '获取监考课程失败')
    }
  } catch (error) {
    console.error('获取监考课程失败:', error)
    ElMessage.error('获取监考课程失败，请检查网络连接')
  } finally {
    coursesLoading.value = false
  }
}

// 打开座位表生成对话框
const openSeatingDialog = (course: InvigilateCourse) => {
  selectedCourse.value = course
  // 重置表单
  seatingForm.value = {
    rows: 5,
    columns: 10
  }
  seatingDialogVisible.value = true
}

// 生成座位表
const generateSeatingChart = async () => {
  if (!seatingFormRef.value || !selectedCourse.value) return
  
  try {
    const valid = await seatingFormRef.value.validate()
    if (!valid) return
    
    generateLoading.value = true
    
    const params: GenerateSeatingParams = {
      CourseId: selectedCourse.value.CourseId,
      ClassId: selectedCourse.value.ClassId,
      Rows: seatingForm.value.rows,
      Columns: seatingForm.value.columns,
      StudentCount: selectedCourse.value.StudentCount
    }
    
    const response = await generateSeatingChartAPI(params)
    
    if (response.data.IsSuccess) {
      seatingChart.value = response.data.Result || null
      seatingDialogVisible.value = false
      seatingChartVisible.value = true
      ElMessage.success('座位表生成成功')
    } else {
      ElMessage.error(response.data.Message || '生成座位表失败')
    }
  } catch (error) {
    console.error('生成座位表失败:', error)
    ElMessage.error('生成座位表失败，请重试')
  } finally {
    generateLoading.value = false
  }
}

// 重新生成座位表
const regenerateSeating = () => {
  seatingChartVisible.value = false
  seatingDialogVisible.value = true
}

// 组件挂载时自动加载数据
onMounted(() => {
  fetchInvigilateCourses()
})
</script>

<style scoped>
.my-invigilate-container {
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

.courses-section {
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

.course-info {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 6px;
  margin-bottom: 20px;
}

.course-info p {
  margin: 5px 0;
  color: #606266;
}

.seats-info {
  display: flex;
  gap: 20px;
  align-items: center;
  color: #606266;
}

.warning-text {
  color: #f56c6c !important;
}

.seating-chart-container {
  text-align: center;
}

.chart-header {
  margin-bottom: 20px;
}

.chart-header h3 {
  color: #303133;
  margin-bottom: 10px;
}

.chart-header p {
  color: #606266;
  margin: 0;
}

.podium {
  background: #909399;
  color: white;
  padding: 10px;
  margin: 20px auto;
  width: 200px;
  border-radius: 6px;
  font-weight: bold;
}

.seating-grid {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}

.seating-table {
  border-collapse: separate;
  border-spacing: 8px;
}

.row-number {
  background: #e6a23c;
  color: white;
  width: 40px;
  height: 60px;
  text-align: center;
  vertical-align: middle;
  border-radius: 6px;
  font-weight: bold;
}

.seat {
  width: 80px;
  height: 60px;
  border-radius: 6px;
  border: 2px solid #dcdfe6;
  position: relative;
  cursor: pointer;
  transition: all 0.3s;
}

.seat.occupied {
  background: #67c23a;
  border-color: #67c23a;
  color: white;
}

.seat.empty {
  background: #f5f7fa;
  border-color: #dcdfe6;
  color: #909399;
}

.seat:hover {
  transform: scale(1.05);
}

.seat-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  padding: 4px;
}

.seat-number {
  font-size: 10px;
  opacity: 0.8;
}

.student-name {
  font-size: 12px;
  font-weight: bold;
  word-break: break-all;
}

.legend {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin-top: 20px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.legend-color {
  width: 20px;
  height: 20px;
  border-radius: 4px;
  border: 2px solid #dcdfe6;
}

.legend-color.occupied {
  background: #67c23a;
  border-color: #67c23a;
}

.legend-color.empty {
  background: #f5f7fa;
  border-color: #dcdfe6;
}

.dialog-footer {
  text-align: right;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .seat {
    width: 60px;
    height: 50px;
  }
  
  .student-name {
    font-size: 10px;
  }
  
  .seating-table {
    border-spacing: 4px;
  }
}
</style>