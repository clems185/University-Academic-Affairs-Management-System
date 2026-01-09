package com.system.schedule.service;

import com.system.schedule.dto.myinvigilate.InvigilateCourseRes;
import com.system.schedule.dto.myinvigilate.GenerateSeatingReq;
import com.system.schedule.dto.other.ApiResult;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IMyInvigilateService {
    /**
     * 获取教师监考课程列表
     *
     * @param teacherId
     * @return
     */
    CompletableFuture<List<InvigilateCourseRes>> getInvigilateCourses(String teacherId);

    /**
     * 生成座位表
     *
     * @param req
     * @return
     */
    CompletableFuture<ApiResult> generateSeatingChart(GenerateSeatingReq req);
}