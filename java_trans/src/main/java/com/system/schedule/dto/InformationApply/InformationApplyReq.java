package com.system.schedule.dto.informationapply;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InformationApplyReq {
    private String title;
    private String content;
    private String types;
    private String applyComment;
}