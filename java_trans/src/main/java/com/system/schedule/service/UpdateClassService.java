// UpdateClassService.java
package com.system.schedule.service;

import com.system.schedule.dto.UpdateClass.*;
import com.system.schedule.entity.UpdateClassApply;
import com.system.schedule.mapper.UpdateClassApplyMapper;
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
public class UpdateClassService {

    @Autowired
    private UpdateClassApplyMapper updateClassApplyMapper;

    /**
     * 获取授课申请列表
     */
    public ApiResult getUpdateClassListAsync(UpdateClassHandleSearchParams searchParams) {
        try {
            log.info("=== 开始查询授课申请列表 ===");
            log.info("搜索参数: ApplyId={}, TeacherId={}, CourseId={}, State={}", 
                searchParams.getApplyId(), searchParams.getTeacherId(), 
                searchParams.getCourseId(), searchParams.getState());

            // 构建查询条件
            Map<String, Object> params = new HashMap<>();
            
            if (StringUtils.hasText(searchParams.getApplyId())) {
                params.put("applyId", searchParams.getApplyId());
            }
            
            if (StringUtils.hasText(searchParams.getTeacherId())) {
                params.put("teacherId", searchParams.getTeacherId());
            }
            
            if (StringUtils.hasText(searchParams.getCourseId())) {
                params.put("courseId", searchParams.getCourseId());
            }
            
            if (StringUtils.hasText(searchParams.getState())) {
                params.put("state", searchParams.getState());
            }

            List<UpdateClassApply> updateClassApplies = updateClassApplyMapper.selectByMap(params);
            log.info("实体查询结果数量: {}", updateClassApplies.size());

            if (!updateClassApplies.isEmpty()) {
                UpdateClassApply first = updateClassApplies.get(0);
                log.info("第一条实体数据: ApplyId='{}', TeacherId='{}', CourseId='{}', State='{}'", 
                    first.getApplyId(), first.getTeacherId(), first.getCourseId(), first.getState());
            }

            // 转换为 DTO
            List<UpdateClassHandleItem> result = updateClassApplies.stream()
                .sorted(Comparator.comparing(UpdateClassApply::getApplyTime).reversed())
                .map(u -> {
                    UpdateClassHandleItem item = new UpdateClassHandleItem();
                    item.setApplyId(StringUtils.hasText(u.getApplyId()) ? u.getApplyId() : "");
                    item.setTeacherId(StringUtils.hasText(u.getTeacherId()) ? u.getTeacherId() : "");
                    item.setCourseId(StringUtils.hasText(u.getCourseId()) ? u.getCourseId() : "");
                    item.setApplyTime(u.getApplyTime());
                    item.setInformation(StringUtils.hasText(u.getInformation()) ? u.getInformation() : "");
                    item.setReviewTime(u.getReviewTime());
                    item.setReviewComments(StringUtils.hasText(u.getReviewComments()) ? u.getReviewComments() : "");
                    item.setState(StringUtils.hasText(u.getState()) ? u.getState() : "");
                    // TODO: 可以在这里添加关联查询获取教师姓名和课程名称
                    item.setTeacherName("");
                    item.setCourseName("");
                    return item;
                })
                .collect(Collectors.toList());

            log.info("DTO转换结果数量: {}", result.size());
            if (!result.isEmpty()) {
                UpdateClassHandleItem firstDto = result.get(0);
                log.info("第一条DTO数据: applyId='{}', teacherId='{}', courseId='{}', state='{}'", 
                    firstDto.getApplyId(), firstDto.getTeacherId(), 
                    firstDto.getCourseId(), firstDto.getState());
            }

            log.info("=== 查询授课申请列表完成 ===");
            return ResultHelper.success(result);
        } catch (Exception ex) {
            log.error("查询授课申请列表异常: {}", ex.getMessage(), ex);
            return ResultHelper.error("获取授课申请列表失败: " + ex.getMessage());
        }
    }

