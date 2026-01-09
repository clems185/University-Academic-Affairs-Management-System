package com.system.schedule.dto.selectcourse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectCourseDto {
    private String courseId = "";
    private String classId = "0"; // 默认"0"表示未选择班级
    private String courseName = "";
    private String teacherName;
    private String location;
    private String timeSlotInfo; // 新增上课时间信息
    private String status = "N";
    private int selectedCount;
    private int capacity;

    // 组合ID字段 (CourseId + ClassId)
    public String getCourseClassId() {
        return courseId + classId;
    }

    // 完整上课信息 (时间+地点)
    public String getClassInfo() {
        return timeSlotInfo + " " + location;
    }
}