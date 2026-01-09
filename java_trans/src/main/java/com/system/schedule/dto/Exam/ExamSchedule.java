package com.system.schedule.dto.exam;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExamScheduleSearchParams {
    private String examId;         // 考试ID
    private String courseId;       // 课程ID
    private String timeSlotId;     // 时间段ID
    private String classroomId;    // 教室ID
    private Integer semester;      // 学期（0/1）
    private Integer year;          // 学年（YYYY）
}

@Getter @Setter
public class ExamScheduleItem {
    private String examId = "";         // 考试ID
    private String courseId = "";       // 课程ID
    private String courseName = "";     // 课程名称
    private String timeSlotId = "";     // 时间段ID
    private String timeSlotDisplay = ""; // 时间段显示文本
    private String classroomId = "";    // 教室ID
    private int semester;                          // 学期（0/1）
    private int year;                              // 学年（YYYY）
    private String semesterDisplay = ""; // 学期显示文本
}