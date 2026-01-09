package com.system.schedule.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class Information {
    private String infoId;
    private String types;
    private String title;
    private String content;
    private Date publishTime;
}