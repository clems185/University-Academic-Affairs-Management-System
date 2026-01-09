package com.system.schedule.dto.competition;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompetitionHandleItem {
    private String contestId;
    private String name;
    private LocalDateTime contestTime;
    private String grade;
    private String url;
    private String information;
    private int numbers;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String state;
    private LocalDateTime reviewTime;
    private String reviewComments;
}