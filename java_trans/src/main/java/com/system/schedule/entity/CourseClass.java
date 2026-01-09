package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter

public class CourseClass {
    private String courseId;
    private String classId;
    private String semester;
    private String year;
    private String name;
    private String information;
    private BigDecimal capacity;
}