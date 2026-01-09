package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.common.ResultHelper;
import com.system.schedule.service.MyExamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/my-exam")
@Slf4j
public class MyExamController {
    
    @Autowired
    private MyExamService myExamService;
    
    /**
     * 获取学生考试列表
     */
    @GetMapping("/exams/{studentId}")
    @PreAuthorize("isAuthenticated()")
    public CompletableFuture<ApiResult> getMyExams(@PathVariable String studentId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (studentId == null || studentId.trim().isEmpty()) {
                    return ResultHelper.error("学生ID不能为空");
                }
                
                log.info("[MyExamController] GetMyExams start, studentId={}", studentId);
                
                var exams = myExamService.getMyExams(studentId);
                
                log.info("[MyExamController] GetMyExams done, studentId={}, resultCount={}", 
                        studentId, exams != null ? exams.size() : 0);
                
                return ResultHelper.success(exams);
            } catch (Exception ex) {
                log.error("[MyExamController] 获取学生考试列表失败", ex);
                return ResultHelper.error("获取学生考试列表失败");
            }
        });
    }
}