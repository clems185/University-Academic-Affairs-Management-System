package com.system.schedule.service;

import com.system.schedule.dto.instructor.InstructorHandleSearchParams;
import com.system.schedule.dto.other.ApiResult;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IInstructorService {
    /**
     * 获取导师申请列表
     *
     * @param searchParams 搜索参数
     * @return 导师申请列表
     */
    CompletableFuture<ApiResult> getInstructorApplyListAsync(InstructorHandleSearchParams searchParams);

    /**
     * 批量同意导师申请
     *
     * @param ids 申请ID列表
     * @return 操作结果
     */
    CompletableFuture<ApiResult> batchApproveAsync(List<String> ids);

    /**
     * 批量拒绝导师申请
     *
     * @param ids 申请ID列表
     * @return 操作结果
     */
    CompletableFuture<ApiResult> batchRejectAsync(List<String> ids);

    /**
     * 同意单个导师申请
     *
     * @param id 申请ID
     * @param reviewComments 审核意见
     * @param reviewerId 审核人ID
     * @return 操作结果
     */
    CompletableFuture<ApiResult> approveAsync(String id, String reviewComments, int reviewerId);

    /**
     * 拒绝单个导师申请
     *
     * @param id 申请ID
     * @param reviewComments 审核意见
     * @param reviewerId 审核人ID
     * @return 操作结果
     */
    CompletableFuture<ApiResult> rejectAsync(String id, String reviewComments, int reviewerId);
}