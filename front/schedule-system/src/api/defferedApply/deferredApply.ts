import axios from 'axios'
import type { 
  MyExamResult,
  DeferredApplyParams,
  DeferredApplyResult,
  MyDeferredApplyResult
} from './types'

// 获取学生考试列表（用于缓考申请）
export const getExamsForDeferred = (studentId: string) => {
  console.info('[DeferredApply] getExamsForDeferred called', {
    studentId,
    time: new Date().toISOString()
  });
  
  return axios.get<MyExamResult>(`/api/myexam/exams/${studentId}`)
    .then(res => {
      console.info('[DeferredApply] getExamsForDeferred success', res.data);
      return res;
    })
    .catch(err => {
      console.error('[DeferredApply] getExamsForDeferred error', err);
      throw err;
    });
}

// 提交缓考申请
export const submitDeferredApply = (params: DeferredApplyParams) => {
  console.info('[DeferredApply] submitDeferredApply called', {
    params,
    time: new Date().toISOString()
  });
  
  return axios.post<DeferredApplyResult>('/api/deferredapply/submit', params)
    .then(res => {
      console.info('[DeferredApply] submitDeferredApply success', res.data);
      return res;
    })
    .catch(err => {
      console.error('[DeferredApply] submitDeferredApply error', err);
      throw err;
    });
}

// 获取我的缓考申请列表
export const getMyDeferredApplies = (studentId: string) => {
  console.info('[DeferredApply] getMyDeferredApplies called', {
    studentId,
    time: new Date().toISOString()
  });
  
  return axios.get<MyDeferredApplyResult>(`/api/deferredapply/my-applies/${studentId}`)
    .then(res => {
      console.info('[DeferredApply] getMyDeferredApplies success', res.data);
      return res;
    })
    .catch(err => {
      console.error('[DeferredApply] getMyDeferredApplies error', err);
      throw err;
    });
}
