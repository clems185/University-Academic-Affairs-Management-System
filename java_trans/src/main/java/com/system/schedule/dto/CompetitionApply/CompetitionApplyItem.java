package com.system.schedule.dto.competitionapply;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompetitionApplyItem {
    private String applyId = "";          // 申请ID
    private String studentId = "";        // 学生ID
    private String studentName = "";      // 学生姓名
    private String contestId = "";        // 竞赛ID
    private String contestName = "";      // 竞赛名称
    private String contestGrade = "";     // 竞赛等级
    private LocalDateTime contestTime;     // 竞赛举办时间
    private LocalDateTime registrationDeadline; // 报名截止时间
    private String contestType = "";      // 竞赛类型
    private int maxParticipants;           // 最大参与人数
    private String applyReason = "";      // 申请理由
    private String contactInfo = "";      // 联系方式
    private LocalDateTime applyTime;       // 申请时间
    private String state = "";            // 申请状态
    private String stateText = "";        // 状态文本
    private LocalDateTime reviewTime;      // 审核时间
    private String reviewComments = "";   // 审核意见
}
package com.system.schedule.dto.competitionapply;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class CompetitionApplyItem {
    private String applyId;          // 申请ID
    private String studentId;        // 学生ID
    private String studentName;      // 学生姓名
    private String contestId;        // 竞赛ID
    private String contestName;      // 竞赛名称
    private String contestGrade;     // 竞赛等级
    private LocalDateTime contestTime;                    // 竞赛举办时间
    private LocalDateTime registrationDeadline;           // 报名截止时间
    private String contestType;      // 竞赛类型
    private int maxParticipants;                     // 最大参与人数
    private String applyReason;      // 申请理由
    private String contactInfo;      // 联系方式
    private LocalDateTime applyTime;                      // 申请时间
    private String state;            // 申请状态
    private String stateText;        // 状态文本
    private LocalDateTime reviewTime;                    // 审核时间
    private String reviewComments;   // 审核意见
}