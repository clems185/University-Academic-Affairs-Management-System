package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.math.BigDecimal;

@Getter
@Setter
public class Selection {
    private String selectionId;
    private Date beginTime;
    private Date endTime;
    private String information;
    private BigDecimal type;
    private String semester;
    private String year;
    private String isProcessed;
}