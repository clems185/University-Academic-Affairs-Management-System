package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamPapers {
    private String examId;
    private byte[] paper;
    private String name;
}