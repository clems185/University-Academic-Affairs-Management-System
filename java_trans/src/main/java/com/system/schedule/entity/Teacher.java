package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class Teacher {
    private String teacherId;
    private String teacherName;
    private String majorId;
    private BigDecimal startYear;
    private BigDecimal workYear;
    private String telephone;
    private String email;
    private String information;
    private String sex;
    private String level;
}