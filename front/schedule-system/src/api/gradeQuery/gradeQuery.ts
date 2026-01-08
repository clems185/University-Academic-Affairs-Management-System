import axios from 'axios'
import { GradeQueryParams, GradeQueryResult, GradeSummaryResult } from './types'

// 根据学生ID查询成绩
export const getGradesByStudent = (studentId: string) => {
    return axios.get<{ success: boolean; data: GradeQueryResult[]; message: string }>(`/api/gradeQuery/student/${studentId}`)
}

// 根据条件查询成绩
export const getGradesByCondition = (params: GradeQueryParams) => {
    return axios.get<{ success: boolean; data: GradeQueryResult[]; message: string }>('/api/gradeQuery/search', { params })
}

// 获取学生成绩汇总
export const getGradeSummary = (studentId: string) => {
    return axios.get<{ success: boolean; data: GradeSummaryResult; message: string }>(`/api/gradeQuery/summary/${studentId}`)
}

// 获取学生按学期分组的成绩
export const getGradesBySemester = (studentId: string) => {
    return axios.get<{ success: boolean; data: Record<string, GradeQueryResult[]>; message: string }>(`/api/gradeQuery/semester/${studentId}`)
}
