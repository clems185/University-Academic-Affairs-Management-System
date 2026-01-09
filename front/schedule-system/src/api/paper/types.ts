// 试卷查询搜索参数类型
export interface ExamPaperSearchParams {
  examId?: string;        // 考试ID
  comment?: string;       // 特殊情况说明
}

// 试卷列表项
export interface ExamPaperItem {
  examId: string;         // 考试ID
  comment: string;        // 特殊情况说明
}

// 试卷详情（包含文件内容）
export interface ExamPaperDetail {
  examId: string;         // 考试ID
  paperContentBase64?: string; // 试卷文件内容（Base64编码）
  comment: string;        // 特殊情况说明
  hasPaper: boolean;      // 是否有试卷文件
}

// API返回结果类型
export interface ExamPaperResult {
  IsSuccess: boolean;
  Message?: string;
  Result: ExamPaperItem[];
}

// 试卷详情API返回结果类型
export interface ExamPaperDetailResult {
  IsSuccess: boolean;
  Message?: string;
  Result: ExamPaperDetail;
}

// 文件下载结果类型
export interface ExamPaperDownloadResult {
  IsSuccess: boolean;
  Message?: string;
  Result: {
    examId: string;
    fileContent: string;  // Base64编码的文件内容
    fileName: string;
  };
}