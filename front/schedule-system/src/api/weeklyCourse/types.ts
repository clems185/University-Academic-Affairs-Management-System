export interface WeeklyScheduleItem {
  courseId: string;
  courseName: string;
  className: string;
  teacherName: string;
  classroom: string;
  dayOfWeek: number;
  startTime: string;
  endTime: string;
  campus: string;
  building: string;
  roomNumber: string;
  period: string;
  weekPattern: string;
  year: string;
  semester: string;
  studentCount: number;
  // 课程详细信息
  courseType: string;
  assessmentType: string;
  credits: number;
  courseInformation: string;
}

export interface WeeklyScheduleSearchParams {
  year: string;
  semester: string;
  week?: number;
  userType: string;
  userId: string;
}

export interface TimeSlot {
  id: number;
  startTime: string;
  endTime: string;
  displayText: string;
}

export interface CourseBlock extends WeeklyScheduleItem {
  startPeriod: number;
  endPeriod: number;
  duration: number;
  displayText: string;
  color: string;
}

// 课程详情接口
export interface CourseDetail {
  courseId: string;
  courseName: string;
  courseType: string;
  assessmentType: string;
  credits: number;
  information: string;
}