    /**
     * 批量同意授课申请
     */
    @Transactional
    public ApiResult batchApproveAsync(List<String> ids) {
        try {
            log.info("=== 开始批量同意授课申请 ===");
            log.info("待同意的申请ID数量: {}", ids != null ? ids.size() : 0);
            log.info("申请ID列表: {}", ids != null ? String.join(", ", ids) : "");

            if (CollectionUtils.isEmpty(ids)) {
                log.error("错误: 没有选择要同意的授课申请");
                return ResultHelper.error("请选择要同意的授课申请");
            }

            // 先检查这些记录是否存在
            List<UpdateClassApply> existingRecords = updateClassApplyMapper.selectBatchIds(ids);
            log.info("找到的现有记录数量: {}", existingRecords.size());
            
            for (UpdateClassApply record : existingRecords) {
                log.info("现有记录: ApplyId={}, TeacherId={}, CourseId={}, State={}", 
                    record.getApplyId(), record.getTeacherId(), record.getCourseId(), record.getState());
            }

            // 批量更新
            int result = 0;
            for (String id : ids) {
                UpdateClassApply updateApply = new UpdateClassApply();
                updateApply.setApplyId(id);
                updateApply.setState("Y");
                updateApply.setReviewTime(LocalDateTime.now());
                
                int updateResult = updateClassApplyMapper.updateById(updateApply);
                result += updateResult;
            }

            log.info("更新操作影响的行数: {}", result);

            if (result > 0) {
                log.info("=== 批量同意操作成功 ===");
                return ResultHelper.success("成功同意 " + result + " 个授课申请");
            } else {
                log.warn("警告: 更新操作没有影响任何行");
                return ResultHelper.error("没有找到可更新的授课申请记录");
            }
        } catch (Exception ex) {
            log.error("批量同意操作异常: {}", ex.getMessage(), ex);
            return ResultHelper.error("批量同意失败: " + ex.getMessage());
        }
    }

    /**
     * 批量拒绝授课申请
     */
    @Transactional
    public ApiResult batchRejectAsync(List<String> ids) {
        try {
            if (CollectionUtils.isEmpty(ids)) {
                return ResultHelper.error("请选择要拒绝的授课申请");
            }

            // 批量更新
            int result = 0;
            for (String id : ids) {
                UpdateClassApply updateApply = new UpdateClassApply();
                updateApply.setApplyId(id);
                updateApply.setState("N");
                updateApply.setReviewTime(LocalDateTime.now());
                
                int updateResult = updateClassApplyMapper.updateById(updateApply);
                result += updateResult;
            }

            return ResultHelper.success("成功拒绝 " + result + " 个授课申请");
        } catch (Exception ex) {
            return ResultHelper.error("批量拒绝失败: " + ex.getMessage());
        }
    }

    /**
     * 同意单个授课申请
     */
    @Transactional
    public ApiResult approveAsync(String id, String reviewComments, Integer reviewerId) {
        try {
            log.info("=== 开始单个同意授课申请 ===");
            log.info("申请ID: {}", id);
            log.info("审核意见: {}", reviewComments);
            log.info("审核人ID: {}", reviewerId);

            if (!StringUtils.hasText(id)) {
                log.error("错误: 申请ID为空");
                return ResultHelper.error("申请ID不能为空");
            }

            // 先检查记录是否存在
            UpdateClassApply existingRecord = updateClassApplyMapper.selectById(id);

            if (existingRecord != null) {
                log.info("找到现有记录: ApplyId={}, TeacherId={}, CourseId={}, State={}", 
                    existingRecord.getApplyId(), existingRecord.getTeacherId(), 
                    existingRecord.getCourseId(), existingRecord.getState());
            } else {
                log.warn("警告: 未找到对应的授课申请记录");
            }

            UpdateClassApply updateApply = new UpdateClassApply();
            updateApply.setApplyId(id);
            updateApply.setState("Y");
            updateApply.setReviewTime(LocalDateTime.now());
            updateApply.setReviewComments(reviewComments);
            
            int result = updateClassApplyMapper.updateById(updateApply);
            log.info("更新操作影响的行数: {}", result);

            if (result > 0) {
                log.info("=== 单个同意操作成功 ===");
                return ResultHelper.success("同意成功");
            } else {
                log.warn("警告: 更新操作没有影响任何行");
                return ResultHelper.error("授课申请不存在或已被处理");
            }
        } catch (Exception ex) {
            log.error("单个同意操作异常: {}", ex.getMessage(), ex);
            return ResultHelper.error("同意失败: " + ex.getMessage());
        }
    }

    /**
     * 拒绝单个授课申请
     */
    @Transactional
    public ApiResult rejectAsync(String id, String reviewComments, Integer reviewerId) {
        try {
            if (!StringUtils.hasText(id)) {
                return ResultHelper.error("申请ID不能为空");
            }

            UpdateClassApply updateApply = new UpdateClassApply();
            updateApply.setApplyId(id);
            updateApply.setState("N");
            updateApply.setReviewTime(LocalDateTime.now());
            updateApply.setReviewComments(reviewComments);
            
            int result = updateClassApplyMapper.updateById(updateApply);

            if (result > 0) {
                return ResultHelper.success("拒绝成功");
            } else {
                return ResultHelper.error("授课申请不存在或已被处理");
            }
        } catch (Exception ex) {
            return ResultHelper.error("拒绝失败: " + ex.getMessage());
        }
    }
}