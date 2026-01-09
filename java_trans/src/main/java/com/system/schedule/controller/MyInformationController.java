package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.common.ResultHelper;
import com.system.schedule.entity.*;
import com.system.schedule.model.dto.*;
import com.system.schedule.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/my-information")
@Slf4j
public class MyInformationController {
    
    @Autowired
    private MyInformationService myInformationService;
    
    /**
     * 获取当前用户的信息通知
     * @param types 信息类型(多个用逗号分隔)
     * @param startTime 筛选起始时间(yyyy-MM-dd)
     * @param page 页码(默认1)
     * @param pageSize 每页数量(默认10)
     */
    @GetMapping
    public ApiResult getMyInformations(
            @RequestParam(required = false) String types,
            @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(defaultValue = "1") 
            @Min(value = 1, message = "页码必须大于0") int page,
            @RequestParam(defaultValue = "10") 
            @Min(value = 1, message = "每页数量至少为1") 
            @Max(value = 100, message = "每页数量最多为100") int pageSize) {
        
        try {
            log.info("获取用户通知: 类型={} 时间={} 页码={} 每页={}",
                    types == null ? "ALL" : types,
                    startTime == null ? "ALL" : startTime,
                    page, pageSize);
            
            return myInformationService.getMyInformations(types, startTime, page, pageSize);
        } catch (Exception ex) {
            log.error("获取用户通知失败", ex);
            return ResultHelper.error("获取通知信息失败，请稍后重试");
        }
    }
}