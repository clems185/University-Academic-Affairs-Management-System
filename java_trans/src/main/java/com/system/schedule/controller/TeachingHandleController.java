package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.common.ResultHelper;
import com.system.schedule.dto.TeachingHandleDto.*;
import com.system.schedule.service.TeachingHandleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachinghandle")
@PreAuthorize("hasRole('ADMIN')")
public class TeachingHandleController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TeachingHandleController.class);
    
    @Autowired
    private TeachingHandleService teachingHandleService;

    /**
     * 获取待处理申请列表（管理员用）
     */
    @GetMapping("/list")
    public ApiResult getPendingApplyList(@Validated TeachingHandleSearchReq searchReq) {
        try {
            var applies = teachingHandleService.getPendingApplyList(searchReq);
            return ResultHelper.success(applies);
        } catch (Exception ex) {
            logger.error("获取待处理申请列表失败", ex);
            return ResultHelper.error("获取待处理申请列表失败");
        }
    }

    /**
     * 同意申请
     */
    @PostMapping("/approve")
    public ApiResult approveApply(@Validated @RequestBody ApproveTeachingApplyReq req) {
        try {
            var result = teachingHandleService.approveTeachingApply(req);
            return result;
        } catch (Exception ex) {
            logger.error("同意申请失败", ex);
            return ResultHelper.error("同意申请失败");
        }
    }

    /**
     * 拒绝申请
     */
    @PostMapping("/reject")
    public ApiResult rejectApply(@Validated @RequestBody RejectTeachingApplyReq req) {
        try {
            var result = teachingHandleService.rejectTeachingApply(req);
            return result;
        } catch (Exception ex) {
            logger.error("拒绝申请失败", ex);
            return ResultHelper.error("拒绝申请失败");
        }
    }

    /**
     * 批量同意申请
     */
    @PostMapping("/batchapprove")
    public ApiResult batchApproveApply(@Validated @RequestBody BatchProcessTeachingApplyReq req) {
        try {
            var result = teachingHandleService.batchApproveTeachingApply(req);
            return result;
        } catch (Exception ex) {
            logger.error("批量同意申请失败", ex);
            return ResultHelper.error("批量同意申请失败");
        }
    }

    /**
     * 批量拒绝申请
     */
    @PostMapping("/batchreject")
    public ApiResult batchRejectApply(@Validated @RequestBody BatchProcessTeachingApplyReq req) {
        try {
            var result = teachingHandleService.batchRejectTeachingApply(req);
            return result;
        } catch (Exception ex) {
            logger.error("批量拒绝申请失败", ex);
            return ResultHelper.error("批量拒绝申请失败");
        }
    }

    /**
     * 删除申请记录
     */
    @PostMapping("/delete")
    public ApiResult deleteApply(@Validated @RequestBody DeleteTeachingApplyReq req) {
        try {
            var result = teachingHandleService.deleteTeachingApply(req);
            return result;
        } catch (Exception ex) {
            logger.error("删除申请失败", ex);
            return ResultHelper.error("删除申请失败");
        }
    }

    /**
     * 批量删除申请记录
     */
    @PostMapping("/batchdelete")
    public ApiResult batchDeleteApply(@Validated @RequestBody BatchDeleteTeachingApplyReq req) {
        try {
            var result = teachingHandleService.batchDeleteTeachingApply(req);
            return result;
        } catch (Exception ex) {
            logger.error("批量删除申请失败", ex);
            return ResultHelper.error("批量删除申请失败");
        }
    }
}