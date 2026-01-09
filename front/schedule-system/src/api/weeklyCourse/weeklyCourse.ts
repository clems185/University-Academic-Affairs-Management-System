import { WeeklyScheduleItem, WeeklyScheduleSearchParams, CourseDetail } from './types';
import axios from 'axios';
import camelCase from 'lodash/camelCase';

// 响应拦截器：将后端返回的大写字段名转换为小驼峰命名
axios.interceptors.response.use((response) => {
  if (response.data && response.data.Result) {
    // 处理课程详情数据
    if (response.data.Result.CourseDetails && Array.isArray(response.data.Result.CourseDetails)) {
      response.data.Result.CourseDetails = response.data.Result.CourseDetails.map((item: any) => {
        const newItem: any = {};
        Object.keys(item).forEach(key => {
          newItem[camelCase(key)] = item[key];
        });
        return newItem;
      });
    }
    
    // 处理课表数据
    if (response.data.Result.Schedule && Array.isArray(response.data.Result.Schedule)) {
      response.data.Result.Schedule = response.data.Result.Schedule.map((item: any) => {
        const newItem: any = {};
        Object.keys(item).forEach(key => {
          newItem[camelCase(key)] = item[key];
        });
        return newItem;
      });
    }
  }
  return response;
});

interface ApiResponse {
  IsSuccess: boolean;
  Message: string;
  Result: {
    Schedule: WeeklyScheduleItem[];
    CourseDetails: CourseDetail[];
    Summary?: any;
  };
}

// 获取周课表（通用）
export const getWeeklySchedule = (params: WeeklyScheduleSearchParams) => {
  return axios.get<ApiResponse>('/api/WeeklyCourse/schedule', { params });
};

// 获取学生课表
export const getStudentSchedule = (params: WeeklyScheduleSearchParams) => {
  return axios.get<ApiResponse>('/api/WeeklyCourse/student', { params });
};

// 获取教师课表
export const getTeacherSchedule = (params: WeeklyScheduleSearchParams) => {
  return axios.get<ApiResponse>('/api/WeeklyCourse/teacher', { params });
};

// 获取管理员课表
export const getAdminSchedule = (params: WeeklyScheduleSearchParams) => {
  return axios.get<ApiResponse>('/api/WeeklyCourse/admin', { params });
};