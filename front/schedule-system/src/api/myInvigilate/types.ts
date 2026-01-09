// 监考课程信息
export interface InvigilateCourse {
  CourseId: string
  CourseName: string
  ClassId: string
  Semester: number
  Year: number
  StudentCount: number // 固定50人
}

// 生成座位表参数
export interface GenerateSeatingParams {
  CourseId: string
  ClassId: string
  Rows: number
  Columns: number
  StudentCount: number
}

// 座位信息
export interface SeatInfo {
  Row: number
  Column: number
  StudentName: string
}

// 座位表结果
export interface SeatingChart {
  CourseId: string
  CourseName: string
  ClassId: string
  Rows: number
  Columns: number
  TotalSeats: number
  StudentCount: number
  Seats: SeatInfo[][]
}

// API 返回结果
export interface InvigilateCoursesResult {
  IsSuccess: boolean
  Result?: InvigilateCourse[]
  Message?: string
  Msg?: string
}

export interface SeatingChartResult {
  IsSuccess: boolean
  Result?: SeatingChart
  Message?: string
  Msg?: string
}