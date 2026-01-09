package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.math.BigDecimal;

@Getter
@Setter
@SuppressWarnings({"all"})
public class ExamApplyTtoa {
    private String applyId;
    private String courseId;
    private String timeSlotId;
    private String classroomId;
    private String semester;
    private String year;
    private byte[] paper;
    private Date applyDate;
    private String information;
    private Date reviewTime;
    private String reviewComments;
    private String state;
    private String paperFileName;
    private BigDecimal week;
}