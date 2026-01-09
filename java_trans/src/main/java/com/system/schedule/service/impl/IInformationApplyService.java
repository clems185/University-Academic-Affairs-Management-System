package com.system.schedule.service;

import com.system.schedule.dto.informationapply.InformationApplyReq;
import com.system.schedule.dto.informationapply.InformationApplyRes;
import com.system.schedule.dto.other.ApiResult;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IInformationApplyService {
    /**
     * 提交消息申请
     */
    CompletableFuture<ApiResult> submitInformationApply(InformationApplyReq req, String applicantId);

    /**
     * 获取当前用户的消息申请列表
     */
    CompletableFuture<List<InformationApplyRes>> getInformationApplyList(String applicantId);

    /**
     * 撤回消息申请
     */
    CompletableFuture<ApiResult> withdrawInformationApply(String infoApplyId, String applicantId);

    /**
     * 批量删除已通过或已拒绝的消息申请
     */
    CompletableFuture<ApiResult> batchDeleteInformationApply(List<String> infoApplyIds, String applicantId);
}