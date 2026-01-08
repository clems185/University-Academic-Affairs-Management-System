export interface SignReq{
    ClassId:string,
    CourseId:string,
    SignId:string,
}

export interface Sign {
  SignId:string;
  interval:number;
  ts:number;
  CourseId: string;
  ClassId: string;
  StudentId:string;
  StudentName:string;//在sessionStorage中获取
  SignTime:Date;//通过时间戳确定
}

export interface ClassList{
    ClassId:string,
    CourseId:string,
    Name:string,
}
