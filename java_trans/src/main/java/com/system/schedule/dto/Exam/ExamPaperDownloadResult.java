package com.system.schedule.dto.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamPaperDownloadResult {
    private String examId;
    private String fileContent;
    private String fileName;
}