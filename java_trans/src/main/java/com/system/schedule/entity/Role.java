package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.math.BigDecimal;

@Getter
@Setter
@SuppressWarnings({"all"})
public class Role {
    private String id;
    private String roleId;
    private String createUserId;
    private Date createdate;
    private BigDecimal isDelete;
    private String modifyUserId;
    private Date modifydate;
    private String name;
    private BigDecimal discription;
    private BigDecimal order;
}