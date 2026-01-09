// TeachingApplyService.java
package com.system.schedule.service;

import com.system.schedule.dto.TeachingApply.*;
import com.system.schedule.entity.UpdateClassApply;
import com.system.schedule.entity.Course;
import com.system.schedule.entity.Users;
import com.system.schedule.mapper.UpdateClassApplyMapper;
import com.system.schedule.mapper.CourseMapper;
import com.system.schedule.mapper.UsersMapper;
import com.system.schedule.util.ApiResult;
import com.system.schedule.util.ResultHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.util.CollectionUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TeachingApplyService {

    @Autowired
    private UpdateClassApplyMapper updateClassApplyMapper;
    
    @Autowired
    private CourseMapper courseMapper;
    
    @Autowired
    private UsersMapper usersMapper;

    public List<CourseInfoRes> getAvailableCourses() {
        try {
            return courseMapper.selectList(null).stream()
                .map(c -> {
                    CourseInfoRes res = new CourseInfoRes();
                    res.setCourseId(c.getCourseId());
                    res.setName(c.getName());
                    res.setIsSelect(c.getIsSelect());
                    res.setIsExam(c.getIsExam());
                    res.setInformation(c.getInformation());
                    res.setCredits(c.getCredits());
                    res.setCourseBeginWeek(c.getCourseBeginWeek());
                    res.setCourseEndWeek(c.getCourseEndWeek());
                    return res;
                })
                .sorted(Comparator.comparing(CourseInfoRes::getCourseId))
                .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new RuntimeException("获取课程列表失败: " + ex.getMessage(), ex);
        }
    }

    public List<TeachingApplyRes> getTeachingApplyList(TeachingApplySearchReq searchReq) {
        try {
            Map<String, Object> params = new HashMap<>();
            
            // 构建查询参数
            if (StringUtils.hasText(searchReq.getApplyId())) {
                params.put("applyId", searchReq.getApplyId());
            }
            
            if (StringUtils.hasText(searchReq.getCourseId())) {
                params.put("courseId", searchReq.getCourseId());
            }
            
            if (StringUtils.hasText(searchReq.getState())) {
                params.put("state", searchReq.getState());
            }
            
            // 默认过滤：只返回当前登录教师的申请
            String currentUserId = getCurrentUserId();
            if (StringUtils.hasText(currentUserId)) {
                params.put("teacherId", currentUserId);
            }
            
            // 如果传入了TeacherId，则以传入值为准（例如管理员端）
            if (StringUtils.hasText(searchReq.getTeacherId())) {
                params.put("teacherId", searchReq.getTeacherId());
            }
            
            // 查询申请列表
            List<UpdateClassApply> applyList = updateClassApplyMapper.selectByMap(params);
            
            // 转换为响应对象
            return applyList.stream()
                .sorted(Comparator.comparing(UpdateClassApply::getApplyTime).reversed())
                .map(apply -> {
                    TeachingApplyRes res = new TeachingApplyRes();
                    res.setApplyId(apply.getApplyId());
                    res.setTeacherId(apply.getTeacherId());
                    
                    // 获取教师姓名
                    Users teacher = usersMapper.selectById(apply.getTeacherId());
                    if (teacher != null) {
                        res.setTeacherName(teacher.getName());
                    }
                    
                    res.setCourseId(apply.getCourseId());
                    
                    // 获取课程名称
                    Course course = courseMapper.selectById(apply.getCourseId());
                    if (course != null) {
                        res.setCourseName(course.getName());
                    }
                    
                    res.setApplyTime(apply.getApplyTime());
                    res.setInformation(apply.getInformation());
                    res.setReviewTime(apply.getReviewTime());
                    res.setReviewComments(apply.getReviewComments());
                    res.setState(apply.getState());
                    res.setStateText("Y".equals(apply.getState()) ? "已同意" : 
                                   "N".equals(apply.getState()) ? "已拒绝" : "处理中");
                    return res;
                })
                .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new RuntimeException("获取申请列表失败: " + ex.getMessage(), ex);
        }
    }

    @Transactional
    public ApiResult submitTeachingApply(SubmitTeachingApplyReq req) {
        try {
            // 输入验证
            if (!StringUtils.hasText(req.getTeacherId()) || !StringUtils.hasText(req.getCourseId())) {
                return ResultHelper.error("教师ID和课程ID不能为空");
            }

            // 检查是否已申请该课程
            Map<String, Object> checkParams = new HashMap<>();
            checkParams.put("teacherId", req.getTeacherId());
            checkParams.put("courseId", req.getCourseId());
            checkParams.put("state", "P");
            
            List<UpdateClassApply> existingApplies = updateClassApplyMapper.selectByMap(checkParams);
            if (!existingApplies.isEmpty()) {
                return ResultHelper.error("您已申请教授该课程，请勿重复申请");
            }

            // 验证课程是否存在
            Course course = courseMapper.selectById(req.getCourseId());
            if (course == null) {
                return ResultHelper.error("课程不存在");
            }

            // 验证教师是否存在
            Users teacher = usersMapper.selectById(req.getTeacherId());
            if (teacher == null) {
                return ResultHelper.error("教师不存在");
            }

            // 生成申请ID
            String applyId = generateApplyId();

            // 创建申请记录
            UpdateClassApply apply = new UpdateClassApply();
            apply.setApplyId(applyId);
            apply.setTeacherId(req.getTeacherId());
            apply.setCourseId(req.getCourseId());
            apply.setInformation(StringUtils.hasText(req.getInformation()) ? req.getInformation() : "");
            apply.setApplyTime(LocalDateTime.now());
            apply.setState("P");

            int result = updateClassApplyMapper.insert(apply);

            if (result > 0) {
                return ResultHelper.success("申请提交成功");
            } else {
                return ResultHelper.error("申请提交失败");
            }
        } catch (Exception ex) {
            log.error("SubmitTeachingApply异常: {}", ex.getMessage(), ex);
            return ResultHelper.error("申请提交失败: " + ex.getMessage());
        }
    }

    @Transactional
    public ApiResult batchSubmitTeachingApply(BatchSubmitTeachingApplyReq req) {
        try {
            int successCount = 0;
            List<String> failedCourses = new ArrayList<>();

            for (String courseId : req.getCourseIds()) {
                SubmitTeachingApplyReq submitReq = new SubmitTeachingApplyReq();
                submitReq.setTeacherId(req.getTeacherId());
                submitReq.setCourseId(courseId);
                submitReq.setInformation(req.getInformation());

                ApiResult result = submitTeachingApply(submitReq);
                if (result.isSuccess()) {
                    successCount++;
                } else {
                    failedCourses.add(courseId);
                }
            }

            if (successCount == req.getCourseIds().size()) {
                return ResultHelper.success("成功提交 " + successCount + " 个申请");
            } else {
                return ResultHelper.success("成功提交 " + successCount + " 个申请，" + 
                    failedCourses.size() + " 个失败");
            }
        } catch (Exception ex) {
            return ResultHelper.error("批量申请失败: " + ex.getMessage());
        }
    }

    @Transactional
    public ApiResult withdrawTeachingApply(WithdrawTeachingApplyReq req) {
        try {
            UpdateClassApply apply = updateClassApplyMapper.selectById(req.getApplyId());
            
            if (apply == null) {
                return ResultHelper.error("申请不存在");
            }

            if (!"P".equals(apply.getState())) {
                return ResultHelper.error("只能撤回处理中的申请");
            }

            int result = updateClassApplyMapper.deleteById(req.getApplyId());

            if (result > 0) {
                return ResultHelper.success("撤回成功");
            } else {
                return ResultHelper.error("撤回失败");
            }
        } catch (Exception ex) {
            return ResultHelper.error("撤回失败: " + ex.getMessage());
        }
    }

    public boolean isAlreadyApplied(String teacherId, String courseId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("teacherId", teacherId);
            params.put("courseId", courseId);
            params.put("state", "P");
            
            int count = updateClassApplyMapper.selectCount(params);
            return count > 0;
        } catch (Exception ex) {
            log.error("检查是否已申请失败: {}", ex.getMessage());
            return false;
        }
    }

    private String generateApplyId() {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String date = LocalDateTime.now().format(dateFormatter);
            
            // 查找当天最大的申请ID
            List<UpdateClassApply> todayApplies = updateClassApplyMapper.selectTodayApplies(date);
            
            if (todayApplies.isEmpty()) {
                return date + "01"; // 10位：8位日期 + 2位序号
            }
            
            // 找到最大的申请ID
            String maxId = todayApplies.stream()
                .map(UpdateClassApply::getApplyId)
                .max(Comparator.naturalOrder())
                .orElse("");
            
            if (StringUtils.hasText(maxId) && maxId.length() >= 10) {
                String sequenceStr = maxId.substring(8, 10);
                try {
                    int sequence = Integer.parseInt(sequenceStr);
                    sequence++;
                    if (sequence > 99) {
                        throw new RuntimeException("当天申请数量已达上限(99)");
                    }
                    return String.format("%s%02d", date, sequence);
                } catch (NumberFormatException e) {
                    return date + "01";
                }
            }
            
            return date + "01";
        } catch (Exception ex) {
            log.error("GenerateApplyId异常: {}", ex.getMessage());
            throw new RuntimeException("生成申请ID失败: " + ex.getMessage());
        }
    }

    private String getCurrentUserId() {
        // 这里需要从 HTTP 上下文或 JWT Token 中获取当前用户ID
        // 临时返回空字符串，实际项目中需要实现
        return "";
    }
}