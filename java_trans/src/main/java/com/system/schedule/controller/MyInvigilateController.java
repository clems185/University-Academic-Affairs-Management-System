package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.common.ResultHelper;
import com.system.schedule.model.dto.GenerateSeatingReq;
import com.system.schedule.service.MyInvigilateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/my-invigilate")
@Slf4j
@Validated
public class MyInvigilateController {
    
    @Autowired
    private MyInvigilateService myInvigilateService;
    
    /**
     * 获取教师监考课程列表
     */
    @GetMapping("/courses/{teacherId}")
    public CompletableFuture<ApiResult> getInvigilateCourses(@PathVariable String teacherId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (teacherId == null || teacherId.trim().isEmpty()) {
                    return ResultHelper.error("教师ID不能为空");
                }
                
                var courses = myInvigilateService.getInvigilateCourses(teacherId);
                log.info("[MyInvigilateController] GetInvigilateCourses done, teacherId={}, resultCount={}", 
                        teacherId, courses != null ? courses.size() : 0);
                return ResultHelper.success(courses);
            } catch (Exception ex) {
                log.error("[MyInvigilateController] 获取监考课程列表失败", ex);
                return ResultHelper.error("获取监考课程列表失败");
            }
        });
    }
    
    /**
     * 生成座位表
     */
    @PostMapping("/generate-seating")
    public CompletableFuture<ApiResult> generateSeatingChart(@Valid @RequestBody GenerateSeatingReq req) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("MyInvigilate generate-seating start");
                
                var result = myInvigilateService.generateSeatingChart(req);
                log.info("MyInvigilate generate-seating end");
                return result;
            } catch (Exception ex) {
                log.error("生成座位表失败", ex);
                return ResultHelper.error("生成座位表失败");
            }
        });
    }
}