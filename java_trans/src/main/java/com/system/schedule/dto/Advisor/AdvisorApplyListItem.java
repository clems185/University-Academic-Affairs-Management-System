package com.system.schedule.dto.advisor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvisorApplyListItem {
    private int id;                    // 前端需要的序号字段
    private String applyId = "";      // 真正的申请ID，用于撤回
    private String teacherId = "";    // 导师编号
    private String teacherName = "";  // 导师名称
    private String applyTime = "";    // 申请时间
    private String applyReason = "";  // 申请理由
}
package com.system.schedule.dto.advisor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvisorApplyListItem {
    private int id;                    // 前端需要的序号字段
    private String applyId;          // 真正的申请ID，用于撤回
    private String teacherId;        // 导师编号
    private String teacherName;      // 导师名称
    private String applyTime;        // 申请时间
    private String applyReason;      // 申请理由
}