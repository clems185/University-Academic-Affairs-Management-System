import axios from 'axios'
import type { 
  MyExamResult
} from './types'

// 获取学生考试列表
export const getMyExams = (studentId: string) => {
  console.info('[MyExam] getMyExams called', {
    studentId,
    time: new Date().toISOString()
  });
  
  return axios.get<MyExamResult>(`/api/myexam/exams/${studentId}`)
    .then(res => {
      console.info('[MyExam] getMyExams success', res.data);
      return res;
    })
    .catch(err => {
      console.error('[MyExam] getMyExams error', err);
      throw err;
    });
}
