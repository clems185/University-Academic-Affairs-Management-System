package com.system.schedule.dto.deferredapply;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeferredApplyRes {
    private String applyId;
    private String studentId;
    private String courseId;
    private String courseName;
    private LocalDateTime applyDate;
    private String information;
    private LocalDateTime reviewTime;
    private String reviewComments;
    private String state;
    private String stateDescription;
}