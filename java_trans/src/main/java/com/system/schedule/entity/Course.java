package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class Course {
    private String courseId;
    private String name;
    private String isSelect;
    private String isExam;
    private String information;
    private BigDecimal credits;
    private BigDecimal courseBeginWeek;
    private BigDecimal courseEndWeek;
    private String firstScoreInformation;
    private BigDecimal firstScore;
    private String secondScoreInformation;
    private BigDecimal secondScore;
    private String thirdScoreInformation;
    private BigDecimal thirdScore;
    private String forthScoreInformation;
    private BigDecimal forthScore;
    private BigDecimal timesPerWeek;
}