package com.system.schedule.service;

import com.system.schedule.dto.informationapply.InformationApplyReq;
import com.system.schedule.dto.informationapply.InformationApplyRes;
import com.system.schedule.dto.other.ApiResult;
import com.system.schedule.entity.InformationApply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class InformationApplyServiceImpl implements IInformationApplyService {
    private static final Logger logger = LoggerFactory.getLogger(InformationApplyServiceImpl.class);

    // 允许的类型值
    private static final String[] ALLOWED_TYPES = { "通知", "公告", "新闻", "文件", "活动" };

    // 这里需要替换为实际的数据库操作类
    // @Autowired
    // private InformationApplyMapper informationApplyMapper;
    // 或者使用ORM框架如MyBatis Plus, JPA等

    public InformationApplyServiceImpl() {
        // 如果需要构造函数注入，可以在这里添加
    }

    @Override
    public CompletableFuture<ApiResult> submitInformationApply(InformationApplyReq req, String applicantId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("[消息申请] 用户 {} 开始提交申请", applicantId);

                // 验证输入
                if (req.getTitle() == null || req.getTitle().trim().isEmpty() ||
                        req.getContent() == null || req.getContent().trim().isEmpty()) {
                    logger.warn("[消息申请] 用户 {} 提交失败: 标题或内容为空", applicantId);
                    return ApiResult.error("标题和内容不能为空");
                }

                // 验证类型是否在允许范围内
                boolean isValidType = false;
                for (String allowedType : ALLOWED_TYPES) {
                    if (allowedType.equals(req.getTypes())) {
                        isValidType = true;
                        break;
                    }
                }

                if (!isValidType) {
                    logger.warn("[消息申请] 用户 {} 提交失败: 无效类型 '{}'", applicantId, req.getTypes());
                    String allowedTypesStr = String.join(", ", ALLOWED_TYPES);
                    return ApiResult.error("无效的类型: " + req.getTypes() + "。允许的类型: " + allowedTypesStr);
                }

                // 生成申请ID
                String applyId = generateApplyId();
                logger.debug("[消息申请] 生成申请ID: {}", applyId);

                // 创建申请记录
                InformationApply apply = new InformationApply();
                apply.setInfoApplyId(applyId);
                apply.setApplicantId(applicantId);
                apply.setApplyTime(LocalDateTime.now());
                apply.setTypes(req.getTypes());
                apply.setTitle(req.getTitle());
                apply.setContent(req.getContent());
                apply.setApplyComment(req.getApplyComment());
                apply.setFinalDecision("P"); // P-待审核

                // 保存到数据库
                // int result = informationApplyMapper.insert(apply);
                // 模拟数据库操作成功
                int result = 1;

                if (result > 0) {
                    logger.info("[消息申请] 用户 {} 提交成功, 申请ID: {}, 标题: '{}'", applicantId, applyId, req.getTitle());
                    return ApiResult.success("消息申请提交成功");
                } else {
                    logger.error("[消息申请] 用户 {} 提交失败: 数据库插入操作返回0", applicantId);
                    return ApiResult.error("消息申请提交失败");
                }
            } catch (Exception ex) {
                logger.error("[消息申请] 用户 {} 提交失败: {}", applicantId, ex.getMessage(), ex);
                return ApiResult.error("消息申请提交失败: " + ex.getMessage());
            }
        });
    }

    @Override
    public CompletableFuture<List<InformationApplyRes>> getInformationApplyList(String applicantId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("[消息申请] 用户 {} 请求申请列表", applicantId);

                // 查询数据库获取申请列表
                // List<InformationApply> applies = informationApplyMapper.selectByApplicantId(applicantId);
                // 模拟数据库查询结果
                List<InformationApply> applies = new ArrayList<>();

                // 转换为DTO
                List<InformationApplyRes> result = applies.stream().map(apply -> {
                    InformationApplyRes res = new InformationApplyRes();
                    res.setInfoApplyId(apply.getInfoApplyId());
                    res.setApplicantId(apply.getApplicantId());
                    res.setApplyTime(apply.getApplyTime());
                    res.setTypes(apply.getTypes());
                    res.setTitle(apply.getTitle());
                    res.setContent(apply.getContent());
                    res.setApplyComment(apply.getApplyComment());
                    res.setReviewTime(apply.getReviewTime());
                    res.setReviewContents(apply.getReviewContents());
                    res.setFinalDecision(apply.getFinalDecision());
                    res.setReviewId(apply.getReviewId());
                    
                    // 设置状态文本
                    if ("P".equals(apply.getFinalDecision())) {
                        res.setStatusText("待审核");
                    } else if ("Y".equals(apply.getFinalDecision())) {
                        res.setStatusText("已通过");
                    } else if ("R".equals(apply.getFinalDecision())) {
                        res.setStatusText("已拒绝");
                    } else {
                        res.setStatusText("未知状态");
                    }
                    
                    return res;
                }).collect(Collectors.toList());

                logger.info("[消息申请] 用户 {} 获取到 {} 条申请记录", applicantId, result.size());

                return result;
            } catch (Exception ex) {
                logger.error("[消息申请] 用户 {} 获取列表失败: {}", applicantId, ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public CompletableFuture<ApiResult> withdrawInformationApply(String infoApplyId, String applicantId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("[消息申请] 用户 {} 尝试撤回申请 {}", applicantId, infoApplyId);

                // 查询申请记录
                // InformationApply apply = informationApplyMapper.selectById(infoApplyId);
                // 模拟数据库查询结果
                InformationApply apply = new InformationApply();
                apply.setInfoApplyId(infoApplyId);
                apply.setApplicantId(applicantId);
                apply.setFinalDecision("P"); // 模拟待审核状态
                apply.setTitle("测试标题");

                if (apply == null) {
                    logger.warn("[消息申请] 用户 {} 撤回失败: 申请不存在或无权操作 {}", applicantId, infoApplyId);
                    return ApiResult.error("申请不存在或无权操作");
                }

                // 只能撤回待审核的申请
                if (!"P".equals(apply.getFinalDecision())) {
                    logger.warn("[消息申请] 用户 {} 撤回失败: 申请状态为 '{}' 不可撤回 {}", applicantId, apply.getFinalDecision(), infoApplyId);
                    return ApiResult.error("只能撤回待审核的申请");
                }

                // 从数据库删除
                // int result = informationApplyMapper.deleteById(infoApplyId);
                // 模拟数据库操作成功
                int result = 1;

                if (result > 0) {
                    logger.info("[消息申请] 用户 {} 成功撤回申请 {}, 标题: '{}'", applicantId, infoApplyId, apply.getTitle());
                    return ApiResult.success("撤回成功");
                } else {
                    logger.error("[消息申请] 用户 {} 撤回失败: 数据库操作返回0 {}", applicantId, infoApplyId);
                    return ApiResult.error("撤回失败");
                }
            } catch (Exception ex) {
                logger.error("[消息申请] 用户 {} 撤回失败: {} {}", applicantId, ex.getMessage(), infoApplyId, ex);
                return ApiResult.error("撤回失败: " + ex.getMessage());
            }
        });
    }

    @Override
    public CompletableFuture<ApiResult> batchDeleteInformationApply(List<String> infoApplyIds, String applicantId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("[消息申请] 用户 {} 尝试批量删除申请，数量: {}", applicantId, infoApplyIds.size());

                // 查询符合条件的申请（当前用户的且状态为已通过或已拒绝）
                // List<InformationApply> validApplies = informationApplyMapper.selectValidApplies(infoApplyIds, applicantId);
                // 模拟数据库查询结果
                List<InformationApply> validApplies = new ArrayList<>();

                if (validApplies.isEmpty()) {
                    logger.warn("[消息申请] 用户 {} 批量删除失败: 没有符合条件的申请", applicantId);
                    return ApiResult.error("没有符合条件的申请可以删除");
                }

                // 获取有效的申请ID列表
                List<String> validIds = validApplies.stream()
                        .map(InformationApply::getInfoApplyId)
                        .collect(Collectors.toList());

                // 执行批量删除
                // int result = informationApplyMapper.batchDelete(validIds);
                // 模拟数据库操作成功
                int result = validIds.size();

                if (result > 0) {
                    logger.info("[消息申请] 用户 {} 成功批量删除了 {} 条申请", applicantId, result);
                    return ApiResult.success("成功删除 " + result + " 条申请");
                } else {
                    logger.warn("[消息申请] 用户 {} 批量删除失败: 数据库操作未影响任何记录", applicantId);
                    return ApiResult.error("删除失败，未找到符合条件的申请");
                }
            } catch (Exception ex) {
                logger.error("[消息申请] 用户 {} 批量删除失败: {}", applicantId, ex.getMessage(), ex);
                return ApiResult.error("批量删除失败: " + ex.getMessage());
            }
        });
    }

    private String generateApplyId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        String date = LocalDateTime.now().format(formatter);
        String prefix = "IN" + date;
        int maxRetry = 5; // 最大重试次数
        int retryCount = 0;

        while (retryCount < maxRetry) {
            // 查找当天最大的申请ID
            // String maxId = informationApplyMapper.findMaxIdByPrefix(prefix);
            // 模拟查找结果
            String maxId = null;

            int sequence = 1;

            if (maxId != null && maxId.length() == 9) {
                String sequenceStr = maxId.substring(6, 9);
                try {
                    int existingSequence = Integer.parseInt(sequenceStr);
                    sequence = existingSequence + 1;
                } catch (NumberFormatException e) {
                    logger.warn("解析序列号失败: {}", sequenceStr, e);
                }
            }

            if (sequence > 999) {
                throw new RuntimeException("当天消息申请数量已达上限(999)");
            }

            String newId = String.format("%s%03d", prefix, sequence);

            // 检查ID是否已存在
            // boolean exists = informationApplyMapper.existsById(newId);
            // 模拟不存在
            boolean exists = false;

            if (!exists) {
                logger.debug("[ID生成] 生成唯一申请ID: {}", newId);
                return newId;
            }

            logger.warn("[ID生成] 检测到重复ID {}，将重试生成", newId);
            retryCount++;
        }

        throw new RuntimeException("生成唯一ID失败，超过最大重试次数");
    }
}

