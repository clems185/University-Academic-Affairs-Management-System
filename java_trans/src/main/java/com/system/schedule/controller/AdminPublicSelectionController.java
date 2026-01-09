package com.system.schedule.controller;

import com.system.schedule.dto.admin.*;
import com.system.schedule.service.IAdminPublicSelectionService;
import com.system.schedule.common.ApiResult;
import com.system.schedule.common.ResultHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/selection")
public class AdminPublicSelectionController {

    private final IAdminPublicSelectionService adminPublicSelectionService;

    @Autowired
    public AdminPublicSelectionController(IAdminPublicSelectionService adminPublicSelectionService) {
        this.adminPublicSelectionService = adminPublicSelectionService;
    }

    /**
     * 获取所有选课时间段
     */
    @GetMapping("/all")
    public ApiResult getAllSelections() {
        try {
            var selections = adminPublicSelectionService.getAllSelections();
            return ResultHelper.success(selections);
        } catch (Exception ex) {
            return ResultHelper.error("获取选课时间段失败: " + ex.getMessage());
        }
    }

    /**
     * 获取选课时间段详情
     */
    @GetMapping("/{selectionId}")
    public ApiResult getSelectionById(@PathVariable String selectionId) {
        try {
            var selection = adminPublicSelectionService.getSelectionById(selectionId);
            return ResultHelper.success(selection);
        } catch (Exception ex) {
            return ResultHelper.error("获取选课时间段详情失败: " + ex.getMessage());
        }
    }

    /**
     * 创建选课时间段
     */
    @PostMapping("/create")
    public ApiResult createSelection(@RequestBody CreateSelectionDto dto) {
        try {
            // 移除时间重叠验证，允许时间段重叠
            if (dto.getBeginTime().compareTo(dto.getEndTime()) >= 0) {
                return ResultHelper.error("开始时间必须早于结束时间");
            }

            var result = adminPublicSelectionService.createSelection(dto);
            return result ? ResultHelper.success("选课时间段创建成功") : ResultHelper.error("选课时间段创建失败");
        } catch (Exception ex) {
            return ResultHelper.error("创建选课时间段失败: " + ex.getMessage());
        }
    }

    /**
     * 更新选课时间段
     */
    @PutMapping("/update")
    public ApiResult updateSelection(@RequestBody UpdateSelectionDto dto) {
        try {
            // 只验证开始时间早于结束时间
            if (dto.getBeginTime() != null && dto.getEndTime() != null && 
                dto.getBeginTime().compareTo(dto.getEndTime()) >= 0) {
                return ResultHelper.error("开始时间必须早于结束时间");
            }

            var result = adminPublicSelectionService.updateSelection(dto);
            return result ? ResultHelper.success("选课时间段更新成功") : ResultHelper.error("选课时间段更新失败");
        } catch (Exception ex) {
            return ResultHelper.error("更新选课时间段失败: " + ex.getMessage());
        }
    }

    /**
     * 删除选课时间段
     */
    @DeleteMapping("/delete/{selectionId}")
    public ApiResult deleteSelection(@PathVariable String selectionId) {
        try {
            var result = adminPublicSelectionService.deleteSelection(selectionId);
            return result ? ResultHelper.success("选课时间段删除成功") : ResultHelper.error("选课时间段删除失败");
        } catch (Exception ex) {
            return ResultHelper.error("删除选课时间段失败: " + ex.getMessage());
        }
    }

    /**
     * 为选课时间段添加课程
     */
    @PostMapping("/{selectionId}/courses/add")
    public ApiResult addCoursesToSelection(@PathVariable String selectionId, @RequestBody List<String> courseIds) {
        try {
            var result = adminPublicSelectionService.addCoursesToSelection(selectionId, courseIds);
            return result ? ResultHelper.success("课程添加成功") : ResultHelper.error("课程添加失败");
        } catch (Exception ex) {
            return ResultHelper.error("添加课程失败: " + ex.getMessage());
        }
    }

