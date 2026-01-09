import axios from 'axios'
import type { 
  InvigilateCoursesResult, 
  GenerateSeatingParams,
  SeatingChartResult
} from './types'

// 获取教师监考课程列表
export const getInvigilateCourses = (teacherId: string) => {
  console.info('[MyInvigilate] getInvigilateCourses called', {
    teacherId,
    time: new Date().toISOString()
  });
  
  return axios.get<InvigilateCoursesResult>(`/api/myinvigilate/courses/${teacherId}`)
    .then(res => {
      console.info('[MyInvigilate] getInvigilateCourses success', res.data);
      return res;
    })
    .catch(err => {
      console.error('[MyInvigilate] getInvigilateCourses error', err);
      throw err;
    });
}

// 生成座位表
export const generateSeatingChart = (params: GenerateSeatingParams) => {
  console.info('[MyInvigilate] generateSeatingChart called', {
    params,
    time: new Date().toISOString()
  });
  
  return axios.post<SeatingChartResult>('/api/myinvigilate/generate-seating', params)
    .then(res => {
      console.info('[MyInvigilate] generateSeatingChart success', res.data);
      return res;
    })
    .catch(err => {
      console.error('[MyInvigilate] generateSeatingChart error', err);
      throw err;
    });
}
