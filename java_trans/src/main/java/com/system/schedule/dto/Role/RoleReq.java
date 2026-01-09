package com.system.schedule.dto.role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleReq {
    private String name;
    private String discription;
    private int pageIndex;
    private int pageSize;
}