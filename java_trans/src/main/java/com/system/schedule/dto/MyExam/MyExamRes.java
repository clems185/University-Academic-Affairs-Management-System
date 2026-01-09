package com.system.schedule.dto.myexam;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyExamRes {
    private String examId;
    private String courseId;
    private String courseName;
    private String timeSlotId;
    private String classroomId;
    private int semester;
    private int year;
    private String examDate; // 考试日期
    private String examTime; // 考试时间
}package com.system.schedule.dto.myexam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyExamRes {
    private String examId;
    private String courseId;
    private String courseName;
    private String timeSlotId;
    private String classroomId;
    private int semester;
    private int year;
    private String examDate; // 考试日期
    private String examTime; // 考试时间
}