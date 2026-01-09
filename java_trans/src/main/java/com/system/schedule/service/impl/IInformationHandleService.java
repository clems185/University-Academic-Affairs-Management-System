package com.system.schedule.service;

import com.system.schedule.dto.informationapply.InformationApplyRes;
import com.system.schedule.dto.informationapply.InformationHandleReq;
import com.system.schedule.dto.other.ApiResult;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IInformationHandleService {
    /**
     * 获取待审核申请列表
     */
    CompletableFuture<List<InformationApplyRes>> getPendingApplications();

    /**
     * 审核消息申请
     */
    CompletableFuture<ApiResult> reviewApplication(InformationHandleReq req, String reviewerId);

    /**
     * 批量删除已审核记录
     */
    CompletableFuture<ApiResult> bulkDeleteReviewedApplications(List<String> applyIds);
}