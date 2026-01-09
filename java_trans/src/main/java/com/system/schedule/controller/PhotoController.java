package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.common.ResultHelper;
import com.system.schedule.entity.Photos;
import com.system.schedule.model.dto.*;
import com.system.schedule.service.PhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/photo")
@Slf4j
public class PhotoController {
    
    @Autowired
    private PhotoService photoService;
    
    /**
     * 获取头像列表
     */
    @GetMapping("/list")
    public CompletableFuture<ApiResult> getPhotoList(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String type) {
        return CompletableFuture.supplyAsync(() -> {
            var searchParams = new PhotoHandleSearchParams();
            searchParams.setId(id);
            searchParams.setType(type);
            
            return photoService.getPhotoListAsync(searchParams);
        });
    }
    
    /**
     * 获取头像详情（包含图片数据）
     */
    @GetMapping("/detail/{id}")
    public CompletableFuture<ApiResult> getPhotoDetail(@PathVariable String id) {
        return CompletableFuture.supplyAsync(() -> 
            photoService.getPhotoDetailAsync(id)
        );
    }
    
    /**
     * 上传/更新头像
     */
    @PostMapping("/upload")
    public CompletableFuture<ApiResult> uploadPhoto(@RequestBody PhotoUploadParams uploadParams) {
        return CompletableFuture.supplyAsync(() -> 
            photoService.uploadPhotoAsync(uploadParams)
        );
    }
    
    /**
     * 批量删除头像
     */
    @PostMapping("/batchDelete")
    public CompletableFuture<ApiResult> batchDeletePhoto(@RequestBody PhotoBatchDeleteParams deleteParams) {
        return CompletableFuture.supplyAsync(() -> 
            photoService.batchDeleteAsync(deleteParams.getIds())
        );
    }
    
    /**
     * 从更新申请更新头像到PHOTOS表（内部使用，不对外暴露）
     */
    @PostMapping("/updateFromApply")
    public CompletableFuture<ApiResult> updatePhotoFromApply(@RequestBody Map<String, Object> requestBody) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String applicantId = (String) requestBody.get("ApplicantId");
                String applicantType = (String) requestBody.get("ApplicantType");
                String newPhotoBase64 = (String) requestBody.get("NewPhotoBase64");
                
                return photoService.updatePhotoFromApplyAsync(applicantId, applicantType, newPhotoBase64);
            } catch (Exception ex) {
                return ResultHelper.error("参数解析失败: " + ex.getMessage());
            }
        });
    }
    
    /**
     * 清空PHOTOS表中的头像数据（内部使用，不对外暴露）
     */
    @PostMapping("/clearPhoto")
    public CompletableFuture<ApiResult> clearPhoto(@RequestBody Map<String, Object> requestBody) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String applicantId = (String) requestBody.get("ApplicantId");
                return photoService.clearPhotoAsync(applicantId);
            } catch (Exception ex) {
                return ResultHelper.error("参数解析失败: " + ex.getMessage());
            }
        });
    }
    
    /**
     * 删除单个头像
     */
    @PostMapping("/delete")
    public CompletableFuture<ApiResult> deletePhoto(@RequestBody PhotoSingleDeleteParams deleteParams) {
        return CompletableFuture.supplyAsync(() -> 
            photoService.deleteAsync(deleteParams.getId())
        );
    }
    
    /**
     * 测试方法 - 验证实体映射和数据查询
     */
    @GetMapping("/test")
    public CompletableFuture<ApiResult> testQuery() {
        return CompletableFuture.supplyAsync(() -> 
            photoService.testQuery()
        );
    }
}