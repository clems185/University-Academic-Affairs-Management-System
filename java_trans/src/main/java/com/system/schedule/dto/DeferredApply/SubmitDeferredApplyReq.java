package com.system.schedule.dto.deferredapply;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SubmitDeferredApplyReq {
    @NotNull(message = "学生ID不能为空")
    private String studentId;

    @NotNull(message = "课程ID不能为空")
    private String courseId;

    @NotNull(message = "申请理由不能为空")
    @Size(max = 4000, message = "申请理由不能超过4000个字符")
    private String information;
}package com.system.schedule.dto.deferredapply;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitDeferredApplyReq {
    private String studentId;
    private String courseId;
    private String information;
}