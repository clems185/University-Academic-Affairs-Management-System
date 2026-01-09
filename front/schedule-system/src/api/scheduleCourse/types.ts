// types.ts
export interface CourseInfo {
  ClassId: string;    
  CourseId: string;   
  CourseName: string;  
  TeacherName: string;
  Location: string;  
  SelectedCount: number; 
  Capacity: number;   
  Year: string;       
  Semester: string;   
  TimeSlots?: string[]; // 添加TimeSlots字段
}

export interface ScheduleRequest {
  year: string;
  semester: string;
  constraints: string[];
}