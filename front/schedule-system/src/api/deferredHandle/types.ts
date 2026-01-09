// 缓考申请处理
export interface ExamDelayHandleParams {
  applyId: string;         // 申请ID
  decision: 'Y' | 'N' | 'P'; // 处理决定 (Y:同意, N:拒绝, P:处理中)
  reviewComments: string;  // 审核意见
  reviewerId: number;      // 处理的管理员ID
}

// 搜索参数类型
export interface ExamDelayHandleSearchParams {
  applyId?: string;        // 申请ID
  studentId?: string;      // 学生ID
  courseId?: string;       // 课程ID
  state?: 'Y' | 'N' | 'P'; // 处理状态
}

// 缓考处理列表项
export interface ExamDelayHandleItem {
  applyId: string;         // 申请ID
  studentId: string;       // 学生ID
  courseId: string;        // 课程ID
  applyDate: string;       // 申请时间
  information: string;     // 申请内容
  reviewTime: string;      // 审核时间
  reviewComments: string;  // 审核意见
  state: 'Y' | 'N' | 'P'; // 处理状态
}

// API返回结果类型
export interface ExamDelayHandleResult {
  IsSuccess: boolean;
  Message?: string;
  Result: ExamDelayHandleItem[];
}