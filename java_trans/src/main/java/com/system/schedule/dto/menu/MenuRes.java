package com.system.schedule.dto.menu;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuRes {
    private String name;
    private String index;
    private String filePath;
    private String description;
    private String parentId;
    private List<Object> children;
}