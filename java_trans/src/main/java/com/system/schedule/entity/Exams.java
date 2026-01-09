package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class Exams {
    private String examId;
    private String courseId;
    private String timeSlotId;
    private String classroomId;
    private String semester;
    private String year;
    private BigDecimal week;
}