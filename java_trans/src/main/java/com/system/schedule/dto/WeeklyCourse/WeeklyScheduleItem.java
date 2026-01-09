
package com.system.schedule.dto.weeklycourse;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class WeeklyScheduleItem {
    private String courseId;
    private String courseName;
    private String className;
    private String teacherName;
    private String classroom;
    private int dayOfWeek;
    private String startTime;
    private String endTime;
    private String campus;
    private String building;
    private String roomNumber;
    private String period;
    private String weekPattern;
    private String year;
    private String semester;
    private int studentCount;
    // 更多课程相关字段
    private String courseType;
    private String assessmentType;
    private BigDecimal credits;
    private String courseInformation;
}