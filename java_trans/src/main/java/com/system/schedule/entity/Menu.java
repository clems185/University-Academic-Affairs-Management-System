package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.math.BigDecimal;

@Getter
@Setter
public class Menu {
    private String id;
    private String name;
    private String index;
    private String filepath;
    private String parentid;
    private BigDecimal order;
    private BigDecimal isenable;
    private String icon;
    private String description;
    private Date createdate;
    private String createuserid;
    private BigDecimal isdeleted;
    private Date modifydate;
    private String modifyuserid;
}