package com.system.schedule.dto.allinfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherInfoRes {
    private String teacherId;
    private String teacherName;
    private String teacherSex;
    private String teacherLevel;
    private String majorId;
    private String majorName;
    private Integer startYear;
    private Integer workYear;
    private String email;
    private String telephone;
}