package com.system.schedule.entity;

import java.time.LocalDateTime;

public class InformationApply {
    private String infoApplyId;
    private String applicantId;
    private LocalDateTime applyTime;
    private String types;
    private String title;
    private String content;
    private String applyComment;
    private LocalDateTime reviewTime;
    private String reviewContents;
    private String finalDecision;
    private String reviewId;

    // Getters and Setters
    public String getInfoApplyId() {
        return infoApplyId;
    }

    public void setInfoApplyId(String infoApplyId) {
        this.infoApplyId = infoApplyId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public LocalDateTime getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(LocalDateTime applyTime) {
        this.applyTime = applyTime;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getApplyComment() {
        return applyComment;
    }

    public void setApplyComment(String applyComment) {
        this.applyComment = applyComment;
    }

    public LocalDateTime getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(LocalDateTime reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getReviewContents() {
        return reviewContents;
    }

    public void setReviewContents(String reviewContents) {
        this.reviewContents = reviewContents;
    }

    public String getFinalDecision() {
        return finalDecision;
    }

    public void setFinalDecision(String finalDecision) {
        this.finalDecision = finalDecision;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }
}

package com.system.schedule.dto.other;

public class ApiResult {
    private boolean isSuccess;
    private Object result;
    private String msg;

    // Constructors
    public ApiResult() {}

    public ApiResult(boolean isSuccess, Object result, String msg) {
        this.isSuccess = isSuccess;
        this.result = result;
        this.msg = msg;
    }

    // Static factory methods
    public static ApiResult success(Object data) {
        return new ApiResult(true, data, "");
    }

    public static ApiResult success(String message) {
        return new ApiResult(true, null, message);
    }

    public static ApiResult error(String message) {
        return new ApiResult(false, null, message);
    }

    // Getters and Setters
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}