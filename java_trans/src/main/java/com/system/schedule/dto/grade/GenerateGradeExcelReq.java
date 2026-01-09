package com.system.schedule.dto.grade;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateGradeExcelReq {
    private String courseId;
    private String classId;
    private int semester;
    private int year;
}