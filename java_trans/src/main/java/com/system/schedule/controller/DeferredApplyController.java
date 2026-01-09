// File: DeferredApplyController.java
package com.system.schedule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.system.schedule.service.IDeferredApplyService;
import com.system.schedule.model.dto.SubmitDeferredApplyReq;
import com.system.schedule.model.ApiResult;
import com.system.schedule.utils.ResultHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/deferred-apply")
@Validated
public class DeferredApplyController {

    private static final Logger logger = LoggerFactory.getLogger(DeferredApplyController.class);
    
    private final IDeferredApplyService deferredApplyService;

    @Autowired
    public DeferredApplyController(IDeferredApplyService deferredApplyService) {
        this.deferredApplyService = deferredApplyService;
    }

    /**
     * 提交缓考申请
     */
    @PostMapping("/submit")
    public ApiResult submitDeferredApply(@Valid @RequestBody SubmitDeferredApplyReq req) {
        try {
            logger.info("[DeferredApplyController] SubmitDeferredApply start, StudentId={}, CourseId={}", 
                req != null ? req.getStudentId() : null, req != null ? req.getCourseId() : null);

            var result = deferredApplyService.submitDeferredApply(req);
            
            logger.info("[DeferredApplyController] SubmitDeferredApply end, success={}", result.isSuccess());
            return result;
        } catch (Exception ex) {
            logger.error("[DeferredApplyController] 提交缓考申请失败", ex);
            return ResultHelper.error("提交缓考申请失败");
        }
    }

    /**
     * 获取学生的缓考申请列表
     */
    @GetMapping("/my-applies/{studentId}")
    public ApiResult getMyDeferredApplies(@PathVariable String studentId) {
        try {
            if (studentId == null || studentId.trim().isEmpty()) {
                return ResultHelper.error("学生ID不能为空");
            }

            logger.info("[DeferredApplyController] GetMyDeferredApplies start, studentId={}", studentId);

            var applies = deferredApplyService.getMyDeferredApplies(studentId);
            
            logger.info("[DeferredApplyController] GetMyDeferredApplies done, studentId={}, resultCount={}", 
                studentId, applies != null ? applies.size() : 0);
            
            return ResultHelper.success(applies);
        } catch (Exception ex) {
            logger.error("[DeferredApplyController] 获取缓考申请列表失败", ex);
            return ResultHelper.error("获取缓考申请列表失败");
        }
    }
}