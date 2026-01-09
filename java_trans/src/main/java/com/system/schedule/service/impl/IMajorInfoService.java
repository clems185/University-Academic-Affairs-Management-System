package com.system.schedule.service;

import com.system.schedule.dto.majorinfo.MajorInfoRes;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IMajorInfoService {
    CompletableFuture<List<MajorInfoRes>> getMajorList();
}