package com.system.schedule.dto.allinfo;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentInfoRes {
    private String studentId;
    private String studentName;
    private String studentSex;
    private String majorId;
    private String majorName;
    private Integer startYear;
    private Integer endYear;
    private String telephone;
    private String email;
    private BigDecimal gpa;
    private BigDecimal attemptedCredit;
    private BigDecimal earnedCredit;
    private String educationLevel;
    private boolean isGraduated;
}