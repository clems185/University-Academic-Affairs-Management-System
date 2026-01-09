// 教室使用情况查询参数
export interface ClassroomQueryParams {
  campusId?: string;        // 校区ID
  buildingId?: string;      // 教学楼ID
  classroomId?: string;     // 教室ID
  date?: string;            // 查询日期 (YYYY-MM-DD)
  timeSlotId?: string;      // 时间段ID
  classroomType?: string;   // 教室类型
}

// 校区信息 - 使用ClassroomUsage类型，因为后端返回的是这个结构
export interface CampusInfo {
  classroomId: string;      // 校区ID
  campusName: string;       // 校区名称
}

// 教学楼信息 - 使用ClassroomUsage类型，因为后端返回的是这个结构
export interface BuildingInfo {
  classroomId: string;      // 教学楼ID
  buildingName: string;     // 教学楼名称
  campusId: string;         // 所属校区ID
}

// 教室信息
export interface ClassroomInfo {
  classroomId: string;
  buildingId: string;
  roomNumber: string;
  capacity: number;
  type: string;
  buildingName?: string;
  campusName?: string;
}

// 教室使用情况
export interface ClassroomUsage {
  classroomId: string;
  roomNumber: string;
  buildingName: string;
  campusName: string;
  capacity: number;
  type: string;
  isOccupied: boolean;
  courseInfo?: {
    courseName: string;
    className: string;
    teacherName: string;
    startTime: string;
    endTime: string;
  };
}

// 时间段信息 - 使用ClassroomUsage类型，因为后端返回的是这个结构
export interface TimeSlotInfo {
  classroomId: string;      // 时间段ID
  roomNumber: string;       // 显示时间（如：08:00-09:00）
  displayTime: string;      // 显示时间
}