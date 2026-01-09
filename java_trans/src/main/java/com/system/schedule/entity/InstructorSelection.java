package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class InstructorSelection {
    private String selectionId;
    private Date beginTime;
    private Date endTime;
    private String information;
}