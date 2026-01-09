package com.system.schedule.service.impl;

import com.system.schedule.dto.instructor.InstructorHandleSearchParams;
import com.system.schedule.dto.other.ApiResult;
import com.system.schedule.dto.other.PageResult;
import com.system.schedule.service.IInstructorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class InstructorServiceImpl implements IInstructorService {

    private static final Logger logger = LoggerFactory.getLogger(InstructorServiceImpl.class);

    @Override
    public CompletableFuture<ApiResult> getInstructorApplyListAsync(InstructorHandleSearchParams searchParams) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始获取导师申请列表，状态: {}", searchParams.getStatus());
                
                // 模拟数据
                List<InstructorApplyMock> applyList = mockInstructorApplies();
                
                // 过滤数据
                List<InstructorApplyMock> filteredApplies = applyList.stream()
                        .filter(apply -> searchParams.getStatus() == null || apply.getStatus() == searchParams.getStatus())
                        .collect(Collectors.toList());
                
                // 转换为DTO
                List<InstructorHandleItem> handleItems = filteredApplies.stream()
                        .map(this::convertToHandleItem)
                        .collect(Collectors.toList());
                
                // 分页处理
                int totalCount = handleItems.size();
                int page = Math.max(searchParams.getPage(), 1);
                int pageSize = Math.max(searchParams.getPageSize(), 10);
                int startIndex = (page - 1) * pageSize;
                int endIndex = Math.min(startIndex + pageSize, totalCount);
                
                List<InstructorHandleItem> paginatedList = startIndex < totalCount ? 
                        handleItems.subList(startIndex, endIndex) : new ArrayList<>();
                
                PageResult<InstructorHandleItem> pageResult = new PageResult<>(paginatedList, totalCount, page, pageSize);
                
                logger.info("获取导师申请列表成功，共 {} 条记录，当前第 {} 页", totalCount, page);
                return ApiResult.success(pageResult);
                
            } catch (Exception e) {
                logger.error("获取导师申请列表失败: {}", e.getMessage(), e);
                return ApiResult.error("获取导师申请列表失败，请稍后重试");
            }
        });
    }

    @Override
    public CompletableFuture<ApiResult> batchApproveAsync(List<String> ids) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始批量同意导师申请，申请ID列表: {}", ids);
                
                // 模拟数据库更新操作
                Thread.sleep(500); // 模拟处理时间
                
                logger.info("批量同意导师申请成功，共处理 {} 条记录", ids.size());
                return ApiResult.success("批量同意导师申请成功");
                
            } catch (Exception e) {
                logger.error("批量同意导师申请失败: {}", e.getMessage(), e);
                return ApiResult.error("批量同意导师申请失败，请稍后重试");
            }
        });
    }

    @Override
    public CompletableFuture<ApiResult> batchRejectAsync(List<String> ids) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始批量拒绝导师申请，申请ID列表: {}", ids);
                
                // 模拟数据库更新操作
                Thread.sleep(500); // 模拟处理时间
                
                logger.info("批量拒绝导师申请成功，共处理 {} 条记录", ids.size());
                return ApiResult.success("批量拒绝导师申请成功");
                
            } catch (Exception e) {
                logger.error("批量拒绝导师申请失败: {}", e.getMessage(), e);
                return ApiResult.error("批量拒绝导师申请失败，请稍后重试");
            }
        });
    }

    @Override
    public CompletableFuture<ApiResult> approveAsync(String id, String reviewComments, int reviewerId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始同意单个导师申请，申请ID: {}, 审核人ID: {}", id, reviewerId);
                
                // 模拟数据库更新操作
                Thread.sleep(300); // 模拟处理时间
                
                logger.info("同意导师申请成功，申请ID: {}", id);
                return ApiResult.success("同意导师申请成功");
                
            } catch (Exception e) {
                logger.error("同意导师申请失败: {}", e.getMessage(), e);
                return ApiResult.error("同意导师申请失败，请稍后重试");
            }
        });
    }

    @Override
    public CompletableFuture<ApiResult> rejectAsync(String id, String reviewComments, int reviewerId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始拒绝单个导师申请，申请ID: {}, 审核人ID: {}", id, reviewerId);
                
                // 模拟数据库更新操作
                Thread.sleep(300); // 模拟处理时间
                
                logger.info("拒绝导师申请成功，申请ID: {}", id);
                return ApiResult.success("拒绝导师申请成功");
                
            } catch (Exception e) {
                logger.error("拒绝导师申请失败: {}", e.getMessage(), e);
                return ApiResult.error("拒绝导师申请失败，请稍后重试");
            }
        });
    }
    
    // 转换为InstructorHandleItem
    private InstructorHandleItem convertToHandleItem(InstructorApplyMock apply) {
        InstructorHandleItem item = new InstructorHandleItem();
        item.setId(apply.getId());
        item.setStudentId(apply.getStudentId());
        item.setStudentName(apply.getStudentName());
        item.setMajor(apply.getMajor());
        item.setGrade(apply.getGrade());
        item.setClassId(apply.getClassId());
        item.setApplyTime(apply.getApplyTime());
        item.setStatus(apply.getStatus());
        item.setReviewComments(apply.getReviewComments());
        item.setReviewerId(apply.getReviewerId());
        item.setReviewTime(apply.getReviewTime());
        return item;
    }
    
    // 模拟数据
    private List<InstructorApplyMock> mockInstructorApplies() {
        List<InstructorApplyMock> applies = new ArrayList<>();
        
        String[] studentIds = {"S1001", "S1002", "S1003", "S1004", "S1005", "S1006", "S1007", "S1008", "S1009", "S1010"};
        String[] studentNames = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十", "郑十一", "王十二"};
        String[] majors = {"计算机科学与技术", "软件工程", "人工智能", "数据科学"};
        int[] grades = {2020, 2021, 2022};
        String[] classIds = {"Class001", "Class002", "Class003"};
        int[] statuses = {0, 1, 2}; // 0: 待审核, 1: 已同意, 2: 已拒绝
        
        Random random = new Random();
        
        for (int i = 0; i < 50; i++) {
            InstructorApplyMock apply = new InstructorApplyMock();
            apply.setId(String.valueOf(i + 1));
            apply.setStudentId(studentIds[i % studentIds.length]);
            apply.setStudentName(studentNames[i % studentNames.length]);
            apply.setMajor(majors[random.nextInt(majors.length)]);
            apply.setGrade(grades[random.nextInt(grades.length)]);
            apply.setClassId(classIds[random.nextInt(classIds.length)]);
            apply.setApplyTime(LocalDateTime.now().minusDays(random.nextInt(30)));
            apply.setStatus(statuses[random.nextInt(statuses.length)]);
            
            // 如果已审核，设置审核信息
            if (apply.getStatus() != 0) {
                apply.setReviewComments(apply.getStatus() == 1 ? "同意申请" : "拒绝申请");
                apply.setReviewerId(1001 + random.nextInt(10));
                apply.setReviewTime(apply.getApplyTime().plusDays(random.nextInt(5)));
            }
            
            applies.add(apply);
        }
        
        return applies;
    }
    
    // 模拟数据的实体类
    private static class InstructorApplyMock {
        private String id;
        private String studentId;
        private String studentName;
        private String major;
        private int grade;
        private String classId;
        private LocalDateTime applyTime;
        private int status;
        private String reviewComments;
        private Integer reviewerId;
        private LocalDateTime reviewTime;
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        public String getMajor() { return major; }
        public void setMajor(String major) { this.major = major; }
        public int getGrade() { return grade; }
        public void setGrade(int grade) { this.grade = grade; }
        public String getClassId() { return classId; }
        public void setClassId(String classId) { this.classId = classId; }
        public LocalDateTime getApplyTime() { return applyTime; }
        public void setApplyTime(LocalDateTime applyTime) { this.applyTime = applyTime; }
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        public String getReviewComments() { return reviewComments; }
        public void setReviewComments(String reviewComments) { this.reviewComments = reviewComments; }
        public Integer getReviewerId() { return reviewerId; }
        public void setReviewerId(Integer reviewerId) { this.reviewerId = reviewerId; }
        public LocalDateTime getReviewTime() { return reviewTime; }
        public void setReviewTime(LocalDateTime reviewTime) { this.reviewTime = reviewTime; }
    }
    
    // 假设的InstructorHandleItem类
    private static class InstructorHandleItem {
        private String id;
        private String studentId;
        private String studentName;
        private String major;
        private int grade;
        private String classId;
        private LocalDateTime applyTime;
        private int status;
        private String reviewComments;
        private Integer reviewerId;
        private LocalDateTime reviewTime;
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        public String getMajor() { return major; }
        public void setMajor(String major) { this.major = major; }
        public int getGrade() { return grade; }
        public void setGrade(int grade) { this.grade = grade; }
        public String getClassId() { return classId; }
        public void setClassId(String classId) { this.classId = classId; }
        public LocalDateTime getApplyTime() { return applyTime; }
        public void setApplyTime(LocalDateTime applyTime) { this.applyTime = applyTime; }
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        public String getReviewComments() { return reviewComments; }
        public void setReviewComments(String reviewComments) { this.reviewComments = reviewComments; }
        public Integer getReviewerId() { return reviewerId; }
        public void setReviewerId(Integer reviewerId) { this.reviewerId = reviewerId; }
        public LocalDateTime getReviewTime() { return reviewTime; }
        public void setReviewTime(LocalDateTime reviewTime) { this.reviewTime = reviewTime; }
    }
}