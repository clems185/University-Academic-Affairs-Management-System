package com.system.schedule.dto.invigilate;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class InvigilateDto {
    private String courseId = "";
    private int semester;
    private int year;
    private String courseName = "";
    private LocalDateTime examTime; // 考试开始的时间
    private String examLocation = "";// 考试的地点
    private int studentCount; // 参加学生人数
}package com.system.schedule.dto.invigilate;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvigilateDto {
    private String courseId;
    private int semester;
    private int year;
    private String courseName;
    private LocalDateTime examTime;
    private String examLocation;
    private int studentCount;
}