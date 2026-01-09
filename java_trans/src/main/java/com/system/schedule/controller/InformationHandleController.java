// InformationHandleController.java
package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.dto.InformationHandleReq;
import com.system.schedule.service.IInformationHandleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/informationhandle")
@Api(tags = "信息处理控制器")
@PreAuthorize("isAuthenticated()")
public class InformationHandleController extends BaseController {
    
    @Autowired
    private IInformationHandleService reviewService;
    
    @GetMapping("/pending")
    @ApiOperation("获取待审核列表")
    public ApiResult<?> getPendingList() {
        try {
            String userId = getCurrentUserId();
            logger.info("审核员 {} 获取待审核列表", userId);
            Object list = reviewService.getPendingApplications();
            logger.debug("获取到 {} 条待审核记录", list != null ? ((List<?>)list).size() : 0);
            return ApiResult.success(list);
        } catch (Exception ex) {
            logger.error("获取待审核列表失败", ex);
            return ApiResult.error("获取待审核列表失败");
        }
    }
    
    @PostMapping("/review")
    @ApiOperation("审核申请")
    public ApiResult<?> reviewApplication(@RequestBody InformationHandleReq req) {
        try {
            String reviewerId = getCurrentUserId();
            if (reviewerId == null || reviewerId.isEmpty()) {
                logger.warn("无法获取审核员ID");
                return ApiResult.error("无法获取审核员ID");
            }
            
            logger.info("审核请求: 审核员={}, 申请ID={}, 决定={}",
                    reviewerId, req.getInfoApplyId(), req.getFinalDecision());
            
            return reviewService.reviewApplication(req, reviewerId);
        } catch (Exception ex) {
            logger.error("审核申请失败", ex);
            return ApiResult.error("审核申请失败");
        }
    }
    
    @PostMapping("/bulk-delete")
    @ApiOperation("批量删除已审核记录")
    public ApiResult<?> bulkDelete(@RequestBody List<String> applyIds) {
        try {
            String userId = getCurrentUserId();
            if (applyIds == null || applyIds.isEmpty()) {
                logger.warn("审核员 {} 尝试空批量删除", userId);
                return ApiResult.error("请选择要删除的记录");
            }
            
            logger.info("审核员 {} 批量删除 {} 条记录", userId, applyIds.size());
            return reviewService.bulkDeleteReviewedApplications(applyIds);
        } catch (Exception ex) {
            logger.error("批量删除失败", ex);
            return ApiResult.error("批量删除失败");
        }
    }
}