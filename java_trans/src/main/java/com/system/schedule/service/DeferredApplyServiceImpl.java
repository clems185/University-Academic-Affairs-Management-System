package com.system.schedule.service;

import com.system.schedule.dto.deferredapply.SubmitDeferredApplyReq;
import com.system.schedule.dto.deferredapply.DeferredApplyRes;
import com.system.schedule.dto.other.ApiResult;
import com.system.schedule.entity.ExamDelayApply;
import com.system.schedule.entity.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class DeferredApplyServiceImpl implements IDeferredApplyService {
    private static final Logger logger = LoggerFactory.getLogger(DeferredApplyServiceImpl.class);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static int sequenceCounter = 1;

    // 这里需要替换为实际的数据库操作类
    // @Autowired
    // private ExamDelayApplyMapper examDelayApplyMapper;
    // @Autowired
    // private CourseMapper courseMapper;

    public DeferredApplyServiceImpl() {
        // 如果需要构造函数注入，可以在这里添加
    }

    @Override
    public CompletableFuture<ApiResult> submitDeferredApply(SubmitDeferredApplyReq req) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("SubmitDeferredApply start, StudentId={}, CourseId={}", req.getStudentId(), req.getCourseId());

                // 检查是否已经申请过该课程的缓考
                // 模拟数据库查询
                List<ExamDelayApply> mockExamDelayApplies = new ArrayList<>();
                // 实际应该是从数据库查询
                boolean hasExistingApply = mockExamDelayApplies.stream()
                        .anyMatch(a -> a.getStudentId().trim().equals(req.getStudentId())
                                && a.getCourseId().trim().equals(req.getCourseId()));

                if (hasExistingApply) {
                    logger.warn("学生{}已经申请过课程{}的缓考", req.getStudentId(), req.getCourseId());
                    return ApiResult.error("您已经申请过该课程的缓考，请勿重复申请");
                }

                // 生成申请ID
                String applyId = generateApplyId();
                logger.info("生成缓考申请ID: {}", applyId);

                // 创建缓考申请记录
                ExamDelayApply applyEntity = new ExamDelayApply();
                applyEntity.setApplyId(applyId);
                applyEntity.setStudentId(req.getStudentId());
                applyEntity.setCourseId(req.getCourseId());
                applyEntity.setInformation(req.getInformation());
                applyEntity.setApplyDate(LocalDateTime.now());
                applyEntity.setState("P"); // P-待审核

                // 模拟数据库插入
                // actual: int result = examDelayApplyMapper.insert(applyEntity);
                int result = 1; // 模拟插入成功

                if (result > 0) {
                    logger.info("缓考申请提交成功, ApplyId={}", applyId);

                    // 创建响应对象
                    // SubmitDeferredApplyRes response = new SubmitDeferredApplyRes();
                    // response.setApplyId(applyId);
                    // response.setMessage("缓考申请提交成功，请等待审核");

                    return ApiResult.success("缓考申请提交成功，请等待审核");
                } else {
                    logger.error("缓考申请提交失败, 数据库插入返回0");
                    return ApiResult.error("缓考申请提交失败");
                }
            } catch (Exception ex) {
                logger.error("SubmitDeferredApply异常: {}", ex.getMessage(), ex);
                return ApiResult.error("缓考申请提交失败: " + ex.getMessage());
            }
        });
    }

    @Override
    public CompletableFuture<List<DeferredApplyRes>> getMyDeferredApplies(String studentId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("GetMyDeferredApplies start, studentId={}", studentId);

                // 模拟数据库查询结果
                List<DeferredApplyRes> applies = new ArrayList<>();

                // 模拟数据
                DeferredApplyRes item1 = new DeferredApplyRes();
                item1.setApplyId("2024010801");
                item1.setStudentId(studentId);
                item1.setCourseId("CS101");
                item1.setCourseName("计算机导论");
                item1.setApplyDate(LocalDateTime.now().minusDays(1));
                item1.setInformation("身体不适申请缓考");
                item1.setState("P");
                item1.setStateDescription("待审核");
                applies.add(item1);

                DeferredApplyRes item2 = new DeferredApplyRes();
                item2.setApplyId("2024010701");
                item2.setStudentId(studentId);
                item2.setCourseId("CS201");
                item2.setCourseName("数据结构");
                item2.setApplyDate(LocalDateTime.now().minusDays(2));
                item2.setInformation("家中有事申请缓考");
                item2.setState("A");
                item2.setStateDescription("已通过");
                item2.setReviewTime(LocalDateTime.now().minusDays(1));
                item2.setReviewComments("同意缓考申请");
                applies.add(item2);

                logger.info("GetMyDeferredApplies end, studentId={}, count={}", studentId, applies.size());
                return applies;
            } catch (Exception ex) {
                logger.error("获取缓考申请列表失败, studentId={}", studentId, ex);
                throw new RuntimeException("获取缓考申请列表失败: " + ex.getMessage(), ex);
            }
        });
    }

    /**
     * 生成缓考申请ID
     *
     * @return 申请ID
     */
    private synchronized String generateApplyId() {
        try {
            String date = LocalDateTime.now().format(dateFormatter);
            
            // 这里应该从数据库查询当天最大的序号
            // 模拟实现：使用静态计数器
            int sequence = sequenceCounter++;
            
            if (sequence > 99) {
                throw new Exception("当天缓考申请数量已达上限(99)");
            }
            
            return String.format("%s%02d", date, sequence); // 保证2位序号
        } catch (Exception ex) {
            logger.error("GenerateApplyId异常: {}", ex.getMessage(), ex);
            throw new RuntimeException("生成申请ID失败: " + ex.getMessage(), ex);
        }
    }
}