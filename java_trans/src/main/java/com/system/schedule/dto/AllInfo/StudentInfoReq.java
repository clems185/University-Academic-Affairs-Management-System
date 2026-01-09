package com.system.schedule.dto.allinfo;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentInfoReq {
    private String studentName;
    private String majorId;
    private String studentSex;
    private Integer startYear;
    private Integer endYear;
    private String telephone;
    private String email;
    private BigDecimal gpa;
    private BigDecimal attemptedCredit;
    private BigDecimal earnedCredit;
    private String educationLevel;
}package com.system.schedule.dto.allinfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentInfoReq {
    private String studentName;
    private String majorId;
    private String studentSex;
    private Integer startYear;
    private Integer endYear;
    private String telephone;
    private String email;
    private BigDecimal gpa;
    private BigDecimal attemptedCredit;
    private BigDecimal earnedCredit;
    private String educationLevel;
}