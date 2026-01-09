package com.system.schedule.dto.grade;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadGradeRes {
    private int successCount;
    private int failureCount;
    private String[] failureDetails;
    private String summary;
}