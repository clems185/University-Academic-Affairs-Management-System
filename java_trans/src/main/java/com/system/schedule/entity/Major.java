package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class Major {
    private String majorId;
    private String name;
    private BigDecimal eduSys;
    private String deptId;
}