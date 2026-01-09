package com.system.schedule.service;

import com.system.schedule.dto.myexam.MyExamRes;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IMyExamService {
    /**
     * 获取学生考试列表
     *
     * @param studentId
     * @return
     */
    CompletableFuture<List<MyExamRes>> getMyExams(String studentId);
}