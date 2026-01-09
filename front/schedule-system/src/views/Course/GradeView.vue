<template>
  <div class="grade-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>成绩登记</h2>
      <p>管理您教授课程的学生成绩</p>
    </div>

    <!-- 课程列表 -->
    <div class="courses-section">
      <div class="section-header">
        <h3>课程列表</h3>
        <el-button 
          type="primary" 
          @click="fetchGradeCourses"
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
        <el-table-column prop="StudentCount" label="学生人数" width="100">
          <template #default="scope">
            <el-tag type="info">{{ scope.row.StudentCount }}人</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300">
          <template #default="scope">
            <el-button 
              size="small" 
              type="success" 
              @click="downloadExcelTemplate(scope.row)"
              :loading="downloadLoading === scope.row.CourseId">
              下载模板
            </el-button>
            <el-button 
              size="small" 
              type="primary" 
              @click="openUploadDialog(scope.row)"
              style="margin-left: 10px;">
              上传成绩
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 上传成绩对话框 -->
    <el-dialog
      v-model="uploadDialogVisible"
      title="上传成绩"
      width="500px">
      <div v-if="selectedCourse">
        <div class="course-info">
          <p><strong>课程：</strong>{{ selectedCourse.CourseName }} ({{ selectedCourse.CourseId }})</p>
          <p><strong>班级：</strong>{{ selectedCourse.ClassId }}</p>
          <p><strong>学期：</strong>{{ selectedCourse.Year }}年第{{ selectedCourse.Semester }}学期</p>
          <p><strong>学生人数：</strong>{{ selectedCourse.StudentCount }}人</p>
        </div>
        
        <el-form :model="uploadForm" :rules="uploadRules" ref="uploadFormRef" label-width="100px">
          <el-form-item label="成绩文件" prop="file">
            <el-upload
              ref="uploadRef"
              :auto-upload="false"
              :limit="1"
              accept=".csv,.xls,.xlsx"
              :on-change="handleFileChange"
              :on-remove="handleFileRemove"
              :file-list="fileList">
              <el-button type="primary">选择文件</el-button>
              <template #tip>
                <div class="el-upload__tip">
                  支持CSV、XLS、XLSX格式，文件大小不超过5MB<br/>
                  请确保文件格式为：学生ID,成绩
                </div>
              </template>
            </el-upload>
          </el-form-item>
        </el-form>

        <div class="upload-instructions">
          <h4>上传说明：</h4>
          <ol>
            <li>先点击"下载模板"获取包含学生ID的Excel模板</li>
            <li>在模板的"成绩"列填入对应学生的成绩</li>
            <li>保存文件后在此处上传</li>
            <li>系统将自动更新学生成绩到数据库</li>
          </ol>
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="uploadDialogVisible = false">取消</el-button>
          <el-button 
            type="primary" 
            @click="uploadGradeFile" 
            :loading="uploadLoading"
            :disabled="!uploadForm.file">
            上传成绩
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 上传结果对话框 -->
    <el-dialog
      v-model="resultDialogVisible"
      title="上传结果"
      width="600px">
      <div v-if="uploadResult">
        <div class="result-summary">
          <el-alert
            :title="uploadResult.Summary"
            :type="uploadResult.FailureCount > 0 ? 'warning' : 'success'"
            show-icon
            :closable="false">
          </el-alert>
        </div>

        <div class="result-details" style="margin-top: 20px;">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-statistic title="成功更新" :value="uploadResult.SuccessCount" suffix="条" />
            </el-col>
            <el-col :span="12">
              <el-statistic title="失败记录" :value="uploadResult.FailureCount" suffix="条" />
            </el-col>
          </el-row>
        </div>

        <div v-if="uploadResult.FailureCount > 0 && uploadResult.FailureDetails" class="failure-details" style="margin-top: 20px;">
          <h4>失败详情：</h4>
          <el-scrollbar height="200px">
            <ul>
              <li v-for="(detail, index) in uploadResult.FailureDetails" :key="index" class="failure-item">
                {{ detail }}
              </li>
            </ul>
          </el-scrollbar>
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="resultDialogVisible = false">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  getGradeCourses, 
  generateGradeExcel,
  uploadGradeExcel
} from '@/api/grade/grade'
import { 
  GradeCourse,
  GenerateExcelParams,
  UploadGradeParams
} from '@/api/grade/types'

// 数据状态
const coursesData = ref<GradeCourse[]>([])
const coursesLoading = ref(false)
const downloadLoading = ref('')
const uploadLoading = ref(false)

// 对话框状态
const uploadDialogVisible = ref(false)
const resultDialogVisible = ref(false)
const selectedCourse = ref<GradeCourse | null>(null)
const uploadResult = ref<any>(null)

// 上传表单
interface UploadForm {
  file: File | null
}

const uploadForm = ref<UploadForm>({
  file: null
})

const fileList = ref<any[]>([])

// 表单验证规则
const uploadRules = {
  file: [
    { required: true, message: '请选择成绩文件', trigger: 'change' }
  ]
}

const uploadFormRef = ref()
const uploadRef = ref()

