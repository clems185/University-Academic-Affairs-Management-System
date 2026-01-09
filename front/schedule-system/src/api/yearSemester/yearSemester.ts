// api/yearSemester.ts
import axios from 'axios';

export interface YearSemesterStartTime {
  year: string;
  semester: string;
  startDate: string;
}

// 获取学期开学日期
export const getSemesterStartDate = (year: number, semester: number) => {
  return axios.get('/api/YearSemesterStart/start-date', {
    params: { year, semester }
  });
};

// 获取当前学期信息
export const getCurrentSemesterInfo = () => {
  return axios.get('/api/YearSemesterStart/current');
};

// 计算当前周次
export const calculateCurrentWeek = (startDate: string): number => {
  const start = new Date(startDate);
  const now = new Date();
  
  // 计算相差的毫秒数
  const diffTime = now.getTime() - start.getTime();
  
  // 转换为天数并计算周数
  const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
  const weekNumber = Math.floor(diffDays / 7) + 1;
  
  return weekNumber > 0 ? weekNumber : 1;
};