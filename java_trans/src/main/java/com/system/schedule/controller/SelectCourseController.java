package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.common.ResultHelper;
import com.system.schedule.service.SelectCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/selectcourse")
public class SelectCourseController {

    @Autowired
    private SelectCourseService selectCourseService;

    /**
     * 获取学生已选课程列表
     */
    @GetMapping("/getcourse/{studentId}/{selectionId}")
    public ApiResult studentCourses(@PathVariable String studentId, 
                                   @PathVariable(required = false) String selectionId) {
        try {
            var courses = selectCourseService.getStudentCourses(studentId, selectionId);
            return ResultHelper.success(courses);
        } catch (Exception ex) {
            return ResultHelper.error("获取已选课程失败: " + ex.getMessage());
        }
    }

    @GetMapping("/get-available-course/{studentId}/{selectionId}")
    public ApiResult availableCourses(@PathVariable String studentId, @PathVariable String selectionId) {
        try {
            var courses = selectCourseService.getAvailableCourses(studentId, selectionId);
            return ResultHelper.success(courses);
        } catch (Exception ex) {
            return ResultHelper.error("获取可选课程失败: " + ex.getMessage());
        }
    }

    @GetMapping("/get/{studentId}/{courseId}")
    public ApiResult classes(@PathVariable String courseId, @PathVariable String studentId) {
        try {
            var classes = selectCourseService.getClassesByCourse(courseId, studentId);
            return ResultHelper.success(classes);
        } catch (Exception ex) {
            return ResultHelper.error("获取班级信息失败: " + ex.getMessage());
        }
    }

    @PostMapping("/add-course/{studentId}/{courseId}/{selectionId}")
    public ApiResult addCourse(@PathVariable String studentId, 
                              @PathVariable String courseId, 
                              @PathVariable String selectionId) {
        try {
            boolean result = selectCourseService.addCourse(studentId, courseId, selectionId);
            return result ? 
                ResultHelper.success("课程已添加到列表") : 
                ResultHelper.error("添加课程失败");
        } catch (Exception ex) {
            return ResultHelper.error("添加课程失败: " + ex.getMessage());
        }
    }

    @PostMapping("/select-class/{studentId}/{courseId}/{classId}")
    public ApiResult selectClass(@PathVariable String studentId, 
                                @PathVariable String courseId, 
                                @PathVariable String classId) {
        try {
            boolean result = selectCourseService.selectClass(studentId, courseId, classId);
            return result ? 
                ResultHelper.success("班级选择成功") : 
                ResultHelper.error("选择班级失败");
        } catch (Exception ex) {
            return ResultHelper.error("选择班级失败: " + ex.getMessage());
        }
    }

    @DeleteMapping("/del/{studentId}/{courseId}")
    public ApiResult remove(@PathVariable String studentId, @PathVariable String courseId) {
        try {
            boolean result = selectCourseService.removeCourse(studentId, courseId);
            return result ? 
                ResultHelper.success("课程已移除") : 
                ResultHelper.error("移除课程失败");
        } catch (Exception ex) {
            return ResultHelper.error("移除课程失败: " + ex.getMessage());
        }
    }

    @PostMapping("/save/{studentId}")
    public ApiResult save(@PathVariable String studentId) {
        try {
            boolean result = selectCourseService.saveCourses(studentId);
            return result ? 
                ResultHelper.success("课表保存成功") : 
                ResultHelper.error("没有可保存的预选课程");
        } catch (Exception ex) {
            return ResultHelper.error("保存课表失败: " + ex.getMessage());
        }
    }
}