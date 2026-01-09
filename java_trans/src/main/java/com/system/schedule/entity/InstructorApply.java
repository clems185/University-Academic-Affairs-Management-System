package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class InstructorApply {
    private String teacherId;
    private String applyId;
    private String information;
    private Date reviewTime;
    private String reviewComments;
    private String finalDecision;
    private Date applyTime;
}