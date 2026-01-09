package com.system.schedule.controller;

import com.system.schedule.dto.advisor.*;
import com.system.schedule.service.IAdvisorApplyService;
import com.system.schedule.common.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/advisor-apply")
@PreAuthorize("isAuthenticated()")
public class AdvisorApplyController {

    private final IAdvisorApplyService advisorApplyService;

    @Autowired
    public AdvisorApplyController(IAdvisorApplyService advisorApplyService) {
        this.advisorApplyService = advisorApplyService;
    }

    /**
     * 获取导师申请列表
     */
    @GetMapping("/list")
    public ApiResult getAdvisorApplyList(@ModelAttribute AdvisorApplyQueryReq req) {
        try {
            var result = advisorApplyService.getAdvisorApplyList(req);
            return ApiResult.success()
                    .data("result", result)
                    .message("查询成功");
        } catch (Exception ex) {
            return ApiResult.error(ex.getMessage());
        }
    }

    /**
     * 提交导师申请
     */
    @PostMapping("/submit")
    public ApiResult submitAdvisorApply(@RequestBody AdvisorApplySubmitReq req) {
        try {
            if (req.getTeacherId() == null || req.getTeacherId().isEmpty()) {
                return ApiResult.error("教师ID不能为空");
            }

            if (req.getInformation() == null || req.getInformation().isEmpty()) {
                return ApiResult.error("申请原因不能为空");
            }

            var result = advisorApplyService.submitAdvisorApply(req);
            return result ? 
                ApiResult.success("申请提交成功") : 
                ApiResult.error("申请提交失败");
        } catch (Exception ex) {
            return ApiResult.error(ex.getMessage());
        }
    }

    /**
     * 撤回导师申请
     */
    @PostMapping("/withdraw")
    public ApiResult withdrawAdvisorApply(@RequestBody WithdrawRequest request) {
        try {
            if (request.getId() == null || request.getId().isEmpty()) {
                return ApiResult.error("申请ID不能为空");
            }

            var result = advisorApplyService.withdrawAdvisorApply(request.getId());
            return result ? 
                ApiResult.success("申请撤回成功") : 
                ApiResult.error("申请撤回失败");
        } catch (Exception ex) {
            return ApiResult.error(ex.getMessage());
        }
    }

    /**
     * 审核导师申请
     */
    @PostMapping("/review")
    public ApiResult reviewAdvisorApply(@RequestBody AdvisorApplyReviewReq req) {
        try {
            if (req.getApplyId() == null || req.getApplyId().isEmpty()) {
                return ApiResult.error("申请ID不能为空");
            }

            if (req.getFinalDecision() == null || req.getFinalDecision().isEmpty()) {
                return ApiResult.error("审核结果不能为空");
            }

            if (!"Y".equals(req.getFinalDecision()) && !"N".equals(req.getFinalDecision())) {
                return ApiResult.error("审核结果只能是Y(同意)或N(拒绝)");
            }

            var result = advisorApplyService.reviewAdvisorApply(req);
            return result ? 
                ApiResult.success("审核完成") : 
                ApiResult.error("审核失败");
        } catch (Exception ex) {
            return ApiResult.error(ex.getMessage());
        }
    }
}

// 撤回申请请求参数类
class WithdrawRequest {
    private String id = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}