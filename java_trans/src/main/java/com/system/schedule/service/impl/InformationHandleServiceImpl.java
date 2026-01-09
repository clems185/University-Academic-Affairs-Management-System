package com.system.schedule.service.impl;

import com.system.schedule.service.IInformationHandleService;
import com.system.schedule.dto.informationapply.InformationApplyRes;
import com.system.schedule.dto.informationapply.InformationHandleReq;
import com.system.schedule.entity.InformationApply;
import com.system.schedule.entity.Information;
import com.system.schedule.common.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class InformationHandleServiceImpl implements IInformationHandleService {
    private static final Logger logger = LoggerFactory.getLogger(InformationHandleServiceImpl.class);
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMdd");
    private static final Random random = new Random();
    
    // 模拟数据库操作，实际项目中应使用Spring Data JPA或MyBatis等ORM框架
    private List<InformationApply> informationApplies = new ArrayList<>();
    private List<Information> informationList = new ArrayList<>();

    @Override
    public CompletableFuture<List<InformationApplyRes>> getPendingApplications() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("[消息审核] 获取待审核申请列表");
                return informationApplies.stream()
                    .filter(a -> "P".equals(a.getFinalDecision())) // 只获取待审核的
                    .sorted((a1, a2) -> a1.getApplyTime().compareTo(a2.getApplyTime())) // 升序排序
                    .map(a -> {
                        InformationApplyRes res = new InformationApplyRes();
                        res.setInfoApplyId(a.getInfoApplyId());
                        res.setApplicantId(a.getApplicantId());
                        res.setApplyTime(a.getApplyTime());
                        res.setTypes(a.getTypes());
                        res.setTitle(a.getTitle());
                        res.setContent(a.getContent());
                        res.setApplyComment(a.getApplyComment());
                        res.setStatusText("待审核");
                        return res;
                    })
                    .collect(Collectors.toList());
            } catch (Exception ex) {
                logger.error(ex, "[消息审核] 获取待审核列表失败: {}", ex.getMessage());
                throw new RuntimeException(ex);
            }
        }, executor);
    }

    @Override
    public CompletableFuture<ApiResult> reviewApplication(InformationHandleReq req, String reviewerId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("[消息审核] 审核员 {} 开始审核申请 {}",
                    reviewerId, req.getInfoApplyId());
                logger.debug("[消息审核] 审核请求参数: ApplyId={}, Decision={}, CommentsLength={}",
                    req.getInfoApplyId(), req.getFinalDecision(), req.getReviewComments() != null ? req.getReviewComments().length() : 0);

                // 模拟事务处理
                logger.debug("[消息审核] 事务开始: ApplyId={}", req.getInfoApplyId());

                // 获取申请记录
                InformationApply apply = informationApplies.stream()
                    .filter(a -> a.getInfoApplyId().equals(req.getInfoApplyId()) && "P".equals(a.getFinalDecision()))
                    .findFirst()
                    .orElse(null);

                if (apply == null) {
                    logger.warn("[消息审核] 申请 {} 不存在或已被审核", req.getInfoApplyId());
                    return ApiResult.error("申请不存在或已被审核");
                }

                logger.debug("[消息审核] 获取到待审核记录: Title={}, Type={}, ApplyTime={}",
                    apply.getTitle(), apply.getTypes(), apply.getApplyTime());

                // 更新审核信息
                apply.setReviewId(reviewerId);
                apply.setReviewTime(LocalDateTime.now());
                apply.setFinalDecision(req.getFinalDecision());
                apply.setReviewContents(req.getReviewComments());

                logger.info("[消息审核] 申请 {} 更新成功", req.getInfoApplyId());

                // 审核通过时创建信息记录
                if ("Y".equals(req.getFinalDecision())) {
                    Information info = new Information();
                    info.setInfoId(generateInfoId());
                    info.setTypes(apply.getTypes());
                    info.setTitle(apply.getTitle());
                    info.setContent(apply.getContent());
                    info.setPublishTime(LocalDateTime.now());

                    informationList.add(info);

                    logger.info("[审计] 信息发布: ApplyId={}, Title={}, Type={}",
                        req.getInfoApplyId(), apply.getTitle(), apply.getTypes());
                }

                logger.info("[消息审核] 申请 {} 审核成功，结果: {}",
                    req.getInfoApplyId(), "Y".equals(req.getFinalDecision()) ? "通过" : "拒绝");

                return ApiResult.success("审核成功");
            } catch (Exception ex) {
                logger.error(ex, "[消息审核] 审核申请 {} 失败: {}",
                    req.getInfoApplyId(), ex.getMessage());
                return ApiResult.error("审核失败: " + ex.getMessage());
            }
        }, executor);
    }

    @Override
    public CompletableFuture<ApiResult> bulkDeleteReviewedApplications(List<String> applyIds) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("[消息清理] 批量删除已审核记录: 总数={}", applyIds.size());

                // 记录采样ID用于调试（最多5个）
                List<String> sampleIds = applyIds.stream().limit(5).collect(Collectors.toList());
                logger.debug("[消息清理] 删除记录采样: SampleIds={}", String.join(",", sampleIds));

                // 只允许删除已审核的记录
                int deletedCount = 0;
                
                // 使用迭代器进行安全删除
                for (Iterator<InformationApply> iterator = informationApplies.iterator(); iterator.hasNext();) {
                    InformationApply apply = iterator.next();
                    if (applyIds.contains(apply.getInfoApplyId()) &&
                        ("Y".equals(apply.getFinalDecision()) || "R".equals(apply.getFinalDecision()))) {
                        iterator.remove();
                        deletedCount++;
                    }
                }

                if (deletedCount > 0) {
                    logger.info("[消息清理] 成功删除 {}/{}" + " 条记录",
                        deletedCount, applyIds.size());
                    return ApiResult.success("成功删除 " + deletedCount + " 条记录");
                }

                logger.warn("[消息清理] 没有符合条件的记录被删除");
                return ApiResult.error("没有符合条件的记录被删除");
            } catch (Exception ex) {
                logger.error(ex, "[消息清理] 批量删除失败: {}", ex.getMessage());
                return ApiResult.error("批量删除失败: " + ex.getMessage());
            }
        }, executor);
    }

    private synchronized String generateInfoId() {
        // 获取当前时间并格式化为月日（4位）
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);

        // 生成随机数（4位）
        int randomNum = 1000 + random.nextInt(9000); // 范围:1000-9999

        // 组合成唯一ID
        String newId = "IN" + timestamp + randomNum;
        logger.debug("[ID生成] 新信息ID: {}", newId);
        return newId;
    }
}