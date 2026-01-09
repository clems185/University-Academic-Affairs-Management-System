package com.system.schedule.dto.schedulecourse;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleCourseDto {
    @JsonProperty("classId")
    private String classId = "";

    @JsonProperty("courseId")
    private String courseId = "";

    @JsonProperty("courseName")
    private String courseName = "";

    @JsonProperty("teacherName")
    private String teacherName;

    @JsonProperty("location")
    private String location;

    @JsonProperty("selectedCount")
    private int selectedCount;

    @JsonProperty("capacity")
    private int capacity;

    @JsonProperty("year")
    private String year;

    @JsonProperty("semester")
    private String semester;

    // 添加TimeSlots字段
    @JsonProperty("timeSlots")
    private List<String> timeSlots;
}