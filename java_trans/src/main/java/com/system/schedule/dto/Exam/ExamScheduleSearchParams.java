package com.system.schedule.dto.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamScheduleSearchParams {
    private String examId;
    private String courseId;
    private String timeSlotId;
    private String classroomId;
    private Integer semester;
    private Integer year;
}