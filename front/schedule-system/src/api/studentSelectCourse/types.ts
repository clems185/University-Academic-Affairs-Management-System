export interface SelectCourseItem {
  CourseId: string;
  ClassId: string;
  CourseName: string;
  TeacherName?: string;
  Location?: string;
  SelectedCount: number;
  Capacity: number;
  Status: 'N' | 'P' | 'C'; // N:未选择 P:预选择 C:已选择
  CourseClassId: string; // 组合ID: CourseId + ClassId
}

export interface ClassInfo {
  CourseId: string;
  ClassId: string;
  CourseName: string;
  TeacherName?: string;
  Location?: string;
  SelectedCount: number;
  Capacity: number;
  CourseClassId: string; // 组合ID: CourseId + ClassId
}