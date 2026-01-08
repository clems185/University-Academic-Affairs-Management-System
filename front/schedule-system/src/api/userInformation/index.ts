import request from '@/utils/request';
import { ProfileInfo, ApiResponse } from './types';

// 调整为与myexam功能一致的传参方式，通过URL参数传递用户ID和类型
export const getProfileInfo = (userId: string, userType: string) => {
  return request<ApiResponse<ProfileInfo>>({
    url: `/Profile/get-profile?userId=${userId}&userType=${userType}`,
    method: 'get'
  }).then(response => {
    if (response.IsSuccess) {
      return response.Result;
    } else {
      return Promise.reject(new Error(response.Msg));
    }
  });
};

// 在现有文件中添加以下代码
export const submitInfoUpdateApply = (data: {
  userId: string;
  userType: string;
  newProfile?: string;
  newPhoto?: string; // Base64格式
  information: string; // 修改原因
}) => {
  return request<ApiResponse<boolean>>({
    url: `/Profile/submit-update-apply`,
    method: 'post',
    data
  }).then(response => {
    if (response.IsSuccess) {
      return response.Result;
    } else {
      return Promise.reject(new Error(response.Msg));
    }
  });
};