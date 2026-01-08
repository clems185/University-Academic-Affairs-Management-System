// 原有ProfileInfo接口补充字段
export interface ProfileInfo {
    Id: string;
    Name: string;
    NickName?: string;
    UserType: number;
    Email?: string;
    MajorName?: string;
    Telephone?: string;
    StartYear?: number;
    EndYear?: number;
    Gpa?: number;
    AttemptedCredit?: number;
    EarnedCredit?: number;
    WorkYear?: number;
    Information?: string;
    
    // 新增：头像（Base64或null）
    Avatar?: string | null;
    
    // 新增：教师专属字段
    CourseCount?: number | null; // 当前授课数
    StudentCount?: number | null; // 指导学生数
    Title?: string | null; // 职称
}

// ApiResponse类型保持不变...
export interface ApiResponse<T> {
    IsSuccess: boolean;
    Msg: string;
    Result: T;
}