    /**
     * 从选课时间段移除课程
     */
    @PostMapping("/{selectionId}/courses/remove")
    public ApiResult removeCoursesFromSelection(@PathVariable String selectionId, @RequestBody List<String> courseIds) {
        try {
            var result = adminPublicSelectionService.removeCoursesFromSelection(selectionId, courseIds);
            return result ? ResultHelper.success("课程移除成功") : ResultHelper.error("课程移除失败");
        } catch (Exception ex) {
            return ResultHelper.error("移除课程失败: " + ex.getMessage());
        }
    }

    /**
     * 为选课时间段添加专业
     */
    @PostMapping("/{selectionId}/majors/add")
    public ApiResult addMajorsToSelection(@PathVariable String selectionId, @RequestBody List<String> majorIds) {
        try {
            var result = adminPublicSelectionService.addMajorsToSelection(selectionId, majorIds);
            return result ? ResultHelper.success("专业添加成功") : ResultHelper.error("专业添加失败");
        } catch (Exception ex) {
            return ResultHelper.error("添加专业失败: " + ex.getMessage());
        }
    }

    /**
     * 从选课时间段移除专业
     */
    @PostMapping("/{selectionId}/majors/remove")
    public ApiResult removeMajorsFromSelection(@PathVariable String selectionId, @RequestBody List<String> majorIds) {
        try {
            var result = adminPublicSelectionService.removeMajorsFromSelection(selectionId, majorIds);
            return result ? ResultHelper.success("专业移除成功") : ResultHelper.error("专业移除失败");
        } catch (Exception ex) {
            return ResultHelper.error("移除专业失败: " + ex.getMessage());
        }
    }

    /**
     * 验证选课时间段时间是否有效
     */
    @PostMapping("/validate-time")
    public ApiResult validateSelectionTime(@RequestBody ValidateSelectionTimeDto dto) {
        try {
            // 只验证开始时间早于结束时间
            var isValid = dto.getBeginTime().compareTo(dto.getEndTime()) < 0;
            return ResultHelper.success(new ValidationResult(isValid));
        } catch (Exception ex) {
            return ResultHelper.error("验证选课时间失败: " + ex.getMessage());
        }
    }

    /**
     * 处理选课结果
     */
    @PostMapping("/process")
    public ApiResult processSelectionResult(@RequestBody ProcessSelectionResultDto dto) {
        try {
            var result = adminPublicSelectionService.processSelectionResult(dto.getSelectionId(), dto.isForceProcess());
            return result ? ResultHelper.success("选课结果处理成功") : ResultHelper.error("选课结果处理失败");
        } catch (Exception ex) {
            return ResultHelper.error("处理选课结果失败: " + ex.getMessage());
        }
    }

    /**
     * 获取所有课程
     */
    @GetMapping("/courses/all")
    public ApiResult getAllCourses() {
        try {
            var courses = adminPublicSelectionService.getAllCourses();
            return ResultHelper.success(courses);
        } catch (Exception ex) {
            return ResultHelper.error("获取课程失败: " + ex.getMessage());
        }
    }

    /**
     * 获取所有专业
     */
    @GetMapping("/majors/all")
    public ApiResult getAllMajors() {
        try {
            var majors = adminPublicSelectionService.getAllMajors();
            return ResultHelper.success(majors);
        } catch (Exception ex) {
            return ResultHelper.error("获取专业失败: " + ex.getMessage());
        }
    }

    /**
     * 获取所有课程ID
     */
    @GetMapping("/courses/all-ids")
    public ApiResult getAllCourseIds() {
        try {
            var courseIds = adminPublicSelectionService.getAllCourseIds();
            return ResultHelper.success(courseIds);
        } catch (Exception ex) {
            return ResultHelper.error("获取课程ID失败: " + ex.getMessage());
        }
    }

    /**
     * 获取所有专业ID
     */
    @GetMapping("/majors/all-ids")
    public ApiResult getAllMajorIds() {
        try {
            var majorIds = adminPublicSelectionService.getAllMajorIds();
            return ResultHelper.success(majorIds);
        } catch (Exception ex) {
            return ResultHelper.error("获取专业ID失败: " + ex.getMessage());
        }
    }

    // 内部DTO类
    public static class ValidationResult {
        private boolean isValid;
        
        public ValidationResult(boolean isValid) {
            this.isValid = isValid;
        }
        
        public boolean isValid() {
            return isValid;
        }
        
        public void setValid(boolean valid) {
            isValid = valid;
        }
    }
}