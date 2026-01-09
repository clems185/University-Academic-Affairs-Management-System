package com.system.schedule.service;

import com.system.schedule.dto.grade.GenerateGradeExcelReq;
import com.system.schedule.dto.grade.GradeCourseRes;
import com.system.schedule.dto.grade.UploadGradeExcelReq;
import com.system.schedule.utils.ApiResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GradeService {
    CompletableFuture<List<GradeCourseRes>> getGradeCourses(String teacherId);
    CompletableFuture<byte[]> generateGradeExcel(GenerateGradeExcelReq req);
    CompletableFuture<ApiResult> uploadGradeExcel(UploadGradeExcelReq req, byte[] excelData);
}
