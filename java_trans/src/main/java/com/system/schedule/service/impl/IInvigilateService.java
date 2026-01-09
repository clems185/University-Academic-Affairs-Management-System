package com.system.schedule.service;

import com.system.schedule.dto.invigilate.InvigilateDto;
import com.system.schedule.dto.invigilate.SeatArrangementRequest;
import com.system.schedule.dto.invigilate.SeatArrangementDto;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IInvigilateService {
    CompletableFuture<List<InvigilateDto>> getInvigilateList(String teacherId);// 获取监考信息列表
    CompletableFuture<SeatArrangementDto> generateSeatArrangement(SeatArrangementRequest req);// 生成座位安排
}