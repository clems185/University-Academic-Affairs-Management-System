// UpdateInfoApplyService.java
package com.system.schedule.service;

import com.system.schedule.dto.UpdateInformationApply.*;
import com.system.schedule.entity.UpdateInformationApply;
import com.system.schedule.entity.Photo;
import com.system.schedule.mapper.UpdateInformationApplyMapper;
import com.system.schedule.mapper.PhotoMapper;
import com.system.schedule.util.ApiResult;
import com.system.schedule.util.ResultHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UpdateInfoApplyService {

    @Autowired
    private UpdateInformationApplyMapper updateInfoApplyMapper;
    
    @Autowired
    private PhotoService photoService;

    /**
     * 获取头像审批申请列表（只显示有头像的申请）
     */
    public ApiResult getUpdateInfoApplyListAsync(UpdateInfoApplySearchParams searchParams) {
        try {
            log.info("=== 开始查询头像审批申请列表 ===");
            
            // 构建查询条件
            UpdateInformationApply queryParams = new UpdateInformationApply();
            queryParams.setNewPhotoNotNull(true); // 只查询有头像的申请
            
            if (StringUtils.hasText(searchParams.getApplicantId())) {
                queryParams.setApplicantId(searchParams.getApplicantId());
            }
            
            if (StringUtils.hasText(searchParams.getApplicantType())) {
                queryParams.setApplicantType(searchParams.getApplicantType());
            }
            
            if (StringUtils.hasText(searchParams.getFinalDecision())) {
                queryParams.setFinalDecision(searchParams.getFinalDecision());
            }
            
            // 时间范围查询
            LocalDateTime applyTimeStart = searchParams.getApplyTimeStart();
            LocalDateTime applyTimeEnd = searchParams.getApplyTimeEnd();
            
            List<UpdateInformationApply> applies = updateInfoApplyMapper.selectApplyList(
                queryParams, applyTimeStart, applyTimeEnd, "apply_time DESC");
            
            log.info("查询到 {} 条申请记录", applies.size());
            
            if (!applies.isEmpty()) {
                UpdateInformationApply firstApply = applies.get(0);
                log.info("第一条记录详情: ApplyId={}, ApplicantId={}, HasPhoto={}", 
                    firstApply.getApplyId(), firstApply.getApplicantId(), 
                    firstApply.getNewPhoto() != null);
            } else {
                log.info("未找到任何有头像的申请记录");
                
                // 查询总记录数
                long totalCount = updateInfoApplyMapper.count();
                long withPhotoCount = updateInfoApplyMapper.countWithPhoto();
                
                log.info("数据库中总申请记录数: {}", totalCount);
                log.info("其中有头像的记录数: {}", withPhotoCount);
            }

            // 转换为 DTO
            List<UpdateInfoApplyItem> result = applies.stream()
                .map(a -> {
                    UpdateInfoApplyItem item = new UpdateInfoApplyItem();
                    item.setApplyId(a.getApplyId());
                    item.setApplicantId(a.getApplicantId());
                    item.setApplicantType(a.getApplicantType());
                    item.setApplyTime(a.getApplyTime());
                    item.setInformation(a.getInformation());
                    item.setHasNewProfile(StringUtils.hasText(a.getNewProfile()));
                    item.setHasNewPhoto(a.getNewPhoto() != null && a.getNewPhoto().length > 0);
                    item.setReviewTime(a.getReviewTime());
                    item.setReviewComments(a.getReviewComments());
                    item.setFinalDecision(a.getFinalDecision());
                    item.setFinalDecisionDisplay(getFinalDecisionDisplay(a.getFinalDecision()));
                    item.setApplicantTypeDisplay(getApplicantTypeDisplay(a.getApplicantType()));
                    return item;
                })
                .collect(Collectors.toList());

            log.info("=== 查询头像审批申请列表完成 ===");
            return ResultHelper.success(result);
        } catch (Exception ex) {
            log.error("查询头像审批申请列表异常: {}", ex.getMessage(), ex);
            return ResultHelper.error("获取申请列表失败: " + ex.getMessage());
        }
    }

    /**
     * 获取更新信息申请详情
     */
    public ApiResult getUpdateInfoApplyDetailAsync(String applyId) {
        try {
            log.info("=== 开始获取申请详情: {} ===", applyId);

            if (!StringUtils.hasText(applyId)) {
                return ResultHelper.error("申请ID不能为空");
            }

            UpdateInformationApply apply = updateInfoApplyMapper.selectById(applyId);
            
            if (apply == null) {
                log.info("未找到ID为 {} 的申请", applyId);
                return ResultHelper.error("申请不存在");
            }

            UpdateInfoApplyDetail result = new UpdateInfoApplyDetail();
            result.setApplyId(apply.getApplyId());
            result.setApplicantId(apply.getApplicantId());
            result.setApplicantType(apply.getApplicantType());
            result.setApplyTime(apply.getApplyTime());
            result.setInformation(apply.getInformation());
            result.setNewProfile(apply.getNewProfile());
            
            if (apply.getNewPhoto() != null && apply.getNewPhoto().length > 0) {
                result.setNewPhotoBase64(Base64.getEncoder().encodeToString(apply.getNewPhoto()));
            }
            
            result.setReviewTime(apply.getReviewTime());
            result.setReviewComments(apply.getReviewComments());
            result.setFinalDecision(apply.getFinalDecision());
            result.setFinalDecisionDisplay(getFinalDecisionDisplay(apply.getFinalDecision()));
            result.setApplicantTypeDisplay(getApplicantTypeDisplay(apply.getApplicantType()));

            log.info("=== 获取申请详情完成 ===");
            return ResultHelper.success(result);
        } catch (Exception ex) {
            log.error("获取申请详情异常: {}", ex.getMessage(), ex);
            return ResultHelper.error("获取申请详情失败: " + ex.getMessage());
        }
    }

    /**
     * 创建头像审批申请
     */
    @Transactional
    public ApiResult createPhotoApplyAsync(CreatePhotoApplyParams createParams) {
        try {
            log.info("=== 开始创建头像审批申请: {} ===", createParams.getApplicantId());

            // 参数验证
            if (!StringUtils.hasText(createParams.getApplicantId())) {
                return ResultHelper.error("申请人ID不能为空");
            }

            if (!StringUtils.hasText(createParams.getApplicantType())) {
                return ResultHelper.error("申请人类型不能为空");
            }

            if (!isValidApplicantType(createParams.getApplicantType())) {
                return ResultHelper.error("申请人类型必须是：学生、教师");
            }

            if (!StringUtils.hasText(createParams.getInformation())) {
                return ResultHelper.error("申请原因不能为空");
            }

            if (!StringUtils.hasText(createParams.getNewPhotoBase64())) {
                return ResultHelper.error("头像数据不能为空");
            }

            // 转换Base64为字节数组
            byte[] photoBytes;
            try {
                photoBytes = Base64.getDecoder().decode(createParams.getNewPhotoBase64());
            } catch (IllegalArgumentException ex) {
                log.error("Base64转换失败: {}", ex.getMessage());
                return ResultHelper.error("头像数据格式错误");
            }

            // 生成申请ID
            String applyId = generateApplyId();

            UpdateInformationApply newApply = new UpdateInformationApply();
            newApply.setApplyId(applyId);
            newApply.setApplicantId(createParams.getApplicantId());
            newApply.setApplicantType(createParams.getApplicantType());
            newApply.setApplyTime(LocalDateTime.now());
            newApply.setInformation(createParams.getInformation());
            newApply.setNewProfile(null); // 头像申请不包含简介
            newApply.setNewPhoto(photoBytes);
            newApply.setFinalDecision("P"); // 默认处理中

            int result = updateInfoApplyMapper.insert(newApply);

            if (result > 0) {
                log.info("=== 创建头像审批申请成功 ===");
                Map<String, Object> data = new HashMap<>();
                data.put("applyId", applyId);
                data.put("message", "申请创建成功");
                return ResultHelper.success(data);
            } else {
                return ResultHelper.error("申请创建失败");
            }
        } catch (Exception ex) {
            log.error("创建头像审批申请异常: {}", ex.getMessage(), ex);
            return ResultHelper.error("创建申请失败: " + ex.getMessage());
        }
    }

    /**
     * 审核更新信息申请
     */
    @Transactional
    public ApiResult reviewUpdateInfoApplyAsync(ReviewUpdateInfoApplyParams reviewParams) {
        try {
            log.info("=== 开始审核申请: {} ===", reviewParams.getApplyId());

            // 参数验证
            if (!StringUtils.hasText(reviewParams.getApplyId())) {
                return ResultHelper.error("申请ID不能为空");
            }

            if (!StringUtils.hasText(reviewParams.getFinalDecision())) {
                return ResultHelper.error("审核决定不能为空");
            }

            if (!"Y".equals(reviewParams.getFinalDecision()) && !"N".equals(reviewParams.getFinalDecision())) {
                return ResultHelper.error("审核决定必须是：Y（同意）或 N（拒绝）");
            }

            // 查找申请记录
            UpdateInformationApply apply = updateInfoApplyMapper.selectById(reviewParams.getApplyId());
            
            if (apply == null) {
                return ResultHelper.error("申请不存在");
            }

            if (!"P".equals(apply.getFinalDecision())) {
                return ResultHelper.error("该申请已被审核，无法重复审核");
            }

            // 更新申请状态
            UpdateInformationApply updateApply = new UpdateInformationApply();
            updateApply.setApplyId(reviewParams.getApplyId());
            updateApply.setFinalDecision(reviewParams.getFinalDecision());
            updateApply.setReviewTime(LocalDateTime.now());
            updateApply.setReviewComments(reviewParams.getReviewComments());
            
            int updateResult = updateInfoApplyMapper.updateById(updateApply);

            if (updateResult <= 0) {
                return ResultHelper.error("更新申请状态失败");
            }

            // 根据审核结果处理头像
            if ("Y".equals(reviewParams.getFinalDecision())) {
                // 审核通过，更新PHOTOS表
                if (apply.getNewPhoto() != null && apply.getNewPhoto().length > 0) {
                    // 将BLOB数据转换为Base64用于传递给PhotoService
                    String photoBase64 = Base64.getEncoder().encodeToString(apply.getNewPhoto());
                    ApiResult updatePhotoResult = photoService.updatePhotoFromApplyAsync(
                        apply.getApplicantId(), apply.getApplicantType(), photoBase64);
                    
                    if (!updatePhotoResult.isSuccess()) {
                        log.error("更新PHOTOS表失败: {}", updatePhotoResult.getMsg());
                        // 记录错误但继续执行，因为审核状态已成功更新
                    } else {
                        log.info("成功更新PHOTOS表");
                    }
                }
            }
            // 审核拒绝时不修改PHOTOS表，保持原有头像不变

            log.info("=== 审核申请成功 ===");
            String message = "审核" + ("Y".equals(reviewParams.getFinalDecision()) ? "通过" : "拒绝") + "成功";
            return ResultHelper.success(message);
        } catch (Exception ex) {
            log.error("审核申请异常: {}", ex.getMessage(), ex);
            return ResultHelper.error("审核申请失败: " + ex.getMessage());
        }
    }

    /**
     * 批量审核更新信息申请
     */
    @Transactional
    public ApiResult batchReviewUpdateInfoApplyAsync(BatchReviewUpdateInfoApplyParams batchReviewParams) {
        try {
            log.info("=== 开始批量审核申请: {} ===", 
                String.join(",", batchReviewParams.getApplyIds()));

            if (CollectionUtils.isEmpty(batchReviewParams.getApplyIds())) {
                return ResultHelper.error("请选择要审核的申请");
            }

            if (!StringUtils.hasText(batchReviewParams.getFinalDecision())) {
                return ResultHelper.error("审核决定不能为空");
            }

            if (!"Y".equals(batchReviewParams.getFinalDecision()) && !"N".equals(batchReviewParams.getFinalDecision())) {
                return ResultHelper.error("审核决定必须是：Y（同意）或 N（拒绝）");
            }

            int successCount = 0;
            List<String> errors = new ArrayList<>();

            for (String applyId : batchReviewParams.getApplyIds()) {
                ReviewUpdateInfoApplyParams reviewParams = new ReviewUpdateInfoApplyParams();
                reviewParams.setApplyId(applyId);
                reviewParams.setReviewComments(batchReviewParams.getReviewComments());
                reviewParams.setFinalDecision(batchReviewParams.getFinalDecision());

                ApiResult result = reviewUpdateInfoApplyAsync(reviewParams);
                if (result.isSuccess()) {
                    successCount++;
                } else {
                    errors.add("申请ID " + applyId + ": " + result.getMsg());
                }
            }

            log.info("=== 批量审核申请完成 ===");

            if (errors.isEmpty()) {
                String message = "成功" + ("Y".equals(batchReviewParams.getFinalDecision()) ? "通过" : "拒绝") + 
                               " " + successCount + " 个头像申请";
                return ResultHelper.success(message);
            } else if (successCount > 0) {
                String message = "成功处理 " + successCount + " 个头像申请，" + errors.size() + 
                               " 个申请处理失败：" + String.join("; ", errors);
                return ResultHelper.success(message);
            } else {
                return ResultHelper.error("批量审核失败：" + String.join("; ", errors));
            }
        } catch (Exception ex) {
            log.error("批量审核申请异常: {}", ex.getMessage(), ex);
            return ResultHelper.error("批量审核失败: " + ex.getMessage());
        }
    }

    /**
     * 删除更新信息申请
     */
    @Transactional
    public ApiResult deleteUpdateInfoApplyAsync(String applyId) {
        try {
            log.info("=== 开始删除申请: {} ===", applyId);

            if (!StringUtils.hasText(applyId)) {
                return ResultHelper.error("申请ID不能为空");
            }

            int deleteResult = updateInfoApplyMapper.deleteById(applyId);

            if (deleteResult > 0) {
                log.info("=== 删除申请成功 ===");
                return ResultHelper.success("删除成功");
            } else {
                return ResultHelper.error("删除失败，申请不存在");
            }
        } catch (Exception ex) {
            log.error("删除申请异常: {}", ex.getMessage(), ex);
            return ResultHelper.error("删除失败: " + ex.getMessage());
        }
    }

    /**
     * 批量删除更新信息申请
     */
    @Transactional
    public ApiResult batchDeleteUpdateInfoApplyAsync(List<String> applyIds) {
        try {
            log.info("=== 开始批量删除申请: {} ===", String.join(",", applyIds));

            if (CollectionUtils.isEmpty(applyIds)) {
                return ResultHelper.error("请选择要删除的申请");
            }

            int deleteResult = updateInfoApplyMapper.deleteBatchIds(applyIds);

            if (deleteResult > 0) {
                log.info("=== 批量删除申请成功 ===");
                return ResultHelper.success("成功删除 " + deleteResult + " 个申请");
            } else {
                return ResultHelper.error("删除失败，未找到指定的申请");
            }
        } catch (Exception ex) {
            log.error("批量删除申请异常: {}", ex.getMessage(), ex);
            return ResultHelper.error("批量删除失败: " + ex.getMessage());
        }
    }

    /**
     * 生成申请ID
     */
    private String generateApplyId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        Random random = new Random();
        int randomNum = random.nextInt(9000) + 1000; // 1000-9999
        return timestamp + randomNum;
    }

    /**
     * 获取最终决定显示文本
     */
    private String getFinalDecisionDisplay(String finalDecision) {
        if (finalDecision == null) return "未知";
        
        switch (finalDecision) {
            case "Y": return "已同意";
            case "N": return "已拒绝";
            case "P": return "处理中";
            default: return "未知";
        }
    }

    /**
     * 获取申请人类型显示文本
     */
    private String getApplicantTypeDisplay(String applicantType) {
        if (applicantType == null) return "未知";
        
        switch (applicantType) {
            case "学生": return "学生";
            case "教师": return "教师";
            default: return "未知";
        }
    }

    /**
     * 验证申请人类型是否有效
     */
    private boolean isValidApplicantType(String applicantType) {
        return "学生".equals(applicantType) || "教师".equals(applicantType);
    }
}