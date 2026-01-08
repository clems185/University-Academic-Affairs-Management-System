import axios from 'axios'
import type { 
  ClassroomQueryParams, 
  ClassroomUsage, 
  CampusInfo, 
  BuildingInfo, 
  TimeSlotInfo 
} from './types'

const baseURL = '/api'

// 获取校区列表
export const getCampusList = async (): Promise<CampusInfo[]> => {
  try {
    const response = await axios.get(`${baseURL}/classroom/campus`)
    if (response.data.success) {
      return response.data.data
    }
    return []
  } catch (error) {
    console.error('获取校区列表失败:', error)
    return []
  }
}

// 根据校区获取教学楼列表
export const getBuildingList = async (campusId: string): Promise<BuildingInfo[]> => {
  try {
    const response = await axios.get(`${baseURL}/classroom/buildings/${campusId}`)
    if (response.data.success) {
      return response.data.data
    }
    return []
  } catch (error) {
    console.error('获取教学楼列表失败:', error)
    return []
  }
}

// 获取时间段列表
export const getTimeSlotList = async (): Promise<TimeSlotInfo[]> => {
  try {
    const response = await axios.get(`${baseURL}/classroom/timeslots`)
    if (response.data.success) {
      return response.data.data
    }
    return []
  } catch (error) {
    console.error('获取时间段列表失败:', error)
    return []
  }
}

// 查询教室使用情况
export const queryClassroomUsage = async (params: ClassroomQueryParams): Promise<ClassroomUsage[]> => {
  try {
    const response = await axios.get(`${baseURL}/classroom/usage`, { params })
    if (response.data.success) {
      return response.data.data
    }
    return []
  } catch (error) {
    console.error('查询教室使用情况失败:', error)
    return []
  }
}

// 获取教室详细信息
export const getClassroomInfo = async (classroomId: string): Promise<ClassroomUsage | null> => {
  try {
    const response = await axios.get(`${baseURL}/classroom/info/${classroomId}`)
    if (response.data.success) {
      return response.data.data
    }
    return null
  } catch (error) {
    console.error('获取教室信息失败:', error)
    return null
  }
}
