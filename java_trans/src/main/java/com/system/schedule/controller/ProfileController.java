package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.common.ResultHelper;
import com.system.schedule.model.dto.UpdateInformationApplyDto;
import com.system.schedule.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/profile")
@Slf4j
public class ProfileController {
    
    @Autowired
    private ProfileService profileService;
    
    /**
     * 获取个人信息
     */
    @GetMapping("/get-profile")
    public CompletableFuture<ApiResult> getProfile(
            @RequestParam String userId,
            @RequestParam String userType) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 验证参数
                if (userId == null || userId.trim().isEmpty() || 
                    userType == null || userType.trim().isEmpty()) {
                    return ResultHelper.error("用户ID和用户类型不能为空");
                }
                
                var profile = profileService.getUserProfileAsync(userId, userType);
                return ResultHelper.success(profile);
            } catch (Exception ex) {
                return ResultHelper.error("获取个人信息失败: " + ex.getMessage());
            }
        });
    }
    
    /**
     * 提交修改申请
     */
    @PostMapping("/submit-update-apply")
    public CompletableFuture<ApiResult> submitUpdateApply(@RequestBody UpdateInformationApplyDto dto) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 验证参数
                if (dto.getUserId() == null || dto.getUserId().trim().isEmpty() || 
                    dto.getUserType() == null || dto.getUserType().trim().isEmpty()) {
                    return ResultHelper.error("用户ID和用户类型不能为空");
                }
                
                // 验证是否至少修改了一项
                if ((dto.getNewProfile() == null || dto.getNewProfile().trim().isEmpty()) && 
                    (dto.getNewPhoto() == null || dto.getNewPhoto().trim().isEmpty())) {
                    return ResultHelper.error("至少需要修改一项信息");
                }
                
                // 验证修改原因
                if (dto.getInformation() == null || dto.getInformation().trim().isEmpty()) {
                    return ResultHelper.error("请输入修改原因");
                }
                
                var result = profileService.submitUpdateApplyAsync(dto);
                return ResultHelper.success(result);
            } catch (Exception ex) {
                return ResultHelper.error("提交修改申请失败: " + ex.getMessage());
            }
        });
    }
}