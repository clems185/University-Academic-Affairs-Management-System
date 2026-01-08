import request from '@/utils/request'
import type { MyTeachingRes } from './types'

// 获取我的授课信息
export function getMyTeaching(params: {
  teacherId: string
  year: string
  semester: string
  week?: number
}) {
  return request({
    url: '/MyTeaching/courses',
    method: 'get',
    params
  })
}

// 获取我的授课课程班级列表
export function getMyTeachingClasses(params: {
  teacherId: string
  year: string
  semester: string
  week?: number
}) {
  return request({
    url: '/MyTeaching/classes',
    method: 'get',
    params
  })
}

// 获取指定班级的学生信息
export function getClassStudents(params: {
  teacherId: string
  year: string
  semester: string
  courseId: string
  className: string
  week?: number
}) {
  return request({
    url: '/MyTeaching/students',
    method: 'get',
    params
  })
} 