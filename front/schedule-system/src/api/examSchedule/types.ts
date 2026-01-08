// 考试安排搜索参数类型
export interface ExamScheduleSearchParams {
  examId?: string;        // 考试ID
  courseId?: string;      // 课程ID
  timeSlotId?: string;    // 时间段ID
  classroomId?: string;   // 教室ID
  semester?: number;      // 学期（0/1）
  year?: number;          // 学年（YYYY）
}

// 考试安排列表项
export interface ExamScheduleItem {
  examId: string;         // 考试ID
  courseId: string;       // 课程ID
  courseName: string;     // 课程名称
  timeSlotId: string;     // 时间段ID
  timeSlotDisplay: string; // 时间段显示文本
  classroomId: string;    // 教室ID
  semester: number;       // 学期（0/1）
  year: number;           // 学年（YYYY）
  semesterDisplay: string; // 学期显示文本
}

// API返回结果类型
export interface ExamScheduleResult {
  IsSuccess: boolean;
  Message?: string;
  Result: ExamScheduleItem[];
}