package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class Student {
    private String id;
    private String name;
    private String majorId;
    private BigDecimal startYear;
    private BigDecimal endYear;
    private String telephone;
    private String email;
    private String information;
    private BigDecimal gpa;
    private BigDecimal attemptedCredit;
    private BigDecimal earnedCredit;
    private String sex;
}