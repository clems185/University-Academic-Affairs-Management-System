// src/api/allinfo/allinfo.ts
import axios from 'axios';
import { UserInfoResult } from './types';

const API_BASE = '/api/allInfo';


// 统一的用户列表获取函数
export const getUserList = (params?: {
  name?: string;
  majorName?: string;
  gender?: string;
  userTypes?: string; // 改为字符串类型，逗号分隔
  pageIndex?: number;
  pageSize?: number;
}) => {
  return axios.get<{
    IsSuccess: boolean;
    Message?: string;
    Result: UserInfoResult[];
    Total: number;
  }>(`${API_BASE}/list`, { params });
};

// 获取学生用户
export const getStudents = (params?: { 
    name?: string; 
    majorName?: string;
    gender?: string;
    pageIndex?: number;
    pageSize?: number;
}) => {
    return axios.get<{
        IsSuccess: boolean;
        Message?: string;
        Result: UserInfoResult[];
        Total: number;
    }>(`${API_BASE}/list`, { 
        params: {
            ...params,
            userType: "1"  // 学生类型
        }
    });
};

// 获取教师用户
export const getTeachers = (params?: { 
    name?: string; 
    majorName?: string;
    gender?: string;
    pageIndex?: number;
    pageSize?: number;
}) => {
    return axios.get<{
        IsSuccess: boolean;
        Message?: string;
        Result: UserInfoResult[];
        Total: number;
    }>(`${API_BASE}/list`, { 
        params: {
            ...params,
            userType: "2"  // 教师类型
        }
    });
};

// 获取管理员用户
export const getAdmins = (params?: { 
    name?: string; 
    majorName?: string;
    gender?: string;
    pageIndex?: number;
    pageSize?: number;
}) => {
    return axios.get<{
        IsSuccess: boolean;
        Message?: string;
        Result: UserInfoResult[];
        Total: number;
    }>(`${API_BASE}/list`, { 
        params: {
            ...params,
            userType: "3"  // 管理员类型
        }
    });
};

// 获取专业列表
export const getMajorList = () => {
    return axios.get<{ 
        IsSuccess: boolean; 
        Message?: string;
        Result: { MajorId: string; MajorName: string }[] 
    }>('/api/major/list');
};