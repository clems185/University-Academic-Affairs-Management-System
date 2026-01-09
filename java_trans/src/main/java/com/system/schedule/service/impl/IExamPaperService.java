package com.system.schedule.service;

import com.system.schedule.dto.exam.ExamPaperSearchParams;
import com.system.schedule.dto.other.ApiResult;
import java.util.concurrent.CompletableFuture;

public interface IExamPaperService {
    /**
     * 获取试卷列表
     *
     * @param searchParams 搜索参数
     * @return 试卷列表
     */
    CompletableFuture<ApiResult> getExamPaperListAsync(ExamPaperSearchParams searchParams);

    /**
     * 获取试卷详情（包含文件内容）
     *
     * @param examId 考试ID
     * @return 试卷详情
     */
    CompletableFuture<ApiResult> getExamPaperDetailAsync(String examId);

    /**
     * 下载试卷文件
     *
     * @param examId 考试ID
     * @return 试卷文件内容
     */
    CompletableFuture<ApiResult> downloadExamPaperAsync(String examId);
}