package com.system.schedule.service;

import com.system.schedule.dto.myteaching.MyTeachingSearchParams;
import com.system.schedule.dto.other.ApiResult;
import java.util.concurrent.CompletableFuture;

public interface IMyTeachingService {
    /**
     * 获取我的授课信息
     */
    CompletableFuture<ApiResult> getMyTeachingAsync(MyTeachingSearchParams searchParams);
    
    /**
     * 获取我的授课课程班级列表
     */
    CompletableFuture<ApiResult> getMyTeachingClassesAsync(MyTeachingSearchParams searchParams);
    
    /**
     * 获取指定班级的学生信息
     */
    CompletableFuture<ApiResult> getClassStudentsAsync(MyTeachingSearchParams searchParams);
}