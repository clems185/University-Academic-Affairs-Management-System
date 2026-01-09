package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.math.BigDecimal;

@Getter
@Setter
@SuppressWarnings({"all"})
public class YearSemesterStarttime {
    private String year;
    private String semester;
    private Date startDate;
}