package com.system.schedule.dto.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamScheduleItem {
    private String examId;
    private String courseId;
    private String courseName;
    private String timeSlotId;
    private String timeSlotDisplay;
    private String classroomId;
    private int semester;
    private int year;
    private String semesterDisplay;
}