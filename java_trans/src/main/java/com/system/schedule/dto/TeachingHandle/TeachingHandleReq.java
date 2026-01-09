package com.system.schedule.dto.teachinghandle;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/// <summary>
/// 审核申请请求
/// </summary>
@Getter @Setter
public class ApproveTeachingApplyReq {
    @NotNull(message = "申请ID不能为空")
    private String applyId;

    @NotNull(message = "审核意见不能为空")
    @Size(min = 1, max = 255, message = "审核意见长度必须在1-255个字符之间")
    private String reviewComments;
}

/// <summary>
/// 拒绝申请请求
/// </summary>
@Getter @Setter
public class RejectTeachingApplyReq {
    @NotNull(message = "申请ID不能为空")
    private String applyId;

    @NotNull(message = "拒绝理由不能为空")
    @Size(min = 1, max = 255, message = "拒绝理由长度必须在1-255个字符之间")
    private String reviewComments;
}

/// <summary>
/// 批量处理申请请求
/// </summary>
@Getter @Setter
public class BatchProcessTeachingApplyReq {
    @NotNull(message = "申请ID列表不能为空")
    private List<String> applyIds;

    @NotNull(message = "处理意见不能为空")
    @Size(min = 1, max = 255, message = "处理意见长度必须在1-255个字符之间")
    private String reviewComments;
}

/// <summary>
/// 删除申请请求
/// </summary>
@Getter @Setter
public class DeleteTeachingApplyReq {
    @NotNull(message = "申请ID不能为空")
    private String applyId;
}

/// <summary>
/// 批量删除申请请求
/// </summary>
@Getter @Setter
public class BatchDeleteTeachingApplyReq {
    @NotNull(message = "申请ID列表不能为空")
    private List<String> applyIds;
}

/// <summary>
/// 搜索处理申请请求
/// </summary>
@Getter @Setter
public class TeachingHandleSearchReq {
    private String applyId;
    private String teacherId;
    private String courseId;
    private String state; // Y, N, P
}