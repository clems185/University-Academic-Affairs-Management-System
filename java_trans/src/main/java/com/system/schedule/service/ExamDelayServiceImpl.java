package com.system.schedule.service.impl;

import com.system.schedule.dto.examdelay.ExamDelayHandleItem;
import com.system.schedule.dto.examdelay.ExamDelayHandleSearchParams;
import com.system.schedule.dto.other.ApiResult;
import com.system.schedule.service.IExamDelayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ExamDelayServiceImpl implements IExamDelayService {

    private static final Logger logger = LoggerFactory.getLogger(ExamDelayServiceImpl.class);

    @Override
    public CompletableFuture<ApiResult> getExamDelayListAsync(ExamDelayHandleSearchParams searchParams) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("=== 开始查询缓考申请列表 ===");
                logger.info("搜索参数: ApplyId={}, StudentId={}, CourseId={}, State={}",
                        searchParams.getApplyId(), searchParams.getStudentId(),
                        searchParams.getCourseId(), searchParams.getState());

                // 模拟数据库查询
                List<ExamDelayApplyMock> examDelayApplies = mockExamDelayApplies();

                // 构建查询条件
                if (searchParams.getApplyId() != null && !searchParams.getApplyId().isEmpty()) {
                    examDelayApplies = examDelayApplies.stream()
                            .filter(e -> e.getApplyId().equals(searchParams.getApplyId()))
                            .collect(Collectors.toList());
                }

                if (searchParams.getStudentId() != null && !searchParams.getStudentId().isEmpty()) {
                    examDelayApplies = examDelayApplies.stream()
                            .filter(e -> e.getStudentId().contains(searchParams.getStudentId()))
                            .collect(Collectors.toList());
                }

                if (searchParams.getCourseId() != null && !searchParams.getCourseId().isEmpty()) {
                    examDelayApplies = examDelayApplies.stream()
                            .filter(e -> e.getCourseId().contains(searchParams.getCourseId()))
                            .collect(Collectors.toList());
                }

                if (searchParams.getState() != null && !searchParams.getState().isEmpty()) {
                    examDelayApplies = examDelayApplies.stream()
                            .filter(e -> e.getState().equals(searchParams.getState()))
                            .collect(Collectors.toList());
                }

                logger.info("查询到 {} 条记录", examDelayApplies.size());

                // 转换为 DTO
                List<ExamDelayHandleItem> result = examDelayApplies.stream()
                        .map(this::toExamDelayHandleItem)
                        .collect(Collectors.toList());

                logger.info("=== 查询缓考申请列表完成 ===");
                return ApiResult.success(result);
            } catch (Exception ex) {
                logger.error("查询缓考申请列表异常: {}", ex.getMessage());
                return ApiResult.error("获取缓考申请列表失败: " + ex.getMessage());
            }
        });
    }

    @Override
    public CompletableFuture<ApiResult> batchApproveAsync(List<String> ids) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("=== 开始批量同意缓考申请 ===");
                logger.info("待同意的申请ID数量: {}", ids != null ? ids.size() : 0);

                if (ids == null || ids.isEmpty()) {
                    return ApiResult.error("请选择要同意的申请");
                }

                // 模拟数据库更新操作
                int result = ids.size(); // 模拟影响行数

                logger.info("批量同意操作影响的行数: {}", result);
                return ApiResult.success("成功同意 " + result + " 个缓考申请");
            } catch (Exception ex) {
                logger.error("批量同意操作异常: {}", ex.getMessage());
                return ApiResult.error("批量同意失败: " + ex.getMessage());
            }
        });
    }

    @Override
    public CompletableFuture<ApiResult> batchRejectAsync(List<String> ids) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("=== 开始批量拒绝缓考申请 ===");
                logger.info("待拒绝的申请ID数量: {}", ids != null ? ids.size() : 0);

                if (ids == null || ids.isEmpty()) {
                    return ApiResult.error("请选择要拒绝的申请");
                }

                // 模拟数据库更新操作
                int result = ids.size(); // 模拟影响行数

                logger.info("批量拒绝操作影响的行数: {}", result);
                return ApiResult.success("成功拒绝 " + result + " 个缓考申请");
            } catch (Exception ex) {
                logger.error("批量拒绝操作异常: {}", ex.getMessage());
                return ApiResult.error("批量拒绝失败: " + ex.getMessage());
            }
        });
    }

    @Override
    public CompletableFuture<ApiResult> approveAsync(String id, String reviewComments, int reviewerId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("=== 开始单个同意缓考申请 ===");
                logger.info("申请ID: {}, 审核意见: {}", id, reviewComments);

                if (id == null || id.isEmpty()) {
                    return ApiResult.error("申请ID不能为空");
                }

                // 模拟数据库更新操作
                int result = 1; // 模拟影响行数

                if (result > 0) {
                    logger.info("=== 单个同意操作成功 ===");
                    return ApiResult.success("同意成功");
                } else {
                    return ApiResult.error("申请不存在或已被处理");
                }
            } catch (Exception ex) {
                logger.error("单个同意操作异常: {}", ex.getMessage());
                return ApiResult.error("同意失败: " + ex.getMessage());
            }
        });
    }

    @Override
    public CompletableFuture<ApiResult> rejectAsync(String id, String reviewComments, int reviewerId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("=== 开始单个拒绝缓考申请 ===");
                logger.info("申请ID: {}, 审核意见: {}", id, reviewComments);

                if (id == null || id.isEmpty()) {
                    return ApiResult.error("申请ID不能为空");
                }

                // 模拟数据库更新操作
                int result = 1; // 模拟影响行数

                if (result > 0) {
                    logger.info("=== 单个拒绝操作成功 ===");
                    return ApiResult.success("拒绝成功");
                } else {
                    return ApiResult.error("申请不存在或已被处理");
                }
            } catch (Exception ex) {
                logger.error("单个拒绝操作异常: {}", ex.getMessage());
                return ApiResult.error("拒绝失败: " + ex.getMessage());
            }
        });
    }

    // 模拟数据类
    private static class ExamDelayApplyMock {
        private String applyId;
        private String studentId;
        private String courseId;
        private Date applyDate;
        private String information;
        private Date reviewTime;
        private String reviewComments;
        private String state;

        // 构造函数、getter和setter
        public ExamDelayApplyMock(String applyId, String studentId, String courseId, Date applyDate,
                                  String information, Date reviewTime, String reviewComments, String state) {
            this.applyId = applyId;
            this.studentId = studentId;
            this.courseId = courseId;
            this.applyDate = applyDate;
            this.information = information;
            this.reviewTime = reviewTime;
            this.reviewComments = reviewComments;
            this.state = state;
        }

        public String getApplyId() {
            return applyId;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getCourseId() {
            return courseId;
        }

        public String getState() {
            return state;
        }
    }

    // 模拟数据
    private List<ExamDelayApplyMock> mockExamDelayApplies() {
        List<ExamDelayApplyMock> list = new ArrayList<>();
        Date now = new Date();

        list.add(new ExamDelayApplyMock("1001", "20220101", "CS101", now, "生病申请缓考", null, "", "N"));
        list.add(new ExamDelayApplyMock("1002", "20220102", "CS102", now, "有事申请缓考", null, "", "N"));
        list.add(new ExamDelayApplyMock("1003", "20220103", "CS103", new Date(now.getTime() - 86400000), "实习冲突", now, "同意缓考", "Y"));

        return list;
    }

    // 转换为DTO
    private ExamDelayHandleItem toExamDelayHandleItem(ExamDelayApplyMock apply) {
        ExamDelayHandleItem item = new ExamDelayHandleItem();
        item.setApplyId(apply.getApplyId());
        item.setStudentId(apply.getStudentId());
        item.setCourseId(apply.getCourseId());
        item.setApplyDate(apply.getApplyDate());
        item.setInformation(apply.getInformation());
        item.setReviewTime(apply.getReviewTime());
        item.setReviewComments(apply.getReviewComments());
        item.setState(apply.getState());
        return item;
    }
}