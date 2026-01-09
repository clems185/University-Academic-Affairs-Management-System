package com.system.schedule.dto.informationapply;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InformationApplyRes {
    private String infoApplyId;
    private String applicantId;
    private LocalDateTime applyTime;
    private String types;
    private String title;
    private String content;
    private String applyComment;
    private LocalDateTime reviewTime;
    private String reviewContents;
    private String finalDecision;
    private String reviewId;
    private String statusText;
}