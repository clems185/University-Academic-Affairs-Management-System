package com.system.schedule.dto.selection;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectionRoundDto {
    private String selectionId = "";
    private String information = "";
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private boolean isActive;
    private int type; // 0:随机踢人 1:人满为止
    private int semester; // 0:第一学期 1:第二学期
    private int year; // 学年
}