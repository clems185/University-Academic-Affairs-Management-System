import request from '@/utils/request';
import { 
  AdminPublicSelectionDto, 
  CreateSelectionDto, 
  UpdateSelectionDto, 
  ValidateSelectionTimeDto,
  ValidateTimeResult,
  ApiResponse,
  ProcessSelectionResultDto,
  SelectionCourseDto,
  SelectionMajorDto
} from './types';

// 获取所有选课时间段
export const getAllSelections = () => {
  return request<ApiResponse<AdminPublicSelectionDto[]>>({
    url: '/admin/selection/all',
    method: 'get'
  });
};

// 获取选课时间段详情
export const getSelectionById = (selectionId: string) => {
  return request<ApiResponse<AdminPublicSelectionDto>>({
    url: `/admin/selection/${selectionId}`,
    method: 'get'
  });
};

// 创建选课时间段
export const createSelection = (data: CreateSelectionDto) => {
  return request<ApiResponse<boolean>>({
    url: '/admin/selection/create',
    method: 'post',
    data
  });
};

// 更新选课时间段
export const updateSelection = (data: UpdateSelectionDto) => {
  return request<ApiResponse<boolean>>({
    url: '/admin/selection/update',
    method: 'put',
    data
  });
};

// 删除选课时间段
export const deleteSelection = (selectionId: string) => {
  return request<ApiResponse<boolean>>({
    url: `/admin/selection/delete/${selectionId}`,
    method: 'delete'
  });
};

// 为选课时间段添加课程
export const addCoursesToSelection = (selectionId: string, courseIds: string[]) => {
  return request<ApiResponse<boolean>>({
    url: `/admin/selection/${selectionId}/courses/add`,
    method: 'post',
    data: courseIds
  });
};

// 从选课时间段移除课程
export const removeCoursesFromSelection = (selectionId: string, courseIds: string[]) => {
  return request<ApiResponse<boolean>>({
    url: `/admin/selection/${selectionId}/courses/remove`,
    method: 'post',
    data: courseIds
  });
};

// 为选课时间段添加专业
export const addMajorsToSelection = (selectionId: string, majorIds: string[]) => {
  return request<ApiResponse<boolean>>({
    url: `/admin/selection/${selectionId}/majors/add`,
    method: 'post',
    data: majorIds
  });
};

// 从选课时间段移除专业
export const removeMajorsFromSelection = (selectionId: string, majorIds: string[]) => {
  return request<ApiResponse<boolean>>({
    url: `/admin/selection/${selectionId}/majors/remove`,
    method: 'post',
    data: majorIds
  });
};

// 验证选课时间段时间是否有效
export const validateSelectionTime = (data: ValidateSelectionTimeDto) => {
  return request<ApiResponse<ValidateTimeResult>>({
    url: '/admin/selection/validate-time',
    method: 'post',
    data
  });
};

// 处理选课结果
export const processSelectionResult = (data: ProcessSelectionResultDto) => {
  return request<ApiResponse<boolean>>({
    url: '/admin/selection/process',
    method: 'post',
    data
  });
};

// 获取所有课程
export const getAllCourses = () => {
  return request<ApiResponse<SelectionCourseDto[]>>({
    url: '/admin/selection/courses/all',
    method: 'get'
  });
};

// 获取所有专业
export const getAllMajors = () => {
  return request<ApiResponse<SelectionMajorDto[]>>({
    url: '/admin/selection/majors/all',
    method: 'get'
  });
};

// 添加获取所有课程ID的API
export const getAllCourseIds = () => {
  return request<ApiResponse<string[]>>({
    url: '/admin/selection/courses/all-ids',
    method: 'get'
  });
};

// 添加获取所有专业ID的API
export const getAllMajorIds = () => {
  return request<ApiResponse<string[]>>({
    url: '/admin/selection/majors/all-ids',
    method: 'get'
  });
};