package com.system.schedule.dto.myinvigilate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Range;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 生成座位表请求
public class MyInvigilateReq {
    @NotNull(message = "课程ID不能为空")
    private String courseId;

    @NotNull(message = "班级ID不能为空")
    private String classId;

    @NotNull(message = "行数不能为空")
    @Range(min = 1, max = 20, message = "行数必须在1-20之间")
    private int rows;

    @NotNull(message = "列数不能为空")
    @Range(min = 1, max = 20, message = "列数必须在1-20之间")
    private int columns;

    @NotNull(message = "学生人数不能为空")
    @Range(min = 1, max = 100, message = "学生人数必须在1-100之间")
    private int studentCount = 50;
}