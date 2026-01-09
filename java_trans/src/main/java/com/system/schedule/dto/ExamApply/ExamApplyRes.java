package com.system.schedule.dto.examapply;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

// 用于向前端返回考试申请的详细信息
@Getter
@Setter
public class ExamApplyRes {
    private String applyId = "";
    private String courseName = "";
    private String className = "";
    private String location;
    private String status = "";// 申请的当前状态 (P: 处理中, Y: 已同意, N: 已驳回)
    private LocalDateTime applyDate;// 申请提交的日期
    private LocalDateTime proposedTime;// 申请的考试时间
    private String paperFileName; // 试卷的原始文件名
    private String reviewComments;// 管理员的审核意见或备注
}