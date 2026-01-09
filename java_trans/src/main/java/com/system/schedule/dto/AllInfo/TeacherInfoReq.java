package com.system.schedule.dto.allinfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherInfoReq {
    private String teacherName;
    private String majorId;
    private String teacherSex;
    private String teacherLevel;
    private int startYear;
    private Integer workYear;
    private String email;
    private String telephone;
}