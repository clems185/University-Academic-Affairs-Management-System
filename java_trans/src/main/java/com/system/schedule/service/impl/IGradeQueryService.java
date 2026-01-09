package com.system.schedule.service;

import com.system.schedule.dto.gradequery.GradeQueryRes;
import com.system.schedule.dto.gradequery.GradeQueryReq;
import com.system.schedule.dto.gradequery.GradeSummaryRes;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface IGradeQueryService {
    CompletableFuture<List<GradeQueryRes>> getGradesByStudentAsync(String studentId);
    CompletableFuture<List<GradeQueryRes>> getGradesByConditionAsync(GradeQueryReq req);
    CompletableFuture<GradeSummaryRes> getGradeSummaryAsync(String studentId);
    CompletableFuture<Map<String, List<GradeQueryRes>>> getGradesBySemesterAsync(String studentId);
}