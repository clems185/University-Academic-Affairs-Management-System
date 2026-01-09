package com.system.schedule.service.impl;

import com.system.schedule.service.IInformationApplyService;
import com.system.schedule.dto.informationapply.InformationApplyReq;
import com.system.schedule.dto.informationapply.InformationApplyRes;
import com.system.schedule.entity.InformationApply;
import com.system.schedule.common.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class InformationApplyServiceImpl implements IInformationApplyService {
    private static final Logger logger = LoggerFactory.getLogger(InformationApplyServiceImpl.class);
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMdd");
    private static final Random random = new Random();
    
    // 允许的类型值
    private static final List<String> ALLOWED_TYPES = List.of("通知", "公告", "新闻", "文件", "活动");
    
    // 模拟数据库操作，实际项目中应使用Spring Data JPA或MyBatis等ORM框架
    private List<InformationApply> informationApplies = new ArrayList<>();

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
                if (!ALLOWED_TYPES.contains(req.getTypes())) {
                    logger.warn("[消息申请] 用户 {} 提交失败: 无效类型 '{}'",
                        applicantId, req.getTypes());
                    return ApiResult.error(String.format("无效的类型: %s。允许的类型: %s", 
                        req.getTypes(), String.join(", ", ALLOWED_TYPES)));
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
                informationApplies.add(apply);

                logger.info("[消息申请] 用户 {} 提交成功, 申请ID: {}, 标题: '{}'",
                    applicantId, applyId, req.getTitle());
                return ApiResult.success("消息申请提交成功");
            } catch (Exception ex) {
                logger.error(ex, "[消息申请] 用户 {} 提交失败: {}",
                    applicantId, ex.getMessage());
                return ApiResult.error("消息申请提交失败: " + ex.getMessage());
            }
        }, executor);
    }

    @Override
    public CompletableFuture<List<InformationApplyRes>> getInformationApplyList(String applicantId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("[消息申请] 用户 {} 请求申请列表", applicantId);

                List<InformationApplyRes> list = informationApplies.stream()
                    .filter(a -> a.getApplicantId().equals(applicantId))
                    .sorted((a1, a2) -> a2.getApplyTime().compareTo(a1.getApplyTime())) // 降序排序
                    .map(a -> {
                        InformationApplyRes res = new InformationApplyRes();
                        res.setInfoApplyId(a.getInfoApplyId());
                        res.setApplicantId(a.getApplicantId());
                        res.setApplyTime(a.getApplyTime());
                        res.setTypes(a.getTypes());
                        res.setTitle(a.getTitle());
                        res.setContent(a.getContent());
                        res.setApplyComment(a.getApplyComment());
                        res.setReviewTime(a.getReviewTime());
                        res.setReviewContents(a.getReviewContents());
                        res.setFinalDecision(a.getFinalDecision());
                        res.setReviewId(a.getReviewId());
                        
                        // 设置状态文本
                        String statusText;
                        if ("P".equals(a.getFinalDecision())) {
                            statusText = "待审核";
                        } else if ("Y".equals(a.getFinalDecision())) {
                            statusText = "已通过";
                        } else if ("R".equals(a.getFinalDecision())) {
                            statusText = "已拒绝";
                        } else {
                            statusText = "未知状态";
                        }
                        res.setStatusText(statusText);
                        
                        return res;
                    })
                    .collect(Collectors.toList());

                logger.info("[消息申请] 用户 {} 获取到 {} 条申请记录",
                    applicantId, list.size());

                return list;
            } catch (Exception ex) {
                logger.error(ex, "[消息申请] 用户 {} 获取列表失败: {}",
                    applicantId, ex.getMessage());
                throw new RuntimeException(ex);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<ApiResult> withdrawInformationApply(String infoApplyId, String applicantId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("[消息申请] 用户 {} 尝试撤回申请 {}",
                    applicantId, infoApplyId);

                // 查找申请
                InformationApply apply = informationApplies.stream()
                    .filter(a -> a.getInfoApplyId().equals(infoApplyId) && a.getApplicantId().equals(applicantId))
                    .findFirst()
                    .orElse(null);

                if (apply == null) {
                    logger.warn("[消息申请] 用户 {} 撤回失败: 申请不存在或无权操作 {}",
                        applicantId, infoApplyId);
                    return ApiResult.error("申请不存在或无权操作");
                }

                // 只能撤回待审核的申请
                if (!"P".equals(apply.getFinalDecision())) {
                    logger.warn("[消息申请] 用户 {} 撤回失败: 申请状态为 '{}' 不可撤回 {}",
                        applicantId, apply.getFinalDecision(), infoApplyId);
                    return ApiResult.error("只能撤回待审核的申请");
                }

                // 删除申请
                boolean result = informationApplies.remove(apply);

                if (result) {
                    logger.info("[消息申请] 用户 {} 成功撤回申请 {}, 标题: '{}'",
                        applicantId, infoApplyId, apply.getTitle());
                    return ApiResult.success("撤回成功");
                } else {
                    logger.error("[消息申请] 用户 {} 撤回失败: 数据库操作失败 {}",
                        applicantId, infoApplyId);
                    return ApiResult.error("撤回失败");
                }
            } catch (Exception ex) {
                logger.error(ex, "[消息申请] 用户 {} 撤回失败: {} {}",
                    applicantId, ex.getMessage(), infoApplyId);
                return ApiResult.error("撤回失败: " + ex.getMessage());
            }
        }, executor);
    }

    @Override
    public CompletableFuture<ApiResult> batchDeleteInformationApply(List<String> infoApplyIds, String applicantId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("[消息申请] 用户 {} 尝试批量删除申请，数量: {}",
                    applicantId, infoApplyIds.size());

                // 统计删除成功的数量
                int deletedCount = 0;
                
                // 使用迭代器进行安全删除
                for (Iterator<InformationApply> iterator = informationApplies.iterator(); iterator.hasNext();) {
                    InformationApply apply = iterator.next();
                    if (infoApplyIds.contains(apply.getInfoApplyId()) &&
                        apply.getApplicantId().equals(applicantId) &&
                        ("Y".equals(apply.getFinalDecision()) || "R".equals(apply.getFinalDecision()))) {
                        iterator.remove();
                        deletedCount++;
                    }
                }

                if (deletedCount == 0) {
                    logger.warn("[消息申请] 用户 {} 批量删除失败: 没有符合条件的申请", applicantId);
                    return ApiResult.error("没有符合条件的申请可以删除");
                }

                logger.info("[消息申请] 用户 {} 成功批量删除了 {} 条申请",
                    applicantId, deletedCount);
                return ApiResult.success(String.format("成功删除 %d 条申请", deletedCount));
            } catch (Exception ex) {
                logger.error(ex, "[消息申请] 用户 {} 批量删除失败: {}",
                    applicantId, ex.getMessage());
                return ApiResult.error("批量删除失败: " + ex.getMessage());
            }
        }, executor);
    }

    private synchronized String generateApplyId() {
        try {
            String date = LocalDateTime.now().format(DATE_FORMATTER);
            String prefix = "IN" + date;
            int maxRetry = 5; // 最大重试次数
            int retryCount = 0;

            while (retryCount < maxRetry) {
                // 查找当天最大的申请ID
                String maxId = null;
                for (InformationApply apply : informationApplies) {
                    if (apply.getInfoApplyId().startsWith(prefix)) {
                        if (maxId == null || apply.getInfoApplyId().compareTo(maxId) > 0) {
                            maxId = apply.getInfoApplyId();
                        }
                    }
                }

                int sequence = 1;

                if (maxId != null && maxId.length() == 9) {
                    try {
                        String sequenceStr = maxId.substring(6, 9);
                        sequence = Integer.parseInt(sequenceStr) + 1;
                    } catch (Exception e) {
                        // 解析失败，使用默认序列1
                    }
                }

                if (sequence > 999) {
                    throw new RuntimeException("当天消息申请数量已达上限(999)");
                }

                String newId = prefix + String.format("%03d", sequence);

                // 检查ID是否已存在
                boolean exists = informationApplies.stream()
                    .anyMatch(a -> a.getInfoApplyId().equals(newId));

                if (!exists) {
                    logger.debug("[ID生成] 生成唯一申请ID: {}", newId);
                    return newId;
                }

                logger.warn("[ID生成] 检测到重复ID {}，将重试生成", newId);
                retryCount++;
            }

            throw new RuntimeException("生成唯一ID失败，超过最大重试次数");
        } catch (Exception ex) {
            logger.error(ex, "[ID生成] 生成申请ID失败: {}", ex.getMessage());
            throw new RuntimeException("生成申请ID失败: " + ex.getMessage());
        }
    }
}