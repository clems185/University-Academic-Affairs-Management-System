// gradeQuery/types.ts
export interface GradeQueryParams {
  studentId?: string;     // 学号
  studentName?: string;   // 学生姓名
  courseName?: string;    // 课程名称
  grade?: string;         // 成绩等级
  year?: number;          // 学年
  semester?: string;      // 学期
}

export interface GradeQueryResult {
  studentId: string;      // 学号
  studentName: string;    // 学生姓名
  courseId: string;       // 课程ID
  courseName: string;     // 课程名称
  classId: string;        // 班级ID
  className: string;      // 班级名称
  grade: string;          // 成绩等级
  credits: number;        // 学分
  year: string;           // 学年
  semester: string;       // 学期
  state: string;          // 修读状态
}

export interface GradeSummaryResult {
  studentId: string;      // 学号
  studentName: string;    // 学生姓名
  grades: GradeQueryResult[]; // 成绩列表
  totalCredits: number;       // 总学分
  passedCredits: number;      // 通过学分
  passRate: number;           // 通过率
  averageGpa: number;         // 平均绩点
}