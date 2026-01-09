package com.system.schedule.service;

import com.system.schedule.dto.deferredapply.SubmitDeferredApplyReq;
import com.system.schedule.dto.deferredapply.DeferredApplyRes;
import com.system.schedule.dto.other.ApiResult;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IDeferredApplyService {
    /**
     * 提交缓考申请
     *
     * @param req
     * @return
     */
    CompletableFuture<ApiResult> submitDeferredApply(SubmitDeferredApplyReq req);

    /**
     * 获取学生的缓考申请列表
     *
     * @param studentId
     * @return
     */
    CompletableFuture<List<DeferredApplyRes>> getMyDeferredApplies(String studentId);
}
package com.system.schedule.service;

import com.system.schedule.dto.deferredapply.SubmitDeferredApplyReq;
import com.system.schedule.dto.deferredapply.DeferredApplyRes;
import com.system.schedule.model.other.ApiResult;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IDeferredApplyService {
    /**
     * 提交缓考申请
     */
    CompletableFuture<ApiResult> submitDeferredApply(SubmitDeferredApplyReq req);

    /**
     * 获取学生的缓考申请列表
     */
    CompletableFuture<List<DeferredApplyRes>> getMyDeferredApplies(String studentId);
}