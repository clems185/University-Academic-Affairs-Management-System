package com.system.schedule.service;

import com.system.schedule.dto.examapply.ExamApplyRes;
import com.system.schedule.dto.examapply.ExamApplyCreateReq;
import com.system.schedule.dto.examapply.TeacherCourseRes;
import com.system.schedule.entity.Building;
import com.system.schedule.entity.Classroom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IExamApplyService {
    CompletableFuture<List<ExamApplyRes>> getApplyListByTeacher(String teacherId); // 获取指定教师的所有考试申请列表
    CompletableFuture<Boolean> createApply(ExamApplyCreateReq req, String teacherId);// 创建一个新的考试申请
    CompletableFuture<List<Building>> getBuildings();// 获取所有教学楼列表
    CompletableFuture<List<Classroom>> getAvailableClassrooms(LocalDateTime startTime, int duration, String buildingId);// 获取指定时间段内可用的教室列表
    CompletableFuture<List<TeacherCourseRes>> getCoursesByTeacher(String teacherId);// 获取指定教师所教的课程和班级列表
    CompletableFuture<Pair<byte[], String>> downloadPaper(String applyId);// 下载指定考试申请的试卷文件
    CompletableFuture<Boolean> cancelApply(String applyId);// 取消指定的考试申请

    // 自定义Pair类用于处理tuple返回类型
    class Pair<T1, T2> {
        private T1 first;
        private T2 second;

        public Pair(T1 first, T2 second) {
            this.first = first;
            this.second = second;
        }

        public T1 getFirst() {
            return first;
        }

        public T2 getSecond() {
            return second;
        }
    }
}