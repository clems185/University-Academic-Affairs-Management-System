package com.system.schedule.service;

import com.system.schedule.dto.examdelay.ExamDelayHandleSearchParams;
import com.system.schedule.dto.other.ApiResult;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IExamDelayService {
    /**
     * 获取缓考申请列表
     *
     * @param searchParams 搜索参数
     * @return 缓考申请列表
     */
    CompletableFuture<ApiResult> getExamDelayListAsync(ExamDelayHandleSearchParams searchParams);

    /**
     * 批量同意缓考申请
     *
     * @param ids 申请ID列表
     * @return 操作结果
     */
    CompletableFuture<ApiResult> batchApproveAsync(List<String> ids);

    /**
     * 批量拒绝缓考申请
     *
     * @param ids 申请ID列表
     * @return 操作结果
     */
    CompletableFuture<ApiResult> batchRejectAsync(List<String> ids);

    /**
     * 同意单个缓考申请
     *
     * @param id 申请ID
     * @param reviewComments 审核意见
     * @param reviewerId 审核人ID
     * @return 操作结果
     */
    CompletableFuture<ApiResult> approveAsync(String id, String reviewComments, int reviewerId);

    /**
     * 拒绝单个缓考申请
     *
     * @param id 申请ID
     * @param reviewComments 审核意见
     * @param reviewerId 审核人ID
     * @return 操作结果
     */
    CompletableFuture<ApiResult> rejectAsync(String id, String reviewComments, int reviewerId);
}