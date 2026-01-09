package com.system.schedule.dto.Profile;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDto {
    private String id = "";
    private String name = "";
    private String nickName;
    private int userType;
    private String email;
    private String majorName;
    private String telephone;
    private Integer startYear;
    private Integer endYear;
    private BigDecimal gpa;
    private BigDecimal attemptedCredit;
    private BigDecimal earnedCredit;

    private Integer workYear;
    private String information;

    // 新增：头像字段（Base64格式，可为null）
    private String avatar;

    // 新增：教师专属字段（学生端返回null）
    private Integer courseCount; // 当前授课数
    private Integer studentCount; // 指导学生数
    private String title; // 职称
}
package com.system.schedule.dto.profile;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDto {
    private String id = "";
    private String name = "";
    private String nickName;
    private int userType;
    private String email;
    private String majorName;
    private String telephone;
    private Integer startYear;
    private Integer endYear;
    private BigDecimal gpa;
    private BigDecimal attemptedCredit;
    private BigDecimal earnedCredit;

    private Integer workYear;
    private String information;

    // 新增：头像字段（Base64格式，可为null）
    private String avatar;

    // 新增：教师专属字段（学生端返回null）
    private Integer courseCount; // 当前授课数
    private Integer studentCount; // 指导学生数
    private String title; // 职称
}