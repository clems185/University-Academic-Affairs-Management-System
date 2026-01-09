package com.system.schedule.mapper;

import com.system.schedule.entity.Course;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper {
    Course findByCourseId(String courseId);
}