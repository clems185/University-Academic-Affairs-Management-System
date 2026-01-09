package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class Classroom {
    private String buildingId;
    private String classroomId;
    private String roomNumber;
    private BigDecimal capacity;
    private String type;
}