// 获取成绩登记课程列表
const fetchGradeCourses = async () => {
  try {
    coursesLoading.value = true
    const currentUserId = sessionStorage.getItem('userId') || ''
    console.log('当前登录用户ID:', currentUserId) // 调试日志
    
    if (!currentUserId) {
      ElMessage.error('请先登录')
      return
    }
    
    console.log('发送请求获取成绩登记课程，teacherId:', currentUserId) // 调试日志
    const response = await getGradeCourses(currentUserId)
    console.log('后端返回结果:', response.data) // 调试日志
    
    if (response.data.IsSuccess) {
      coursesData.value = response.data.Result || []
      console.log('成功获取课程数据:', coursesData.value) // 调试日志
      ElMessage.success(`成绩登记课程列表加载成功，共${coursesData.value.length}门课程`)
    } else {
      console.error('后端返回错误:', response.data.Message) // 调试日志
      ElMessage.error(response.data.Message || '获取成绩登记课程失败')
    }
  } catch (error) {
    console.error('获取成绩登记课程失败:', error)
    ElMessage.error('获取成绩登记课程失败，请检查网络连接')
  } finally {
    coursesLoading.value = false
  }
}

// 下载Excel模板
const downloadExcelTemplate = async (course: GradeCourse) => {
  try {
    downloadLoading.value = course.CourseId
    
    const params: GenerateExcelParams = {
      CourseId: course.CourseId,
      ClassId: course.ClassId,
      Semester: course.Semester, // 保持原始值，数据库中就是0
      Year: course.Year
    }
    
    console.log('生成Excel模板参数:', params) // 调试日志
    console.log('发送请求到后端...') // 调试日志
    console.log('请求详情:', {
      courseId: params.CourseId,
      classId: params.ClassId, 
      semester: params.Semester,
      year: params.Year
    })
    
    try {
      const response = await generateGradeExcel(params)
      console.log('后端响应:', response) // 调试日志
      
      // 检查响应数据
      if (!response.data) {
        console.error('后端返回空数据')
        throw new Error('后端返回空数据')
      }
      
      console.log('响应数据类型:', typeof response.data)
      console.log('响应数据是否为Blob:', response.data instanceof Blob)
      console.log('响应数据大小:', response.data.size || 'unknown')
      
      // 确保response.data是Blob类型
      let blob: Blob
      if (response.data instanceof Blob) {
        blob = response.data
      } else {
        // 如果不是Blob，尝试转换
        blob = new Blob([response.data], { type: 'text/csv; charset=utf-8' })
      }
      
      // 检查blob大小
      if (blob.size === 0) {
        console.error('生成的blob大小为0')
        throw new Error('生成的文件为空')
      }
      
      // 创建下载链接
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = `成绩登记表_${course.CourseId}_${course.ClassId}_${new Date().toISOString().slice(0, 10)}.csv`
      link.style.display = 'none'
      document.body.appendChild(link)
      link.click()
      
      // 清理
      setTimeout(() => {
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
      }, 100)
      
      console.log('Excel模板下载完成') // 调试日志
      ElMessage.success('Excel模板下载成功')
      
    } catch (apiError: any) {
      console.error('后端Excel生成失败:', apiError)
      
      // 显示具体错误信息而不是使用测试数据
      let errorMsg = '下载Excel模板失败'
      if (apiError.response) {
        errorMsg += `: ${apiError.response.status} ${apiError.response.data?.message || apiError.response.statusText}`
      } else if (apiError.message) {
        errorMsg += `: ${apiError.message}`
      }
      
      ElMessage.error(errorMsg)
    }
    
  } catch (error: any) {
    console.error('下载Excel模板失败:', error)
    console.error('错误详情:', {
      message: error.message,
      response: error.response?.data,
      status: error.response?.status,
      statusText: error.response?.statusText
    })
    
    let errorMessage = '下载Excel模板失败，请重试'
    if (error.response?.status === 401) {
      errorMessage = '认证失败，请重新登录'
    } else if (error.response?.status === 404) {
      errorMessage = '接口不存在，请检查后端服务'
    } else if (error.response?.status === 500) {
      errorMessage = '服务器内部错误'
    } else if (error.response?.data) {
      errorMessage = error.response.data.message || error.response.data || errorMessage
    }
    
    ElMessage.error(errorMessage)
  } finally {
    downloadLoading.value = ''
  }
}

// 打开上传对话框
const openUploadDialog = (course: GradeCourse) => {
  selectedCourse.value = course
  uploadForm.value.file = null
  fileList.value = []
  uploadDialogVisible.value = true
}

// 文件选择处理
const handleFileChange = (file: any) => {
  uploadForm.value.file = file.raw
  fileList.value = [file]
}

// 文件移除处理
const handleFileRemove = () => {
  uploadForm.value.file = null
  fileList.value = []
}

