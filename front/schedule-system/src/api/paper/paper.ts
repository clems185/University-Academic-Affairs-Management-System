import axios from 'axios'
import { ExamPaperSearchParams, ExamPaperResult, ExamPaperDetailResult, ExamPaperDownloadResult } from './types'

// 获取试卷列表 - GET请求（支持搜索）
export const getExamPaperList = (params?: ExamPaperSearchParams) => {
    return axios.get<ExamPaperResult>('/api/exampaper/list', { params })
}

// 获取试卷详情 - GET请求
export const getExamPaperDetail = (examId: string) => {
    return axios.get<ExamPaperDetailResult>(`/api/exampaper/detail/${examId}`)
}

// 下载试卷文件 - GET请求
export const downloadExamPaper = (examId: string) => {
    return axios.get<ExamPaperDownloadResult>(`/api/exampaper/download/${examId}`)
}

// 测试接口 - GET请求
export const testExamPaperApi = () => {
    return axios.get<{
        IsSuccess: boolean;
        Message?: string;
        Result: any;
    }>('/api/exampaper/test')
}