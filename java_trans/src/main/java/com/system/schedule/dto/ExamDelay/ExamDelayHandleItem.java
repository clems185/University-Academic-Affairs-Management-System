package com.system.schedule.dto.examdelay;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

// 缓考处理列表项
@Getter
@Setter
public class ExamDelayHandleItem {
    private String applyId;           // 申请ID
    private String studentId;         // 学生ID
    private String courseId;          // 课程ID
    private LocalDateTime applyDate;                       // 申请时间
    private String information;       // 申请内容
    private LocalDateTime reviewTime;                     // 审核时间
    private String reviewComments;    // 审核意见
    private String state;             // 处理状态
}