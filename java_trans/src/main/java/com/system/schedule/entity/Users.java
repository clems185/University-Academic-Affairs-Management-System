package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Users {
    private String userId;
    private String userName;
    private String nickName;
    private String password;
    private String salt;
    private String userType;
    private String email;
    private String verification;
    private Date createTime;
}