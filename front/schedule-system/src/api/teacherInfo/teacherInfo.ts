// src/api/teacherInfo/teacherInfo.ts
import axios from 'axios';
import { TeacherInfoResult, MajorInfo } from './types';

const API_BASE = '/api/allInfo';

// 获取教师列表
export const getTeacherInfoList = (params?: { 
    name?: string; 
    majorName?: string;
    gender?: string;
    pageIndex?: number;
    pageSize?: number;
}) => {
    return axios.get<{
        IsSuccess: boolean;
        Message?: string;
        Result: TeacherInfoResult[];
        Total: number;
    }>(`${API_BASE}/list`, { 
        params: {
            ...params,
            userType: "2"  // 教师类型
        }
    });
};

// 获取教师详情
export const getTeacherInfo = (teacherId: string) => {
    return axios.get<{
        IsSuccess: boolean;
        Message?: string;
        Result: any; // 可根据需要定义详细类型
    }>(`${API_BASE}/teacher/detail/${teacherId}`);
};

// 获取专业列表 (复用学生信息的API)
export { getMajorList } from '@/api/studentInfo/studentInfo';