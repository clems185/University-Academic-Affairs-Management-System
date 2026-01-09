// src/api/userInfo/types.ts
// 用户信息结果类型
export interface UserInfoResult {
  Id: string;
  Name: string;
  Gender: string;
  MajorName: string;
  TypeDetail: string;
  Email: string;
  Phone: string;
}

// 专业信息类型
export interface MajorInfo {
    MajorId: string;
    MajorName: string;
}