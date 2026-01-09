// api/studentSelectCourse/index.ts
import request from '@/utils/request';
import { SelectCourseItem, ClassInfo } from './types';
import axios from 'axios'

interface ApiResponseSelect {
  IsSuccess: boolean;
  Msg: string;
  Result: SelectCourseItem[];
}

interface ApiResponseClass {
  IsSuccess: boolean;
  Msg: string;
  Result: ClassInfo[];
}

// 获取学生已选课程列表
export const getStudentCourses = (studentId: string, selectionId?: string) => {
  const url = selectionId 
    ? `/api/selectcourse/getcourse/${studentId}/${selectionId}`
    : `/api/selectcourse/getcourse/${studentId}`;
  
  return axios.get<ApiResponseSelect>(url);
};

// 获取可选课程列表
export const getAvailableCourses = (studentId: string, selectionId: string) => {
  return axios.get<ApiResponseSelect>(`/api/selectcourse/get-available-course/${studentId}/${selectionId}`);
};

// 获取指定课程的所有班级
export const getClassesByCourse = (studentId: string, courseId: string) => {
  return axios.get<ApiResponseClass>(`/api/selectcourse/get/${studentId}/${courseId}`);
};

// 添加课程（未选择状态）
export const addCourse = (studentId: string, courseId: string, selectionId: string) => {
  return axios.post(`/api/selectcourse/add-course/${studentId}/${courseId}/${selectionId}`);
};

// 选择班级
export const selectClass = (studentId: string, courseId: string, classId: string) => {
  return axios.post(`/api/selectcourse/select-class/${studentId}/${courseId}/${classId}`)
    .then(response => {
      if (response.data.IsSuccess) {
        return response;
      } else {
        // 如果后端返回成功但IsSuccess为false，抛出错误
        throw new Error(response.data.Msg || '选择班级失败');
      }
    });
};
// 移除课程
export const removeCourse = (studentId: string, courseId: string) => {
  return axios.delete(`/api/selectcourse/del/${studentId}/${courseId}`);
};

// 保存课表
export const saveCourses = (studentId: string) => {
  return axios.post(`/api/selectcourse/save/${studentId}`);
};