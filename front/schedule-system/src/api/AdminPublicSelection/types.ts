export interface AdminPublicSelectionDto {
  SelectionId: string;
  Information: string;
  BeginTime: string;
  EndTime: string;
  IsActive: boolean;
  Type: number; // 0:随机踢人 1:人满为止
  Semester: number; // 0:第一学期 1:第二学期
  Year: number; // 学年
  IsProcessed: string; // 是否已处理
  Courses: SelectionCourseDto[];
  Majors: SelectionMajorDto[];
}

export interface SelectionCourseDto {
  CourseId: string;
  CourseName: string;
  Credits: number;
  IsSelect: string;
}

export interface SelectionMajorDto {
  MajorId: string;
  MajorName: string;
  DepartmentName: string;
}

export interface CreateSelectionDto {
  Information: string;
  BeginTime: string;
  EndTime: string;
  Type: number; // 0:随机踢人 1:人满为止
  Semester: number; // 0:第一学期 1:第二学期
  Year: number; // 学年
  CourseIds: string[];
  MajorIds: string[];
}

export interface UpdateSelectionDto {
  SelectionId: string;
  Information?: string;
  BeginTime?: string;
  EndTime?: string;
  Type?: number; // 0:随机踢人 1:人满为止
  Semester?: number; // 0:第一学期 1:第二学期
  Year?: number; // 学年
  CourseIds?: string[];
  MajorIds?: string[];
}

export interface ValidateSelectionTimeDto {
  BeginTime: string;
  EndTime: string;
  ExcludeSelectionId?: string;
}

export interface ValidateTimeResult {
  IsValid: boolean;
}

export interface ProcessSelectionResultDto {
  SelectionId: string;
  ForceProcess: boolean;
}

export interface ApiResponse<T> {
  IsSuccess: boolean;
  Msg: string;
  Result: T;
}