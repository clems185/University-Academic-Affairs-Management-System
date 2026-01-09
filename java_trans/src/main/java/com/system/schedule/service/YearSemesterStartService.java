// YearSemesterStartService.java
package com.system.schedule.service;

import com.system.schedule.entity.YearSemesterStartTime;
import com.system.schedule.mapper.YearSemesterStartTimeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class YearSemesterStartService {

    @Autowired
    private YearSemesterStartTimeMapper yearSemesterStartTimeMapper;

    public LocalDateTime getSemesterStartDate(String year, String semester) {
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("semester", semester);
        
        YearSemesterStartTime startTime = yearSemesterStartTimeMapper.selectOne(params);
        return startTime != null ? startTime.getStartTime() : null;
    }

    public YearSemesterStartTime getCurrentSemesterInfo(LocalDateTime currentDate) {
        // 查询小于等于当前日期的最新学期信息
        return yearSemesterStartTimeMapper.selectLatestBeforeDate(currentDate);
    }

    public int calculateCurrentWeek(LocalDateTime currentDate, String year, String semester) {
        LocalDateTime startDate = getSemesterStartDate(year, semester);
        if (startDate == null) return 0;

        long daysDiff = java.time.Duration.between(startDate, currentDate).toDays();
        return (int) Math.floor(daysDiff / 7.0) + 1;
    }
}