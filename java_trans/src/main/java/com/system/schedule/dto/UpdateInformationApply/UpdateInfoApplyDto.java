package com.system.schedule.dto.updateinformationapply;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

// 更新信息申请搜索参数
@Getter @Setter
public class UpdateInfoApplySearchParams {
    private String applicantId;           // 申请人ID
    private String applicantType;         // 申请人类型
    private String finalDecision;         // 审核状态（Y=同意/N=拒绝/P=处理中）
    private LocalDateTime applyTimeStart;      // 申请时间开始
    private LocalDateTime applyTimeEnd;        // 申请时间结束
}

// 更新信息申请列表项
@Getter @Setter
public class UpdateInfoApplyItem {
    private String applyId = "";           // 申请ID
    private String applicantId = "";       // 申请人ID
    private String applicantType = "";     // 申请人类型
    private LocalDateTime applyTime;       // 申请时间
    private String information = "";       // 修改原因
    private boolean hasNewProfile;         // 是否有新简介
    private boolean hasNewPhoto;           // 是否有新头像
    private LocalDateTime reviewTime;      // 审核时间
    private String reviewComments;         // 审核意见
    private String finalDecision = "P";   // 最终决定
    private String finalDecisionDisplay = "";  // 决定显示文本
    private String applicantTypeDisplay = "";  // 申请人类型显示文本
}

// 更新信息申请详情
@Getter @Setter
public class UpdateInfoApplyDetail {
    private String applyId = "";           // 申请ID
    private String applicantId = "";       // 申请人ID
    private String applicantType = "";     // 申请人类型
    private LocalDateTime applyTime;       // 申请时间
    private String information = "";       // 修改原因
    private String newProfile;             // 新个人简介
    private String newPhotoBase64;         // 新头像Base64编码
    private LocalDateTime reviewTime;      // 审核时间
    private String reviewComments;         // 审核意见
    private String finalDecision = "P";   // 最终决定
    private String finalDecisionDisplay = "";  // 决定显示文本
    private String applicantTypeDisplay = "";  // 申请人类型显示文本
}

// 创建更新信息申请参数（用于头像申请）
@Getter @Setter
public class CreatePhotoApplyParams {
    private String applicantId = "";       // 申请人ID
    private String applicantType = "";     // 申请人类型
    private String information = "";       // 申请原因
    private String newPhotoBase64 = "";    // 新头像Base64编码
}

// 审核更新信息申请参数
@Getter @Setter
public class ReviewUpdateInfoApplyParams {
    private String applyId = "";           // 申请ID
    private String reviewComments;         // 审核意见（可为空）
    private String finalDecision = "";     // 最终决定（Y=同意/N=拒绝）
}

// 批量审核参数
@Getter @Setter
public class BatchReviewUpdateInfoApplyParams {
    private List<String> applyIds = new java.util.ArrayList<>();  // 申请ID列表
    private String reviewComments;                        // 审核意见（可为空）
    private String finalDecision = "";                   // 最终决定（Y=同意/N=拒绝）
}