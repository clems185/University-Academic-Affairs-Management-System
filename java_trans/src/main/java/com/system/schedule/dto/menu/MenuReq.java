package com.system.schedule.dto.menu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuReq {
    private String name;
    private String index;
    private String filePath;
    private String description;
}