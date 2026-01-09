package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class IsTeacher {
    private String selectionId;
    private String teacherId;
    private BigDecimal capacity;
}