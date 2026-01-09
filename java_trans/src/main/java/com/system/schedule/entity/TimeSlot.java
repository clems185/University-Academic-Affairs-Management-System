package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@SuppressWarnings({"all"})
public class TimeSlot {
    private String timeSlotId;
    private Date startTime;
    private Date endTime;
    private String day;
}