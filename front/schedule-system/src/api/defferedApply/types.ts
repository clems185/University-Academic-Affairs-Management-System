// 复用 MyExam 相关类型
export interface MyExam {
  ExamId: string
  CourseId: string
  CourseName: string
  TimeSlotId: string
  ClassroomId: string
  Semester: number
  Year: number
  ExamDate?: string
  ExamTime?: string
}

export interface MyExamResult {
  IsSuccess: boolean
  Result?: MyExam[]
  Message?: string
  Msg?: string
}

// 缓考申请参数
export interface DeferredApplyParams {
  StudentId: string
  CourseId: string
  Information: string // 申请理由
}

// 缓考申请信息
export interface DeferredApply {
  ApplyId: string
  StudentId: string
  CourseId: string
  CourseName?: string // 课程名称（关联查询得到）
  ApplyDate: string
  Information: string
  ReviewTime?: string
  ReviewComments?: string
  State: string // P-待审核, A-已通过, R-已拒绝
  StateDescription?: string // 状态描述
}

// 缓考申请结果
export interface DeferredApplyResult {
  IsSuccess: boolean
  Result?: {
    ApplyId: string
    Message: string
  }
  Message?: string
  Msg?: string
}

// 我的缓考申请列表结果
export interface MyDeferredApplyResult {
  IsSuccess: boolean
  Result?: DeferredApply[]
  Message?: string
  Msg?: string
}
