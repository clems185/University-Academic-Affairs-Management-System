// GradeQueryController.java
package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.dto.GradeQueryReq;
import com.system.schedule.service.IGradeQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gradequery")
@Api(tags = "成绩查询控制器")
@PreAuthorize("isAuthenticated()")
public class GradeQueryController extends BaseController {
    
    @Autowired
    private IGradeQueryService gradeQueryService;
    
    @GetMapping("/student/{studentId}")
    @ApiOperation("根据学生ID查询成绩")
    public ApiResult<?> getGradesByStudent(@PathVariable String studentId) {
        logger.info("开始查询成绩");
        try {
            Object result = gradeQueryService.getGradesByStudentAsync(studentId);
            return ApiResult.success(result, "查询成功");
        } catch (Exception ex) {
            logger.error("查询成绩失败", ex);
            return ApiResult.error(ex.getMessage());
        }
    }
    
    @GetMapping("/search")
    @ApiOperation("根据条件查询成绩")
    public ApiResult<?> getGradesByCondition(@ModelAttribute GradeQueryReq req) {
        try {
            Object result = gradeQueryService.getGradesByConditionAsync(req);
            return ApiResult.success(result, "查询成功");
        } catch (Exception ex) {
            logger.error("根据条件查询成绩失败", ex);
            return ApiResult.error(ex.getMessage());
        }
    }
    
    @GetMapping("/summary/{studentId}")
    @ApiOperation("获取学生成绩汇总")
    public ApiResult<?> getGradeSummary(@PathVariable String studentId) {
        try {
            Object result = gradeQueryService.getGradeSummaryAsync(studentId);
            return ApiResult.success(result, "查询成功");
        } catch (Exception ex) {
            logger.error("获取成绩汇总失败", ex);
            return ApiResult.error(ex.getMessage());
        }
    }
    
    @GetMapping("/semester/{studentId}")
    @ApiOperation("获取学生按学期分组的成绩")
    public ApiResult<?> getGradesBySemester(@PathVariable String studentId) {
        try {
            Object result = gradeQueryService.getGradesBySemesterAsync(studentId);
            return ApiResult.success(result, "查询成功");
        } catch (Exception ex) {
            logger.error("获取学期成绩失败", ex);
            return ApiResult.error(ex.getMessage());
        }
    }
}