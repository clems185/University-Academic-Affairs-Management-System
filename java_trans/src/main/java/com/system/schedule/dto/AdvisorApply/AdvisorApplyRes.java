package com.system.schedule.dto.advisorapply;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvisorApplyRes {
    /// <summary>
    /// 申请ID
    /// </summary>
    private String applyId = "";

    /// <summary>
    /// 教师ID
    /// </summary>
    private String teacherId = "";

    /// <summary>
    /// 教师姓名
    /// </summary>
    private String teacherName = "";

    /// <summary>
    /// 申请原因
    /// </summary>
    private String information = "";

    /// <summary>
    /// 申请时间
    /// </summary>
    private LocalDateTime applyTime;

    /// <summary>
    /// 审核时间
    /// </summary>
    private LocalDateTime reviewTime;

    /// <summary>
    /// 审核意见
    /// </summary>
    private String reviewComments;

    /// <summary>
    /// 最终决定（Y=同意/N=拒绝/P=处理中）
    /// </summary>
    private String finalDecision;

    /// <summary>
    /// 状态描述
    /// </summary>
    public String getStatusDescription() {
        return switch (finalDecision) {
            case "Y" -> "已同意";
            case "N" -> "已拒绝";
            case "P" -> "处理中";
            default -> "未知状态";
        };
    }
}