import axios from 'axios'
import { ExamDelayHandleParams, ExamDelayHandleSearchParams, ExamDelayHandleItem, ExamDelayHandleResult } from './types'

// 获取缓考申请列表 - GET请求（支持搜索）
export const getExamDelayApplyList = (params?: ExamDelayHandleSearchParams) => {
    return axios.get<ExamDelayHandleResult>('/api/examdelay/list', { params })
}

// 提交缓考申请处理 - POST请求
export const submitExamDelayHandle = (params: ExamDelayHandleParams) => {
    return axios.post<{
        IsSuccess: boolean;
        Message?: string;
        Result: any;
    }>('/api/examdelay/submit', params)
}

// 批量同意申请 - POST请求
export const batchApproveExamDelayApply = (ids: string[]) => {
    return axios.post<{
        IsSuccess: boolean;
        Message?: string;
        Result: any;
    }>('/api/examdelay/batchApprove', { ids })
}

// 批量拒绝申请 - POST请求
export const batchRejectExamDelayApply = (ids: string[]) => {
    return axios.post<{
        IsSuccess: boolean;
        Message?: string;
        Result: any;
    }>('/api/examdelay/batchReject', { ids })
}

// 同意申请 - POST请求
export const approveExamDelayApply = (id: string, reviewComments: string, reviewerId: number) => {
    return axios.post<{
        IsSuccess: boolean;
        Message?: string;
        Result: any;
    }>('/api/examdelay/approve', { id, reviewComments, reviewerId })
}

// 拒绝申请 - POST请求
export const rejectExamDelayApply = (id: string, reviewComments: string, reviewerId: number) => {
    return axios.post<{
        IsSuccess: boolean;
        Message?: string;
        Result: any;
    }>('/api/examdelay/reject', { id, reviewComments, reviewerId })
}