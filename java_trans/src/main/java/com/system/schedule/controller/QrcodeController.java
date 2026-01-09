package com.system.schedule.controller;

import com.system.schedule.entity.Sign;
import com.system.schedule.model.dto.SignReq;
import com.system.schedule.service.QrcodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/qrcode")
@Slf4j
public class QrcodeController {
    
    @Autowired
    private QrcodeService qrcodeService;
    
    /**
     * 获取签到学生列表
     */
    @GetMapping("/signin-list")
    public CompletableFuture<Map<String, Object>> getSignInStudents(@ModelAttribute SignReq req) {
        return CompletableFuture.supplyAsync(() -> {
            var students = qrcodeService.getCheckedInStudents(req);
            return Map.of("IsSuccess", true, "Result", students);
        });
    }
    
    /**
     * 学生签到
     */
    @PostMapping("/signin")
    public CompletableFuture<Map<String, Object>> studentSignIn(@RequestBody Sign sign) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("学生签到");
            log.info(sign.getSignTime().toString());
            
            boolean isTimeValid = false;
            long ts = sign.getTs();
            long epochTicks = 621355968000000000L;
            long interval = sign.getInterval() * 1000 + 5000;
            long nowts = ((System.currentTimeMillis() * 10000) - epochTicks) / 10000;
            
            log.info("签到时间戳：{}, 请求时间戳：{}, 时间间隔：{}", nowts, ts, interval);
            
            if (nowts - ts < interval) {
                isTimeValid = true;
            }
            
            if (isTimeValid) {
                var success = qrcodeService.addCheckedInStudent(sign);
                if (success) {
                    return Map.of("IsSuccess", true);
                }
                return Map.of("IsSuccess", false, "Message", "该学生已签到");
            } else {
                return Map.of("IsSuccess", false, "Message", "二维码过期");
            }
        });
    }
    
    /**
     * 获取班级列表
     */
    @GetMapping("/class-list")
    public CompletableFuture<Map<String, Object>> getClassList(@RequestParam String teacherId) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("查询班级列表，教师ID为：" + teacherId);
            
            var classes = qrcodeService.getClassList(teacherId);
            return Map.of("IsSuccess", true, "Result", classes);
        });
    }
    
    /**
     * 获取签到列表
     */
    @GetMapping("/sign-list")
    public CompletableFuture<Map<String, Object>> getSignList(
            @RequestParam String teacherId,
            @RequestParam(required = false) String classId,
            @RequestParam(required = false) String signId) {
        return CompletableFuture.supplyAsync(() -> 
            qrcodeService.getSignList(teacherId, classId, signId)
        );
    }
}