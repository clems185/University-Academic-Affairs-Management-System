package com.system.schedule.dto.myinformation;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class MyInformationRes {
    private String infoId;
    private String types;
    private String title;
    private String content;
    private LocalDateTime publishTime = LocalDateTime.now();
}