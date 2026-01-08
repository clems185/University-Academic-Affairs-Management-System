export interface LoginParams{
  Name: string;
  Password: string;
}

export interface LoginResult {
  Id: string;
  Token: string;
  UserType: number;
  Name: string;
  NickName: string;
}

// 用户个人信息
export interface UserInfo {
  Id: number;           
  Name: string;       
  Email?: string;
  Phone?: string;
  Avatar?: string;
  UserType: number;    
}

// 更新个人信息参数
export interface UpdateUserInfoParams {
  Name?: string;        
  Email?: string;
  Phone?: string;
  Avatar?: string;
}

// 修改密码参数
export interface ChangePasswordParams {
  OldPassword: string; 
  NewPassword: string; 
  ConfirmPassword: string; 
}


export interface UserApiResult<T = any> {
  IsSuccess: boolean;
  Message?: string;
  Result: T;
}

export interface verificationParams {
  userId: string;       
  email: string;
}

export interface forgetPasswordParams {
  id: string;
  verificationCode: string;
  newPassword: string;
}