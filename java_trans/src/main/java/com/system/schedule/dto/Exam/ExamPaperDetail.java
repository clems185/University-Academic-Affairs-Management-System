package com.system.schedule.dto.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamPaperDetail {
    private String examId;
    private String paperContentBase64;
    private String comment;
    private boolean hasPaper;
}