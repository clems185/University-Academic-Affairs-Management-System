package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class ExamDelayApplyStoa {
    private String applyId;
    private String studentId;
    private String courseId;
    private Date applyDate;
    private String information;
    private Date reviewTime;
    private String reviewComments;
    private String state;
}