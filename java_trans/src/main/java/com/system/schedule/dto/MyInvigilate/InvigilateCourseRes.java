package com.system.schedule.dto.myinvigilate;

import lombok.Getter;
import lombok.Setter;

// 监考课程响应
@Getter
@Setter
public class InvigilateCourseRes {
    private String courseId;
    private String courseName;
    private String classId;
    private int semester;
    private int year;
    private int studentCount = 50; // 固定50人
}