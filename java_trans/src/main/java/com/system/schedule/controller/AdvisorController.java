package com.system.schedule.controller;

import com.system.schedule.dto.advisor.AdvisorQueryReq;
import com.system.schedule.service.IAdvisorService;
import com.system.schedule.common.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/advisor")
@PreAuthorize("isAuthenticated()")
public class AdvisorController {

    private final IAdvisorService advisorService;

    @Autowired
    public AdvisorController(IAdvisorService advisorService) {
        this.advisorService = advisorService;
    }

    /**
     * 查询导师列表
     */
    @GetMapping("/list")
    public ApiResult getAdvisorList(@ModelAttribute AdvisorQueryReq req) {
        try {
            var result = advisorService.getAdvisorList(req);
            return ApiResult.success()
                    .data("data", result)
                    .message("查询成功");
        } catch (Exception ex) {
            return ApiResult.error(ex.getMessage());
        }
    }

    /**
     * 获取导师详情
     */
    @GetMapping("/detail/{teacherId}")
    public ApiResult getAdvisorDetail(@PathVariable String teacherId) {
        try {
            var result = advisorService.getAdvisorDetail(teacherId);
            if (result == null) {
                return ApiResult.error("未找到导师信息");
            }
            return ApiResult.success()
                    .data("data", result)
                    .message("查询成功");
        } catch (Exception ex) {
            return ApiResult.error(ex.getMessage());
        }
    }

    /**
     * 根据专业查询导师
     */
    @GetMapping("/major/{majorId}")
    public ApiResult getAdvisorByMajor(@PathVariable String majorId) {
        try {
            var result = advisorService.getAdvisorByMajor(majorId);
            return ApiResult.success()
                    .data("data", result)
                    .message("查询成功");
        } catch (Exception ex) {
            return ApiResult.error(ex.getMessage());
        }
    }

    /**
     * 根据状态查询导师
     */
    @GetMapping("/status/{status}")
    public ApiResult getAdvisorByStatus(@PathVariable String status) {
        try {
            var result = advisorService.getAdvisorByStatus(status);
            return ApiResult.success()
                    .data("data", result)
                    .message("查询成功");
        } catch (Exception ex) {
            return ApiResult.error(ex.getMessage());
        }
    }
}