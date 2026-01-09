package com.system.schedule.dto.updateclass;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 授课处理列表项
public class UpdateClassHandleItem {
    private String applyId = "";
    private String teacherId = "";
    private String courseId = "";
    private LocalDateTime applyTime;
    private String information = "";
    private LocalDateTime reviewTime;
    private String reviewComments = "";
    private String state = "";
    // 扩展信息 - 可以包含教师姓名、课程名称等关联信息
    private String teacherName = "";
    private String courseName = "";
}