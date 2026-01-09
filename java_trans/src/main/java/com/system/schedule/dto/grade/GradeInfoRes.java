package com.system.schedule.dto.grade;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * 成绩信息响应DTO
 */
@Getter
@Setter
public class GradeInfoRes {
    /**
     * 课程ID
     */
    private String courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 学分
     */
    private int credits;

    /**
     * 成绩等级
     */
    private String grade;

    /**
     * 学期
     */
    private String semester;

    /**
     * 学年
     */
    private String year;

    /**
     * 班级ID
     */
    private String classId;

    /**
     * 学期GPA
     */
    private BigDecimal semesterGpa;
}