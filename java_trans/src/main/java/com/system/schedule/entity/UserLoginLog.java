package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.math.BigDecimal;

@Getter
@Setter
public class UserLoginLog {
    private BigDecimal logId;
    private String userId;
    private Date loginTime;
    private String ipAddress;
    private BigDecimal loginStatus;
}