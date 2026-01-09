package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.common.ResultHelper;
import com.system.schedule.dto.WeeklyCourseDto.WeeklyScheduleSearchParams;
import com.system.schedule.service.WeeklyCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weeklycourse")
public class WeeklyCourseController {

    @Autowired
    private WeeklyCourseService weeklyCourseService;

    /**
     * 获取周课表（通用）
     */
    @GetMapping("/schedule")
    public ApiResult getWeeklySchedule(@ModelAttribute WeeklyScheduleSearchParams searchParams) {
        try {
            return weeklyCourseService.getWeeklyScheduleAsync(searchParams);
        } catch (Exception ex) {
            return ResultHelper.error("获取周课表失败: " + ex.getMessage());
        }
    }

    /**
     * 获取学生课表
     */
    @GetMapping("/student")
    public ApiResult getStudentSchedule(@ModelAttribute WeeklyScheduleSearchParams searchParams) {
        try {
            searchParams.setUserType("3");
            return weeklyCourseService.getStudentScheduleAsync(searchParams);
        } catch (Exception ex) {
            return ResultHelper.error("获取学生课表失败: " + ex.getMessage());
        }
    }

    /**
     * 获取教师课表
     */
    @GetMapping("/teacher")
    public ApiResult getTeacherSchedule(@ModelAttribute WeeklyScheduleSearchParams searchParams) {
        try {
            searchParams.setUserType("2");
            return weeklyCourseService.getTeacherScheduleAsync(searchParams);
        } catch (Exception ex) {
            return ResultHelper.error("获取教师课表失败: " + ex.getMessage());
        }
    }

    /**
     * 获取管理员课表
     */
    @GetMapping("/admin")
    public ApiResult getAdminWeeklySchedule(@ModelAttribute WeeklyScheduleSearchParams searchParams) {
        try {
            searchParams.setUserType("0");
            return weeklyCourseService.getAdminWeeklyScheduleAsync(searchParams);
        } catch (Exception ex) {
            return ResultHelper.error("获取管理员课表失败: " + ex.getMessage());
        }
    }
}