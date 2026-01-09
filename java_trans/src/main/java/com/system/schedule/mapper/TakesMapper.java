package com.system.schedule.mapper;


import com.system.schedule.entity.Takes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TakesMapper{

    @Update("ALTER TRIGGER SCHEDULE.CREDITS_GPA_EARNED DISABLE")
    void disableTrigger();

    @Update("ALTER TRIGGER SCHEDULE.CREDITS_GPA_EARNED ENABLE")
    void enableTrigger();
    Integer countAll();
    Integer countByCourseId(String courseId);
    Integer countByCourseAndClassId(String courseId, String classId);
    Integer countByCourseClassAndSemester(String courseId, String classId, Integer semester);
    List<Takes> findByCourseClassSemesterAndYear(String courseId, String classId, Integer semester, Integer year);
    List<Takes> findSampleData(Integer limit);
    Integer countByCourseAndClass(String courseId, String classId, Integer semester, Integer year);
    int updateGradeByStudentAndCourse(String studentId, String courseId, String classId, Integer semester, Integer year, String grade);

}