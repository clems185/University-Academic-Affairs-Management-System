import axios from 'axios'
import { ExamScheduleSearchParams, ExamScheduleResult } from './types'

// 获取考试安排列表 - GET请求（支持搜索）
export const getExamScheduleList = (params?: ExamScheduleSearchParams) => {
    return axios.get<ExamScheduleResult>('/api/exam/schedule', { params })
}

// 测试接口
export const testExamSchedule = () => {
    return axios.get<{
        IsSuccess: boolean;
        Message?: string;
        Result: any;
    }>('/api/exam/test')
}