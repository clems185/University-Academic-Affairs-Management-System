// src/api/teacherInfo/types.ts
// 教师信息参数类型
export interface TeacherInfoParams {
    TeacherId?: string;
    TeacherName: string;
    TeacherSex: string;
    MajorId: string;
    TeacherLevel: string;
    StartYear?: number;
    WorkYear?: number;
    Email: string;
    Telephone: string;
}

// 教师信息结果类型
export interface TeacherInfoResult {
  Id: string;
  Name: string;
  Gender: string;
  MajorName: string;
  TypeDetail: string;
  Email: string;
  Phone: string;
}

// 专业信息类型
export interface MajorInfo {
    MajorId: string;
    MajorName: string;
}

// 复用学生信息的部分类型
export type { StudentInfoParams, StudentInfoResult } from '@/api/studentInfo/types';