import request from '@/utils/request'
import type { CourseInfo, ScheduleRequest } from './types'

export const fetchAllCourses = () => {
  return request({
    url: 'ScheduleCourse/GetAllCourses',
    method: 'get'
  })
}

export const scheduleCourses = (data: ScheduleRequest) => {
  return request({
    url: 'ScheduleCourse/Schedule',
    method: 'post',
    data
  })
}