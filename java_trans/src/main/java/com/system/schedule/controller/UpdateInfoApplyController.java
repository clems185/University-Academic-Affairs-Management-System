package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.common.ResultHelper;
import com.system.schedule.dto.UpdateInfoApplyDto.*;
import com.system.schedule.service.UpdateInfoApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/updateinfoapply")
public class UpdateInfoApplyController extends BaseController {

    @Autowired
    private UpdateInfoApplyService updateInfoApplyService;

    /**
     * 获取更新信息申请列表
     */
    @GetMapping("/list")
    public ApiResult getUpdateInfoApplyList(
            @RequestParam(required = false) String applicantId,
            @RequestParam(required = false) String applicantType,
            @RequestParam(required = false) String finalDecision,
            @RequestParam(required = false) LocalDateTime applyTimeStart,
            @RequestParam(required = false) LocalDateTime applyTimeEnd) {
        
        System.out.println("=== 控制器接收到头像审批列表请求 ===");
        System.out.println("请求参数: applicantId=" + applicantId + 
                          ", applicantType=" + applicantType + 
                          ", finalDecision=" + finalDecision);
        
        var searchParams = new UpdateInfoApplySearchParams();
        searchParams.setApplicantId(applicantId);
        searchParams.setApplicantType(applicantType);
        searchParams.setFinalDecision(finalDecision);
        searchParams.setApplyTimeStart(applyTimeStart);
        searchParams.setApplyTimeEnd(applyTimeEnd);

        var result = updateInfoApplyService.getUpdateInfoApplyListAsync(searchParams);
        System.out.println("控制器返回结果: IsSuccess=" + result.isSuccess() + 
                          ", Message=" + result.getMsg());
        return result;
    }

    /**
     * 获取更新信息申请详情
     */
    @GetMapping("/detail/{applyId}")
    public ApiResult getUpdateInfoApplyDetail(@PathVariable String applyId) {
        return updateInfoApplyService.getUpdateInfoApplyDetailAsync(applyId);
    }

    /**
     * 创建头像审批申请
     */
    @PostMapping("/createPhoto")
    public ApiResult createPhotoApply(@RequestBody CreatePhotoApplyParams createParams) {
        return updateInfoApplyService.createPhotoApplyAsync(createParams);
    }

    /**
     * 审核更新信息申请
     */
    @PostMapping("/review")
    public ApiResult reviewUpdateInfoApply(@RequestBody ReviewUpdateInfoApplyParams reviewParams) {
        return updateInfoApplyService.reviewUpdateInfoApplyAsync(reviewParams);
    }

    /**
     * 批量审核更新信息申请
     */
    @PostMapping("/batchReview")
    public ApiResult batchReviewUpdateInfoApply(@RequestBody BatchReviewUpdateInfoApplyParams batchReviewParams) {
        return updateInfoApplyService.batchReviewUpdateInfoApplyAsync(batchReviewParams);
    }

    /**
     * 删除更新信息申请
     */
    @DeleteMapping("/delete/{applyId}")
    public ApiResult deleteUpdateInfoApply(@PathVariable String applyId) {
        return updateInfoApplyService.deleteUpdateInfoApplyAsync(applyId);
    }

    /**
     * 批量删除更新信息申请
     */
    @PostMapping("/batchDelete")
    public ApiResult batchDeleteUpdateInfoApply(@RequestBody List<String> applyIds) {
        return updateInfoApplyService.batchDeleteUpdateInfoApplyAsync(applyIds);
    }

    /**
     * 获取待审核的申请数量（用于首页统计等）
     */
    @GetMapping("/pendingCount")
    public ApiResult getPendingCount() {
        var searchParams = new UpdateInfoApplySearchParams();
        searchParams.setFinalDecision("P"); // 只查询处理中的申请

        var result = updateInfoApplyService.getUpdateInfoApplyListAsync(searchParams);
        
        if (result.isSuccess() && result.getData() instanceof List) {
            @SuppressWarnings("unchecked")
            List<UpdateInfoApplyItem> list = (List<UpdateInfoApplyItem>) result.getData();
            return ResultHelper.success(new PendingCountResponse(list.size()));
        }
        
        return ResultHelper.success(new PendingCountResponse(0));
    }

    // 内部类用于返回待审核数量
    private static class PendingCountResponse {
        private int pendingCount;
        
        public PendingCountResponse(int pendingCount) {
            this.pendingCount = pendingCount;
        }
        
        public int getPendingCount() {
            return pendingCount;
        }
    }
}