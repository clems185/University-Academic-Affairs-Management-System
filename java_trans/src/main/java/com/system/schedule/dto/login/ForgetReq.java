package com.system.schedule.dto.login;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgetReq {
    private String name;
    private String email;
    private String verification;
    private String newPassword;


}