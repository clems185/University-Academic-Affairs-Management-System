// 成绩登记课程信息
export interface GradeCourse {
  CourseId: string
  CourseName: string
  ClassId: string
  Semester: number
  Year: number
  StudentCount: number // 选课学生数量
}

// 生成Excel参数
export interface GenerateExcelParams {
  CourseId: string
  ClassId: string
  Semester: number
  Year: number
}

// 上传成绩参数
export interface UploadGradeParams {
  CourseId: string
  ClassId: string
  Semester: number
  Year: number
  ExcelFile?: File
}

// 学生成绩信息
export interface StudentGrade {
  StudentId: string
  Grade: string
}

// 上传成绩结果
export interface UploadGradeResult {
  IsSuccess: boolean
  Result?: {
    SuccessCount: number
    FailureCount: number
    FailureDetails?: string[]
  }
  Message?: string
  Msg?: string
}

// API 返回结果
export interface GradeCoursesResult {
  IsSuccess: boolean
  Result?: GradeCourse[]
  Message?: string
  Msg?: string
}
