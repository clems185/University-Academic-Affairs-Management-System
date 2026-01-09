package com.system.schedule.service;

import com.system.schedule.dto.ScheduleItemDto;
import com.system.schedule.dto.semestercourse.CourseInfoDto;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ISemesterCourseService {
    CompletableFuture<List<ScheduleItemDto>> getStudentScheduleAsync(String studentId, String year, String semester);
    CompletableFuture<List<CourseInfoDto>> getStudentCoursesAsync(String studentId, String year, String semester);
}