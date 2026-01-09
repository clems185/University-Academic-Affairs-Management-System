package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class Takes {
    private String studentId;
    private String courseId;
    private String classId;
    private String semester;
    private String year;
    private String grade;
    private String state;
    private byte[] paper;
}