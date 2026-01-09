// TeachingHandleService.java
package com.system.schedule.service;

import com.system.schedule.dto.TeachingApply.*;
import com.system.schedule.dto.TeachingHandle.*;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TeachingHandleService {

    @Autowired
    private UpdateClassApplyMapper updateClassApplyMapper;
    
    @Autowired
    private CourseMapper courseMapper;
    
    @Autowired
    private UsersMapper usersMapper;

    public List<TeachingApplyRes> getPendingApplyList(TeachingHandleSearchReq searchReq) {
        try {
            Map<String, Object> params = new HashMap<>();
            
            // 构建查询参数
            if (StringUtils.hasText(searchReq.getApplyId())) {
                params.put("applyId", searchReq.getApplyId());
            }
            
            if (StringUtils.hasText(searchReq.getTeacherId())) {
                params.put("teacherId", searchReq.getTeacherId());
            }
            
            if (StringUtils.hasText(searchReq.getCourseId())) {
                params.put("courseId", searchReq.getCourseId());
            }
            
            if (StringUtils.hasText(searchReq.getState())) {
                params.put("state", searchReq.getState());
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
    public ApiResult approveTeachingApply(ApproveTeachingApplyReq req) {
        try {
            UpdateClassApply apply = updateClassApplyMapper.selectById(req.getApplyId());
            
            if (apply == null) {
                return ResultHelper.error("申请不存在");
            }

            if (!"P".equals(apply.getState())) {
                return ResultHelper.error("该申请已被处理");
            }

            UpdateClassApply updateApply = new UpdateClassApply();
            updateApply.setApplyId(req.getApplyId());
            updateApply.setState("Y");
            updateApply.setReviewTime(LocalDateTime.now());
            updateApply.setReviewComments(req.getReviewComments());
            
            int result = updateClassApplyMapper.updateById(updateApply);

            if (result > 0) {
                return ResultHelper.success("审核通过");
            } else {
                return ResultHelper.error("审核失败");
            }
        } catch (Exception ex) {
            return ResultHelper.error("审核失败: " + ex.getMessage());
        }
    }

    @Transactional
    public ApiResult rejectTeachingApply(RejectTeachingApplyReq req) {
        try {
            UpdateClassApply apply = updateClassApplyMapper.selectById(req.getApplyId());
            
            if (apply == null) {
                return ResultHelper.error("申请不存在");
            }

            if (!"P".equals(apply.getState())) {
                return ResultHelper.error("该申请已被处理");
            }

            UpdateClassApply updateApply = new UpdateClassApply();
            updateApply.setApplyId(req.getApplyId());
            updateApply.setState("N");
            updateApply.setReviewTime(LocalDateTime.now());
            updateApply.setReviewComments(req.getReviewComments());
            
            int result = updateClassApplyMapper.updateById(updateApply);

            if (result > 0) {
                return ResultHelper.success("申请已拒绝");
            } else {
                return ResultHelper.error("处理失败");
            }
        } catch (Exception ex) {
            return ResultHelper.error("处理失败: " + ex.getMessage());
        }
    }

    @Transactional
    public ApiResult batchApproveTeachingApply(BatchProcessTeachingApplyReq req) {
        try {
            int successCount = 0;
            int failedCount = 0;

            for (String applyId : req.getApplyIds()) {
                ApproveTeachingApplyReq approveReq = new ApproveTeachingApplyReq();
                approveReq.setApplyId(applyId);
                approveReq.setReviewComments(req.getReviewComments());

                ApiResult result = approveTeachingApply(approveReq);
                if (result.isSuccess()) {
                    successCount++;
                } else {
                    failedCount++;
                }
            }

            return ResultHelper.success("批量处理完成：成功 " + successCount + " 个，失败 " + failedCount + " 个");
        } catch (Exception ex) {
            return ResultHelper.error("批量处理失败: " + ex.getMessage());
        }
    }

    @Transactional
    public ApiResult batchRejectTeachingApply(BatchProcessTeachingApplyReq req) {
        try {
            int successCount = 0;
            int failedCount = 0;

            for (String applyId : req.getApplyIds()) {
                RejectTeachingApplyReq rejectReq = new RejectTeachingApplyReq();
                rejectReq.setApplyId(applyId);
                rejectReq.setReviewComments(req.getReviewComments());

                ApiResult result = rejectTeachingApply(rejectReq);
                if (result.isSuccess()) {
                    successCount++;
                } else {
                    failedCount++;
                }
            }

            return ResultHelper.success("批量处理完成：成功 " + successCount + " 个，失败 " + failedCount + " 个");
        } catch (Exception ex) {
            return ResultHelper.error("批量处理失败: " + ex.getMessage());
        }
    }

    @Transactional
    public ApiResult deleteTeachingApply(DeleteTeachingApplyReq req) {
        try {
            int result = updateClassApplyMapper.deleteById(req.getApplyId());

            if (result > 0) {
                return ResultHelper.success("删除成功");
            } else {
                return ResultHelper.error("删除失败，记录不存在");
            }
        } catch (Exception ex) {
            return ResultHelper.error("删除失败: " + ex.getMessage());
        }
    }

    @Transactional
    public ApiResult batchDeleteTeachingApply(BatchDeleteTeachingApplyReq req) {
        try {
            int result = updateClassApplyMapper.deleteBatchIds(req.getApplyIds());
            return ResultHelper.success("成功删除 " + result + " 条记录");
        } catch (Exception ex) {
            return ResultHelper.error("批量删除失败: " + ex.getMessage());
        }
    }
}