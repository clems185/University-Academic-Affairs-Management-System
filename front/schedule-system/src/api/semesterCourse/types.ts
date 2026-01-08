export interface ScheduleItem {
  courseId: string;
  classId: string;
  courseName: string;
  teacherName: string;
  classroom: string;
  weekDay: number; // 1-7 (周一至周日)
  period: number;  // 1-5
}

export interface CourseInfo {
  courseId: string;
  classId: string;
  courseName: string;
  courseType: string;
  credits: number;
  assessmentType: string;
  teacherName: string;
}

export interface SemesterParams {
  studentId: string;
  year: string;
  semester: string;
}