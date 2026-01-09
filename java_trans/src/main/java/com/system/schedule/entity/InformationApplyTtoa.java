package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@SuppressWarnings({"all"})
public class InformationApplyTtoa {
    private String infoApplyId;
    private String applicantId;
    private Date applyTime;
    private String types;
    private String title;
    private String content;
    private String applyComment;
    private Date reviewTime;
    private String reviewComments;
    private String finalDecision;
    private String reviewId;
}