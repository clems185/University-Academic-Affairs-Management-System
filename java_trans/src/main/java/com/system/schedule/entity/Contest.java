package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.math.BigDecimal;

@Getter
@Setter
public class Contest {
    private String contestId;
    private String name;
    private Date contestTime;
    private String grade;
    private String url;
    private String information;
    private BigDecimal numbers;
    private Date createTime;
    private Date updateTime;
    private String state;
    private Date reviewTime;
    private String reviewComments;
}