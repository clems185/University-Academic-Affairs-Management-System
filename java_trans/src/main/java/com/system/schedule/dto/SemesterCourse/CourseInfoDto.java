package com.system.schedule.dto.semestercourse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseInfoDto {
    private String courseId;
    private String classId;
    private String courseName;
    private String courseType;
    private int credits;
    private String assessmentType;
    private String teacherName;
}