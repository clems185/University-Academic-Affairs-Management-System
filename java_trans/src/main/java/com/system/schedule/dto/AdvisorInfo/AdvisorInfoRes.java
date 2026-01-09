package com.system.schedule.dto.advisorinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvisorInfoRes {
    /// <summary>
    /// 教师ID
    /// </summary>
    @JsonProperty("teacherId")
    private String teacherId = "";

    /// <summary>
    /// 教师姓名
    /// </summary>
    @JsonProperty("teacherName")
    private String teacherName = "";

    /// <summary>
    /// 专业名称
    /// </summary>
    @JsonProperty("majorName")
    private String majorName = "";

    /// <summary>
    /// 个人简介
    /// </summary>
    @JsonProperty("information")
    private String information;

    /// <summary>
    /// 联系电话
    /// </summary>
    @JsonProperty("telephone")
    private String telephone;

    /// <summary>
    /// 电子邮箱
    /// </summary>
    @JsonProperty("email")
    private String email;
}