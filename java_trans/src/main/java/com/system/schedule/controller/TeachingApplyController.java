package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.common.ResultHelper;
import com.system.schedule.dto.TeachingApplyDto.*;
import com.system.schedule.service.TeachingApplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachingapply")
@PreAuthorize("isAuthenticated()")
public class TeachingApplyController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TeachingApplyController.class);
    
    @Autowired
    private TeachingApplyService teachingApplyService;

    /**
     * 获取所有可申请课程列表
     */
    @GetMapping("/courses")
    public ApiResult getCourses() {
        try {
            logger.info("[TeachingApplyController] GetCourses start");
            var courses = teachingApplyService.getAvailableCourses();
            logger.info("[TeachingApplyController] GetCourses done, count={}", courses != null ? courses.size() : 0);
            return ResultHelper.success(courses);
        } catch (Exception ex) {
            logger.error("[TeachingApplyController] 获取课程列表失败: {}", ex.getMessage(), ex);
            return ResultHelper.error("获取课程列表失败");
        }
    }

    /**
     * 获取申请列表
     */
    @GetMapping("/list")
    public ApiResult getApplyList(@Validated TeachingApplySearchReq searchReq) {
        try {
            // 只返回当前登录教师的申请记录
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            var currentUserId = authentication != null ? authentication.getName() : null;
            
            logger.info("[TeachingApplyController] GetApplyList with currentUserId={}, incoming.TeacherId={}", 
                       currentUserId, searchReq.getTeacherId());
            
            if (currentUserId != null && !currentUserId.trim().isEmpty()) {
                searchReq.setTeacherId(currentUserId.trim());
            }
            
            var applies = teachingApplyService.getTeachingApplyList(searchReq);
            logger.info("[TeachingApplyController] GetApplyList done, resultCount={}", 
                       applies != null ? applies.size() : 0);
            return ResultHelper.success(applies);
        } catch (Exception ex) {
            logger.error("[TeachingApplyController] 获取申请列表失败: {}", ex.getMessage(), ex);
            return ResultHelper.error("获取申请列表失败");
        }
    }

    /**
     * 提交授课申请
     */
    @PostMapping("/submit")
    public ApiResult submitApply(@Validated @RequestBody SubmitTeachingApplyReq req) {
        try {
            logger.info("submit start");
            var result = teachingApplyService.submitTeachingApply(req);
            logger.info("submit middle");
            return result;
        } catch (Exception ex) {
            logger.error("提交申请失败", ex);
            return ResultHelper.error("提交申请失败");
        }
    }

    /**
     * 批量提交申请
     */
    @PostMapping("/batchsubmit")
    public ApiResult batchSubmitApply(@Validated @RequestBody BatchSubmitTeachingApplyReq req) {
        try {
            var result = teachingApplyService.batchSubmitTeachingApply(req);
            return result;
        } catch (Exception ex) {
            logger.error("批量提交申请失败", ex);
            return ResultHelper.error("批量提交申请失败");
        }
    }

    /**
     * 撤回申请
     */
    @PostMapping("/withdraw")
    public ApiResult withdrawApply(@Validated @RequestBody WithdrawTeachingApplyReq req) {
        try {
            var result = teachingApplyService.withdrawTeachingApply(req);
            return result;
        } catch (Exception ex) {
            logger.error("撤回申请失败", ex);
            return ResultHelper.error("撤回申请失败");
        }
    }

    /**
     * 检查是否已申请某课程
     */
    @GetMapping("/check/{teacherId}/{courseId}")
    public ApiResult checkApplied(@PathVariable String teacherId, @PathVariable String courseId) {
        try {
            var isApplied = teachingApplyService.isAlreadyApplied(teacherId, courseId);
            return ResultHelper.success(new CheckAppliedResponse(isApplied));
        } catch (Exception ex) {
            logger.error("检查申请状态失败", ex);
            return ResultHelper.error("检查申请状态失败");
        }
    }

    // 内部类用于检查申请状态响应
    private static class CheckAppliedResponse {
        private boolean isApplied;
        
        public CheckAppliedResponse(boolean isApplied) {
            this.isApplied = isApplied;
        }
        
        public boolean isApplied() {
            return isApplied;
        }
    }
}