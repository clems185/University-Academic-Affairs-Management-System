// 学生考试信息
export interface MyExam {
  ExamId: string
  CourseId: string
  CourseName: string
  TimeSlotId: string
  ClassroomId: string
  Semester: number
  Year: number
  ExamDate?: string // 考试日期（如果有时间段信息）
  ExamTime?: string // 考试时间（如果有时间段信息）
}

// API 返回结果
export interface MyExamResult {
  IsSuccess: boolean
  Result?: MyExam[]
  Message?: string
  Msg?: string
}