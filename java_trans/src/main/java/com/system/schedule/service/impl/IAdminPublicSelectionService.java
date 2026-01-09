package com.system.schedule.service;

import com.system.schedule.dto.adminpublish.AdminPublicSelectionDto;
import com.system.schedule.dto.adminpublish.CreateSelectionDto;
import com.system.schedule.dto.adminpublish.UpdateSelectionDto;
import com.system.schedule.dto.adminpublish.SelectionCourseDto;
import com.system.schedule.dto.adminpublish.SelectionMajorDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IAdminPublicSelectionService {
    CompletableFuture<List<AdminPublicSelectionDto>> getAllSelectionsAsync();
    CompletableFuture<AdminPublicSelectionDto> getSelectionByIdAsync(String selectionId);
    CompletableFuture<Boolean> createSelectionAsync(CreateSelectionDto dto);
    CompletableFuture<Boolean> updateSelectionAsync(UpdateSelectionDto dto);
    CompletableFuture<Boolean> deleteSelectionAsync(String selectionId);
    CompletableFuture<Boolean> addCoursesToSelectionAsync(String selectionId, List<String> courseIds);
    CompletableFuture<Boolean> removeCoursesFromSelectionAsync(String selectionId, List<String> courseIds);
    CompletableFuture<Boolean> addMajorsToSelectionAsync(String selectionId, List<String> majorIds);
    CompletableFuture<Boolean> removeMajorsFromSelectionAsync(String selectionId, List<String> majorIds);
    CompletableFuture<Boolean> isSelectionTimeValidAsync(LocalDateTime beginTime, LocalDateTime endTime, String excludeSelectionId);
    // 新增方法：处理选课结果
    CompletableFuture<Boolean> processSelectionResultAsync(String selectionId, boolean forceProcess);
    CompletableFuture<List<SelectionCourseDto>> getAllCoursesAsync();
    CompletableFuture<List<SelectionMajorDto>> getAllMajorsAsync();
    CompletableFuture<List<String>> getAllCourseIdsAsync();
    CompletableFuture<List<String>> getAllMajorIdsAsync();
}