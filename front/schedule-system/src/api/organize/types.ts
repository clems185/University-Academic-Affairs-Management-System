// 考试申请表单的数据接口
export interface ExamApplyFormParams {
  courseId: string | null;      // 课程ID (course.course_id)
  classId: string | null;       // 教学班ID (class.class_id)
  buildingId: string | null;    
  classroomId: string | null;
  semester: 0 | 1 | null;       // 学期 (class.semester)
  year: number | null;          // 学年 (class.year)
  bookType: string | null;      // 开卷/闭卷
  examType: string | null;      // 考试类型
  examTime: Date | null;        // 考试时间
  examDuration: number | null;  // 考试时长 (分钟)
  paperFile: File | null;       // 试卷文件
}

// 考试申请列表项的数据接口
export interface ExamApplyItem {
  applyId: string;
  courseName: string;
  className: string;
  location?: string;
  applyDate: string;
  proposedTime: string;
  status: string; // 'P', 'Y', 'N'
  paperFileName?: string;
  reviewComments?: string;
}

// 教学班信息的接口
export interface ClassInfo {
  classId: string;
  className: string;
}

// 教师所教课程的数据接口
export interface TeacherCourse {
  courseId: string;
  courseName: string;
  classes: ClassInfo[];
}

// 教学楼信息的接口 
export interface Building {
  cId: string;
  buildingId: string;
  name: string;
}

// 教室信息的接口
export interface Classroom {
  buildingId: string;
  classroomId: string;
  roomNumber: string;
  capacity: number;
  type: string;
}