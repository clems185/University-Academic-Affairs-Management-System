package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.common.ResultHelper;
import com.system.schedule.service.YearSemesterStartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/yearsemesterstart")
public class YearSemesterStartController {

    @Autowired
    private YearSemesterStartService yearSemesterStartService;

    @GetMapping("/start-date")
    public ApiResult getSemesterStartDate(@RequestParam String year, @RequestParam String semester) {
        try {
            var startDate = yearSemesterStartService.getSemesterStartDate(year, semester);
            return startDate == null ? 
                ResultHelper.error("未找到对应的学期开学日期") : 
                ResultHelper.success(new SemesterStartDateResponse(startDate));
        } catch (Exception ex) {
            return ResultHelper.error("获取学期开学日期失败: " + ex.getMessage());
        }
    }

    @GetMapping("/current")
    public ApiResult getCurrentSemesterInfo() {
        try {
            var currentDate = LocalDateTime.now();
            var semesterInfo = yearSemesterStartService.getCurrentSemesterInfo(currentDate);
            if (semesterInfo == null) {
                return ResultHelper.error("未找到当前学期信息");
            }
            return ResultHelper.success(semesterInfo);
        } catch (Exception ex) {
            return ResultHelper.error("获取当前学期信息失败: " + ex.getMessage());
        }
    }

    @GetMapping("/current-week")
    public ApiResult getCurrentWeek(@RequestParam String year, @RequestParam String semester) {
        try {
            var currentDate = LocalDateTime.now();
            var currentWeek = yearSemesterStartService.calculateCurrentWeek(currentDate, year, semester);
            return ResultHelper.success(new CurrentWeekResponse(currentWeek));
        } catch (Exception ex) {
            return ResultHelper.error("计算当前周失败: " + ex.getMessage());
        }
    }

    // 内部类用于API响应
    private static class SemesterStartDateResponse {
        private LocalDateTime startDate;
        
        public SemesterStartDateResponse(LocalDateTime startDate) {
            this.startDate = startDate;
        }
        
        public LocalDateTime getStartDate() {
            return startDate;
        }
    }

    private static class CurrentWeekResponse {
        private int currentWeek;
        
        public CurrentWeekResponse(int currentWeek) {
            this.currentWeek = currentWeek;
        }
        
        public int getCurrentWeek() {
            return currentWeek;
        }
    }
}