package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@SuppressWarnings({"all"})
public class UpdateClassApplyTtoa {
    private String applyId;
    private String teacherId;
    private String courseId;
    private Date applyTime;
    private String information;
    private Date reviewTime;
    private String reviewComments;
    private String state;
}