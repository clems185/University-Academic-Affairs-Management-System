// InformationApplyController.java
package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.dto.InformationApplyReq;
import com.system.schedule.service.IInformationApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/informationapply")
@Api(tags = "信息申请控制器")
@PreAuthorize("isAuthenticated()")
@Validated
public class InformationApplyController extends BaseController {
    
    @Autowired
    private IInformationApplyService infoApplyService;
    
    @PostMapping("/submit")
    @ApiOperation("提交消息申请")
    public ApiResult<?> submitApply(@Valid @RequestBody InformationApplyReq req) {
        String currentUserId = getCurrentUserId();
        
        try {
            if (currentUserId == null || currentUserId.isEmpty()) {
                logger.error("无法从Token中获取用户ID");
                return ApiResult.error("无法获取当前用户ID");
            }
            
            logger.info("用户 {} 正在提交消息申请", currentUserId);
            return infoApplyService.submitInformationApply(req, currentUserId);
        } catch (Exception ex) {
            logger.error("用户 {} 提交消息申请失败", currentUserId, ex);
            return ApiResult.error("提交消息申请失败");
        }
    }
    
    @GetMapping("/list")
    @ApiOperation("获取当前用户的消息申请列表")
    public ApiResult<?> getApplyList() {
        String currentUserId = getCurrentUserId();
        
        try {
            if (currentUserId == null || currentUserId.isEmpty()) {
                logger.error("无法从Token中获取用户ID");
                return ApiResult.error("无法获取当前用户ID");
            }
            
            logger.info("用户 {} 正在获取申请列表", currentUserId);
            Object applies = infoApplyService.getInformationApplyList(currentUserId);
            return ApiResult.success(applies);
        } catch (Exception ex) {
            logger.error("用户 {} 获取消息申请列表失败", currentUserId, ex);
            return ApiResult.error("获取消息申请列表失败");
        }
    }
    
    @PostMapping("/withdraw/{infoApplyId}")
    @ApiOperation("撤回消息申请")
    public ApiResult<?> withdrawApply(@PathVariable String infoApplyId) {
        String currentUserId = getCurrentUserId();
        
        try {
            if (currentUserId == null || currentUserId.isEmpty()) {
                logger.error("无法从Token中获取用户ID");
                return ApiResult.error("无法获取当前用户ID");
            }
            
            if (infoApplyId == null || infoApplyId.isEmpty()) {
                return ApiResult.error("申请ID不能为空");
            }
            
            logger.info("用户 {} 正在撤回申请 {}", currentUserId, infoApplyId);
            return infoApplyService.withdrawInformationApply(infoApplyId, currentUserId);
        } catch (Exception ex) {
            logger.error("用户 {} 撤回申请 {} 失败", currentUserId, infoApplyId, ex);
            return ApiResult.error("撤回消息申请失败");
        }
    }
    
    @PostMapping("/batchDelete")
    @ApiOperation("批量删除申请")
    public ApiResult<?> batchDeleteApply(@RequestBody List<String> infoApplyIds) {
        String currentUserId = getCurrentUserId();
        
        try {
            if (currentUserId == null || currentUserId.isEmpty()) {
                logger.error("无法从Token中获取用户ID");
                return ApiResult.error("无法获取当前用户ID");
            }
            
            if (infoApplyIds == null || infoApplyIds.isEmpty()) {
                return ApiResult.error("申请ID列表不能为空");
            }
            
            logger.info("用户 {} 正在批量删除申请，数量: {}", currentUserId, infoApplyIds.size());
            return infoApplyService.batchDeleteInformationApply(infoApplyIds, currentUserId);
        } catch (Exception ex) {
            logger.error("用户 {} 批量删除申请失败", currentUserId, ex);
            return ApiResult.error("批量删除申请失败");
        }
    }
}