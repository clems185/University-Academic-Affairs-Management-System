package com.system.schedule.dto.examdelay;

import lombok.Getter;
import lombok.Setter;

// 缓考处理参数
@Getter
@Setter
public class ExamDelayHandleParams {
    private String applyId = "";
    private String decision = "";
    private String reviewComments = "";
    private int reviewerId;
}