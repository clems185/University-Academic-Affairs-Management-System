package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.common.ResultHelper;
import com.system.schedule.model.dto.MyTeachingSearchParams;
import com.system.schedule.service.MyTeachingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/my-teaching")
@Slf4j
public class MyTeachingController {
    
    @Autowired
    private MyTeachingService myTeachingService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * 获取我的授课信息
     */
    @GetMapping("/courses")
    public CompletableFuture<ApiResult> getMyTeaching(@ModelAttribute MyTeachingSearchParams searchParams) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("=== MyTeachingController.getMyTeaching 开始 ===");
                log.info("查询参数: {}", objectMapper.writeValueAsString(searchParams));
                
                var result = myTeachingService.getMyTeachingAsync(searchParams);
                
                log.info("=== MyTeachingController.getMyTeaching 完成 ===");
                return result;
            } catch (Exception ex) {
                log.error("获取我的授课信息控制器异常", ex);
                return ResultHelper.error("获取授课信息失败: " + ex.getMessage());
            }
        });
    }
    
    /**
     * 获取我的授课课程班级列表
     */
    @GetMapping("/classes")
    public CompletableFuture<ApiResult> getMyTeachingClasses(@ModelAttribute MyTeachingSearchParams searchParams) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var result = myTeachingService.getMyTeachingClassesAsync(searchParams);
                return result;
            } catch (Exception ex) {
                return ResultHelper.error("获取课程班级列表失败: " + ex.getMessage());
            }
        });
    }
    
    /**
     * 获取指定班级的学生信息
     */
    @GetMapping("/students")
    public CompletableFuture<ApiResult> getClassStudents(@ModelAttribute MyTeachingSearchParams searchParams) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var result = myTeachingService.getClassStudentsAsync(searchParams);
                return result;
            } catch (Exception ex) {
                return ResultHelper.error("获取班级学生信息失败: " + ex.getMessage());
            }
        });
    }
}