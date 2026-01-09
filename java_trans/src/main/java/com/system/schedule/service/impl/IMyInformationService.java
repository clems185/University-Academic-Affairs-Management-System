package com.system.schedule.service;

import com.system.schedule.dto.other.ApiResult;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public interface IMyInformationService {
    CompletableFuture<ApiResult> getMyInformations(String types, LocalDateTime startTime, int page, int pageSize);
}