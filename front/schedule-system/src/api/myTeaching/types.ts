// 我的授课响应类型
export interface MyTeachingRes {
  courseId: string
  courseName: string
  className: string
  classroom: string
  dayOfWeek: number
  startTime: string
  endTime: string
  year: string
  semester: string
  week: number
  period: number
  studentId: string
  studentName: string
  studentSex: string
  studentGPA: number
  studentCredits: number
  studentMajorId: string
  studentMajorName: string
  studentStartYear: number
  courseType: string
  assessmentType: string
  credits: number
  startWeek: number
  endWeek: number
}

// 我的授课搜索参数
export interface MyTeachingSearchParams {
  teacherId: string
  year: string
  semester: string
  week?: number
  courseId?: string
  className?: string
}