// 上传成绩文件
const uploadGradeFile = async () => {
  console.log('uploadGradeFile 函数开始执行')
  
  if (!uploadFormRef.value) {
    console.error('uploadFormRef 不存在')
    ElMessage.error('表单引用错误')
    return
  }
  
  if (!selectedCourse.value) {
    console.error('selectedCourse 不存在')
    ElMessage.error('未选择课程')
    return
  }
  
  try {
    console.log('开始验证表单...')
    const valid = await uploadFormRef.value.validate()
    console.log('表单验证结果:', valid)
    
    if (!valid) {
      console.warn('表单验证失败')
      return
    }
    
    if (!uploadForm.value.file) {
      console.error('没有选择文件')
      ElMessage.error('请选择成绩文件')
      return
    }
    
    console.log('开始上传，设置loading状态...')
    uploadLoading.value = true
    
    const params: UploadGradeParams = {
      CourseId: selectedCourse.value.CourseId,
      ClassId: selectedCourse.value.ClassId,
      Semester: selectedCourse.value.Semester, // 保持原始值，数据库中就是0
      Year: selectedCourse.value.Year,
      ExcelFile: uploadForm.value.file
    }
    
    console.log('上传成绩文件参数:', params) // 调试日志
    console.log('文件信息:', {
      name: uploadForm.value.file.name,
      size: uploadForm.value.file.size,
      type: uploadForm.value.file.type
    }) // 调试日志
    
    // 验证文件格式
    const fileName = uploadForm.value.file.name.toLowerCase()
    if (!fileName.endsWith('.csv') && !fileName.endsWith('.xls') && !fileName.endsWith('.xlsx')) {
      throw new Error('文件格式不支持，请选择CSV、XLS或XLSX格式的文件')
    }
    
    // 验证文件大小
    if (uploadForm.value.file.size > 5 * 1024 * 1024) {
      throw new Error('文件太大，请确保文件小于5MB')
    }
    
    console.log('开始调用API...')
    const response = await uploadGradeExcel(params)
    console.log('API调用完成，响应:', response)
    console.log('响应数据:', response.data)
    
    if (response.data && response.data.IsSuccess) {
      console.log('上传成功，处理结果...')
      uploadResult.value = response.data.Result || {
        SuccessCount: 0,
        FailureCount: 0,
        FailureDetails: [],
        Summary: '上传完成'
      }
      uploadDialogVisible.value = false
      resultDialogVisible.value = true
      ElMessage.success('成绩上传成功')
    } else {
      console.error('上传失败，后端返回错误:', response.data)
      const errorMsg = response.data?.Message || response.data?.Msg || '成绩上传失败'
      ElMessage.error(errorMsg)
    }
  } catch (error: any) {
    console.error('上传成绩文件异常:', error)
    
    // 详细的错误信息记录
    if (error.response) {
      console.error('HTTP响应错误:', {
        status: error.response.status,
        statusText: error.response.statusText,
        data: error.response.data,
        headers: error.response.headers
      })
    } else if (error.request) {
      console.error('网络请求错误:', error.request)
    } else {
      console.error('其他错误:', error.message)
    }
    
    let errorMessage = '上传成绩文件失败，请重试'
    
    if (error.message) {
      errorMessage = error.message
    } else if (error.response?.status === 401) {
      errorMessage = '认证失败，请重新登录'
    } else if (error.response?.status === 404) {
      errorMessage = '接口不存在，请检查后端服务'
    } else if (error.response?.status === 413) {
      errorMessage = '文件太大，请确保文件小于5MB'
    } else if (error.response?.status === 400) {
      errorMessage = '请求参数错误：' + (error.response.data?.Message || error.response.data || '未知错误')
    } else if (error.response?.status === 500) {
      errorMessage = '服务器内部错误：' + (error.response.data?.Message || error.response.data || '未知错误')
    } else if (error.response?.data) {
      if (typeof error.response.data === 'string') {
        errorMessage = error.response.data
      } else {
        errorMessage = error.response.data.Message || error.response.data.Msg || error.response.data.message || errorMessage
      }
    }
    
    console.error('最终错误消息:', errorMessage)
    ElMessage.error(errorMessage)
  } finally {
    console.log('上传流程结束，重置loading状态')
    uploadLoading.value = false
  }
}

// 组件挂载时自动加载数据
onMounted(() => {
  fetchGradeCourses()
})
</script>

<style scoped>
.grade-container {
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

.upload-instructions {
  margin-top: 20px;
  padding: 15px;
  background: #f0f9ff;
  border-radius: 6px;
  border-left: 4px solid #409eff;
}

.upload-instructions h4 {
  color: #303133;
  margin-bottom: 10px;
}

.upload-instructions ol {
  margin: 0;
  padding-left: 20px;
}

.upload-instructions li {
  margin-bottom: 5px;
  color: #606266;
}

.result-summary {
  margin-bottom: 20px;
}

.result-details {
  text-align: center;
}

.failure-details {
  border-top: 1px solid #ebeef5;
  padding-top: 20px;
}

.failure-details h4 {
  color: #303133;
  margin-bottom: 10px;
}

.failure-details ul {
  margin: 0;
  padding-left: 20px;
}

.failure-item {
  margin-bottom: 5px;
  color: #f56c6c;
}

.dialog-footer {
  text-align: right;
}

.el-upload__tip {
  color: #606266;
  font-size: 12px;
  line-height: 1.4;
}
</style>