package com.system.schedule.service;

import com.system.schedule.dto.classroom.ClassroomQueryReq;
import com.system.schedule.dto.classroom.ClassroomUsageRes;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IClassroomService {
    CompletableFuture<List<ClassroomUsageRes>> getClassroomUsageAsync(ClassroomQueryReq req);
    CompletableFuture<List<ClassroomUsageRes>> getCampusListAsync();
    CompletableFuture<List<ClassroomUsageRes>> getBuildingListAsync(String campusId);
    CompletableFuture<List<ClassroomUsageRes>> getTimeSlotListAsync();
    CompletableFuture<ClassroomUsageRes> getClassroomInfoAsync(String classroomId);
}