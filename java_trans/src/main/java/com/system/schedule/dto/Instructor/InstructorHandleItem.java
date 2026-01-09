package com.system.schedule.dto.instructor;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstructorHandleItem {
    private String applyId = "";              // 申请ID
    private String teacherId = "";            // 教师ID
    private String teacherName = "";          // 教师名称
    private String information = "";          // 申请原因
    private LocalDateTime applyTime;          // 申请时间
    private LocalDateTime reviewTime;         // 审核时间
    private String reviewComments = "";       // 审核意见
    private String finalDecision = "";        // 最终决定
}
package com.system.schedule.dto.instructor;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

// 导师申请处理列表项
@Getter
@Setter
public class InstructorHandleItem {
    private String applyId;              // 申请ID
    private String teacherId;            // 教师ID
    private String teacherName;          // 教师名称
    private String information;          // 申请原因
    private LocalDateTime applyTime;     // 申请时间
    private LocalDateTime reviewTime;    // 审核时间
    private String reviewComments;       // 审核意见
    private String finalDecision;        // 最终决定
}