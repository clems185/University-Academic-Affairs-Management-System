import request from '@/utils/request'
import { ScheduleItem, CourseInfo, SemesterParams } from '@/api/semesterCourse/types'


interface ApiResponse<T> {
  IsSuccess: boolean;
  Msg: string;
  Result: T;
}

// 获取学生已选课程列表
export const getSchedule = (params: SemesterParams) => {
  return request<ApiResponse<ScheduleItem[]>>({
    url: '/semestercourse/schedule',
    method: 'get',
    params
  }).then(response => {
    if (response.IsSuccess) {
      return response.Result;
    } else {
      return Promise.reject(new Error(response.Msg));
    }
  });
};

// 获取可选课程列表
export const getCourses = (params: SemesterParams) => {
  return request<ApiResponse<CourseInfo[]>>({
    url:'/semestercourse/courses',
    method: 'get',
    params
  }).then(response => {
    if (response.IsSuccess) {
      return response.Result;
    } else {
      return Promise.reject(new Error(response.Msg));
    }
  });
};