package com.system.schedule.dto.myteaching;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/// <summary>
/// 我的授课响应类型
/// </summary>
public class MyTeachingRes {
    /// <summary>
    /// 课程ID
    /// </summary>
    private String courseId = "";

    /// <summary>
    /// 课程名称
    /// </summary>
    private String courseName = "";

    /// <summary>
    /// 班级名称
    /// </summary>
    private String className = "";

    /// <summary>
    /// 教室
    /// </summary>
    private String classroom = "";

    /// <summary>
    /// 星期几
    /// </summary>
    private int dayOfWeek;

    /// <summary>
    /// 开始时间
    /// </summary>
    private String startTime = "";

    /// <summary>
    /// 结束时间
    /// </summary>
    private String endTime = "";

    /// <summary>
    /// 学年
    /// </summary>
    private String year = "";

    /// <summary>
    /// 学期
    /// </summary>
    private String semester = "";

    /// <summary>
    /// 周数
    /// </summary>
    private int week;

    /// <summary>
    /// 节次
    /// </summary>
    private int period;

    /// <summary>
    /// 学生ID
    /// </summary>
    private String studentId = "";

    /// <summary>
    /// 学生姓名
    /// </summary>
    private String studentName = "";

    /// <summary>
    /// 学生性别
    /// </summary>
    private String studentSex = "";

    /// <summary>
    /// 学生GPA
    /// </summary>
    private BigDecimal studentGPA;

    /// <summary>
    /// 学生已修学分
    /// </summary>
    private BigDecimal studentCredits;

    /// <summary>
    /// 学生专业ID
    /// </summary>
    private String studentMajorId = "";

    /// <summary>
    /// 学生专业名称
    /// </summary>
    private String studentMajorName = "";

    /// <summary>
    /// 学生入学年份
    /// </summary>
    private int studentStartYear;

    /// <summary>
    /// 课程类型（必修/选修）
    /// </summary>
    private String courseType = "";

    /// <summary>
    /// 考核方式（考试/考查）
    /// </summary>
    private String assessmentType = "";

    /// <summary>
    /// 学分
    /// </summary>
    private BigDecimal credits;

    /// <summary>
    /// 起始周
    /// </summary>
    private int startWeek;

    /// <summary>
    /// 结束周
    /// </summary>
    private int endWeek;
}
package com.system.schedule.dto.myteaching;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * 我的授课响应类型
 */
@Getter
@Setter
public class MyTeachingRes {
    /**
     * 课程ID
     */
    private String courseId = "";

    /**
     * 课程名称
     */
    private String courseName = "";

    /**
     * 班级名称
     */
    private String className = "";

    /**
     * 教室
     */
    private String classroom = "";

    /**
     * 星期几
     */
    private int dayOfWeek;

    /**
     * 开始时间
     */
    private String startTime = "";

    /**
     * 结束时间
     */
    private String endTime = "";

    /**
     * 学年
     */
    private String year = "";

    /**
     * 学期
     */
    private String semester = "";

    /**
     * 周数
     */
    private int week;

    /**
     * 节次
     */
    private int period;

    /**
     * 学生ID
     */
    private String studentId = "";

    /**
     * 学生姓名
     */
    private String studentName = "";

    /**
     * 学生性别
     */
    private String studentSex = "";

    /**
     * 学生GPA
     */
    private BigDecimal studentGPA;

    /**
     * 学生已修学分
     */
    private BigDecimal studentCredits;

    /**
     * 学生专业ID
     */
    private String studentMajorId = "";

    /**
     * 学生专业名称
     */
    private String studentMajorName = "";

    /**
     * 学生入学年份
     */
    private int studentStartYear;

    /**
     * 课程类型（必修/选修）
     */
    private String courseType = "";

    /**
     * 考核方式（考试/考查）
     */
    private String assessmentType = "";

    /**
     * 学分
     */
    private BigDecimal credits;

    /**
     * 起始周
     */
    private int startWeek;

    /**
     * 结束周
     */
    private int endWeek;
}