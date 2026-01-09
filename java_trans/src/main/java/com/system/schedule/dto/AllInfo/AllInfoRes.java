package com.system.schedule.dto.allinfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllInfoRes {
    private String id;
    private String name;
    private String gender;
    private String majorId;
    private String majorName;
    private String email;
    private String phone;
    private int userType;
    private String typeDetail;
    
    public String getUserState() {
        return switch (userType) {
            case 1 -> "学生";
            case 2 -> "教师";
            case 3 -> "管理员";
            default -> "未知";
        };
    }
}