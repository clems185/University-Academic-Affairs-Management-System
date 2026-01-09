import axios from 'axios';
import { ExamApplyItem, TeacherCourse } from './types';

interface ApiResponse<T> {
  IsSuccess: boolean;
  Result: T;
  Message?: string;
}

// 获取“我组织的考试”列表
export const getExamApplyList = (teacherId: string) => {
  // 发送GET请求到后端获取该老师的所有考试申请记录
  return axios.get<ApiResponse<ExamApplyItem[]>>(
    '/api/exam-apply/list',
    { params: { teacherId } }
  );
};

// 获取老师教的所有课程
export const getTeacherCourses = (teacherId: string) => {
  // 发送GET请求，用于填充“申请新考试”弹窗里的课程下拉框
  return axios.get<ApiResponse<TeacherCourse[]>>(
    '/api/exam-apply/my-courses',
    { params: { teacherId } } // 并把它作为参数传给后端
  );
};

// 创建一个新的考试申请
export const createExamApply = (formData: FormData) => {
  const teacherId = sessionStorage.getItem('userId');// 从浏览器的会话存储中获取当前用户ID
  if (teacherId) {
    formData.append('teacherId', teacherId);// 把 teacherId 也添加到表单数据里，一起发给后端
  }

  return axios.post<ApiResponse<null>>(
    '/api/exam-apply/create',
    formData,
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
  );
};

// 获取所有教学楼
export const getBuildings = () => {
  return axios.get<ApiResponse<any[]>>('/api/exam-apply/buildings');
};

// 获取可用的教室
export const getAvailableClassrooms = (params: { startTime: string, duration: number, buildingId: string }) => {
  // 发送GET请求，根据时间段筛选出空闲的教室
  return axios.get<ApiResponse<any[]>>(
    '/api/exam-apply/available-classrooms',
    { params }
  );
};

// 获取试卷下载链接
export const getPaperDownloadUrl = (applyId: string) => {
  return `/api/exam-apply/download-paper/${applyId}`;
};

// 取消一个考试申请
export const cancelExamApply = (applyId: string) => {
  // 发送POST请求来执行取消操作
  return axios.post<ApiResponse<null>>(
    `/api/exam-apply/cancel/${applyId}`
  );
};
