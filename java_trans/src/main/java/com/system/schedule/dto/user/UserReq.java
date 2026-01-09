package com.system.schedule.dto.user;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReq {
    private int id;
    private String name;
    private String nickName;
    private int userType;
    private String email;
    private String image;
    private LocalDateTime createdTime;
}