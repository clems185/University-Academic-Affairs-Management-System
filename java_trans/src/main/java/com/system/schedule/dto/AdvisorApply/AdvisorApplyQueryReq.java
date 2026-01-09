package com.system.schedule.dto.advisorapply;

import lombok.Getter;
import lombok.Setter;

/**
 * 导师申请查询请求参数
 */
@Getter
@Setter
public class AdvisorApplyQueryReq {
    /**
     * 教师ID
     */
    private String teacherId;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 申请状态
     */
    private String status;
}