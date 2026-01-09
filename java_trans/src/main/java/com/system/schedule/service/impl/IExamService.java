package com.system.schedule.service;

import com.system.schedule.dto.exam.ExamScheduleSearchParams;
import com.system.schedule.dto.other.ApiResult;
import java.util.concurrent.CompletableFuture;

public interface IExamService {
    /**
     * 获取考试安排列表
     *
     * @param searchParams 搜索参数
     * @return 考试安排列表
     */
    CompletableFuture<ApiResult> getExamScheduleListAsync(ExamScheduleSearchParams searchParams);
}