package com.system.schedule.controller;

import com.system.schedule.dto.*;
import com.system.schedule.service.*;
import com.system.schedule.common.ApiResult;
import com.system.schedule.common.ResultHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advisor-info")
@PreAuthorize("isAuthenticated()")
public class AdvisorInfoController {

    private final IAdvisorInfoService advisorInfoService;

    @Autowired
    public AdvisorInfoController(IAdvisorInfoService advisorInfoService) {
        this.advisorInfoService = advisorInfoService;
    }

    /**
     * 根据教师ID获取导师信息
     */
    @GetMapping("/detail/{teacherId}")
    public ApiResult getAdvisorInfoById(@PathVariable String teacherId) {
        try {
            var result = advisorInfoService.getAdvisorInfoById(teacherId);
            if (result == null) {
                return ResultHelper.success(false, "未找到导师信息");
            }
            return ResultHelper.success(result, "查询成功");
        } catch (Exception ex) {
            return ResultHelper.error(ex.getMessage());
        }
    }

    /**
     * 更新导师信息
     */
    @PutMapping("/update/{teacherId}")
    public ApiResult updateAdvisorInfo(@PathVariable String teacherId, @RequestBody AdvisorInfoReq req) {
        try {
            var result = advisorInfoService.updateAdvisorInfo(teacherId, req);
            if (result) {
                return ResultHelper.success("更新成功");
            } else {
                return ResultHelper.error("更新失败，教师不存在");
            }
        } catch (Exception ex) {
            return ResultHelper.error(ex.getMessage());
        }
    }
}