import request from '@/utils/request';
import { SelectionRound } from './types';
import axios from 'axios'
interface ApiResponse {
  IsSuccess: boolean;
  Msg: string;
  Result?: SelectionRound[]
}

// export const getAvailableSelectionRounds = () => {
//   return request<ApiResponse<SelectionRound[]>>({
//     url: '/selection/available-rounds',
//     method: 'get'
//   }).then(response => {
//     if (response.IsSuccess) {
//       return response.Result;
//     } else {
//       return Promise.reject(new Error(response.Msg));
//     }
//   });
// };


export const getAvailableSelectionRounds = (studentId:string) => {
    console.info('[Rounds] getRounds called', {
    studentId,
    time: new Date().toISOString()
  });
  return axios.get<ApiResponse>(`/api/selection/available-rounds/${studentId}`)
    .then(res => {
      console.info('[Rounds] getMyRounds success', res.data);
      return res;
    })
    .catch(err => {
      console.error('[MyExam] getMyRounds error', err);
      throw err;
    });
};


