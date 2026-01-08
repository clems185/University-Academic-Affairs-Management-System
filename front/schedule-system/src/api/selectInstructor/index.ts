import request from '@/utils/request';
import { InstructorSelectionRound, InstructorInfo } from './types';
import axios from 'axios'
interface ApiResponse<T> {
  IsSuccess: boolean;
  Msg: string;
  Result: T;
}

export const getAvailableInstructorSelections = () => {
  return request<ApiResponse<InstructorSelectionRound[]>>({
    url: '/InstructorSelection/available-rounds',
    method: 'get'
  }).then(response => {
    if (response.IsSuccess) {
      return response.Result;
    } else {
      return Promise.reject(new Error(response.Msg));
    }
  });
};

export const getAllInstructors = (studentId: string) => {
  return axios.get<ApiResponse<InstructorInfo[]>>(`/api/InstructorSelection/all-instructors/${studentId}`);
};


export const selectInstructor = (studentId: string,teacherId: string) => {
  return axios.post(`/api/InstructorSelection/select/${studentId}/${teacherId}`);
};

export const saveInstructorSelection = (studentId: string) => {
  return axios.post(`/api/InstructorSelection/save/${studentId}`);
};

export const withdrawInstructor = (studentId: string) => {

  return axios.post(`/api/InstructorSelection/withdraw/${studentId}`);
};

export const getInstructorSelectionById = (studentId: string,selectionId: string) => {
  return axios.get<ApiResponse<InstructorSelectionRound>>(`/api/InstructorSelection/information/${selectionId}/${studentId}`);
}