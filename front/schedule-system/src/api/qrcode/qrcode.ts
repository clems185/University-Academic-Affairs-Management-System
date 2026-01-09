import axios from 'axios'
import { SignReq ,Sign,ClassList} from './types';
// 获取某课程某次签到的学生列表
export const getSignInStudents = (signReq:SignReq) => {
  return axios.get<{ IsSuccess: boolean; Result: any[] }>(
    `/api/qrcode/signin-list?CourseId=${signReq.CourseId}&ClassId=${signReq.ClassId}&SignId=${signReq.SignId}`
  )
}

// 学生扫码签到
export const studentSignIn = (sign:Sign) => {
  return axios.post<{ IsSuccess: boolean; Message?: string }>(
    `/api/qrcode/signin`,
    sign
  )
} 
export const getClassList = (TeacherId: string) => {
  return axios.get<{ IsSuccess: boolean; Result: ClassList[] }>(
    `/api/qrcode/class-list?TeacherId=${TeacherId}`
  )

}

