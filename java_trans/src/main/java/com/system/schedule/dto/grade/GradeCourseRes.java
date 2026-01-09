package com.system.schedule.dto.grade;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeCourseRes {
    private String courseId;
    private String courseName;
    private String classId;
    private int semester;
    private int year;
    private int studentCount;
}