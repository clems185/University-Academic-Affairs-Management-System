import axios from 'axios'
import type { 
  GradeCoursesResult, 
  GenerateExcelParams,
  UploadGradeParams,
  UploadGradeResult
} from './types'

// 获取教师成绩登记课程列表
export const getGradeCourses = (teacherId: string) => {
  console.info('[Grade] getGradeCourses called', {
    teacherId,
    time: new Date().toISOString()
  });
  
  return axios.get<GradeCoursesResult>(`/api/grade/courses/${teacherId}`)
    .then(res => {
      console.info('[Grade] getGradeCourses success', res.data);
      return res;
    })
    .catch(err => {
      console.error('[Grade] getGradeCourses error', err);
      throw err;
    });
}

// 生成成绩登记Excel模板
export const generateGradeExcel = (params: GenerateExcelParams) => {
  console.info('[Grade] generateGradeExcel called', {
    params,
    time: new Date().toISOString()
  });
  
  // 获取token
  const token = sessionStorage.getItem('token')
  
  return axios.post(`/api/grade/generate-excel`, params, {
    responseType: 'blob', // 重要：指定响应类型为blob
    headers: {
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    }
  })
    .then(res => {
      console.info('[Grade] generateGradeExcel success, response type:', typeof res.data);
      console.info('[Grade] generateGradeExcel response size:', res.data.size);
      return res;
    })
    .catch(err => {
      console.error('[Grade] generateGradeExcel error', err);
      console.error('[Grade] Error details:', {
        status: err.response?.status,
        statusText: err.response?.statusText,
        data: err.response?.data
      });
      throw err;
    });
}

// 上传成绩Excel文件
export const uploadGradeExcel = (params: UploadGradeParams) => {
  console.info('[Grade] uploadGradeExcel called', {
    courseId: params.CourseId,
    classId: params.ClassId,
    fileSize: params.ExcelFile?.size,
    fileName: params.ExcelFile?.name,
    time: new Date().toISOString()
  });
  
  const formData = new FormData()
  formData.append('CourseId', params.CourseId)
  formData.append('ClassId', params.ClassId)
  formData.append('Semester', params.Semester.toString())
  formData.append('Year', params.Year.toString())
  
  if (params.ExcelFile) {
    formData.append('ExcelFile', params.ExcelFile)
    console.info('[Grade] File appended to FormData:', {
      name: params.ExcelFile.name,
      size: params.ExcelFile.size,
      type: params.ExcelFile.type
    });
  }
  
  // 获取token并手动设置（避免axios拦截器与multipart冲突）
  const token = sessionStorage.getItem('token')
  
  const config = {
    headers: {} as any,
    timeout: 30000, // 30秒超时
  }
  
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`
  }
  
  // 不设置Content-Type，让浏览器自动设置multipart/form-data边界
  
  return axios.post<UploadGradeResult>('/api/grade/upload-excel', formData, config)
    .then(res => {
      console.info('[Grade] uploadGradeExcel success', res.data);
      return res;
    })
    .catch(err => {
      console.error('[Grade] uploadGradeExcel error', err);
      console.error('[Grade] Upload error details:', {
        status: err.response?.status,
        statusText: err.response?.statusText,
        data: err.response?.data
      });
      throw err;
    });
}
