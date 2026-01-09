package com.system.schedule.dto.teachingapply;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/// <summary>
/// 教师授课申请响应
/// </summary>
public class TeachingApplyRes {
    private String applyId;
    private String teacherId;
    private String teacherName;
    private String courseId;
    private String courseName;
    private LocalDateTime applyTime;
    private String information;
    private LocalDateTime reviewTime;
    private String reviewComments;
    private String state;
    private String stateText;
}

@Getter
@Setter
/// <summary>
/// 课程信息响应
/// </summary>
class CourseInfoRes {
    private String courseId;
    private String name;
    private String isSelect;
    private String isExam;
    private String information;
    private int credits;
    private int courseBeginWeek;
    private int courseEndWeek;
}