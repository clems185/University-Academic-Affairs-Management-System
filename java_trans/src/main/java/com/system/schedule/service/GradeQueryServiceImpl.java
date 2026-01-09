package com.system.schedule.service;

import com.system.schedule.dto.gradequery.GradeQueryReq;
import com.system.schedule.dto.gradequery.GradeQueryRes;
import com.system.schedule.dto.gradequery.GradeSummaryRes;
import com.system.schedule.service.IGradeQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class GradeQueryServiceImpl implements IGradeQueryService {

    private static final Logger logger = LoggerFactory.getLogger(GradeQueryServiceImpl.class);

    @Override
    public CompletableFuture<List<GradeQueryRes>> getGradesByStudentAsync(String studentId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始获取学生 {} 的成绩信息", studentId);
                
                // 模拟数据
                List<GradeQueryRes> grades = mockGrades();
                
                // 过滤数据
                List<GradeQueryRes> studentGrades = grades.stream()
                        .filter(grade -> grade.getStudentId().equals(studentId))
                        .collect(Collectors.toList());
                
                logger.info("获取学生 {} 的成绩信息成功，共 {} 门课程", studentId, studentGrades.size());
                return studentGrades;
                
            } catch (Exception e) {
                logger.error("获取学生成绩信息失败: {}", e.getMessage(), e);
                return new ArrayList<>();
            }
        });
    }

    @Override
    public CompletableFuture<List<GradeQueryRes>> getGradesByConditionAsync(GradeQueryReq req) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始按条件查询成绩，学生ID: {}, 课程ID: {}, 学年: {}, 学期: {}", 
                        req.getStudentId(), req.getCourseId(), req.getYear(), req.getSemester());
                
                // 模拟数据
                List<GradeQueryRes> grades = mockGrades();
                
                // 过滤数据
                List<GradeQueryRes> filteredGrades = grades.stream()
                        .filter(grade -> req.getStudentId() == null || grade.getStudentId().equals(req.getStudentId()))
                        .filter(grade -> req.getCourseId() == null || grade.getCourseId().equals(req.getCourseId()))
                        .filter(grade -> req.getYear() == null || grade.getYear().equals(req.getYear()))
                        .filter(grade -> req.getSemester() == null || grade.getSemester().equals(req.getSemester()))
                        .collect(Collectors.toList());
                
                logger.info("按条件查询成绩成功，共 {} 条记录", filteredGrades.size());
                return filteredGrades;
                
            } catch (Exception e) {
                logger.error("按条件查询成绩失败: {}", e.getMessage(), e);
                return new ArrayList<>();
            }
        });
    }

    @Override
    public CompletableFuture<GradeSummaryRes> getGradeSummaryAsync(String studentId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始获取学生 {} 的成绩汇总信息", studentId);
                
                // 模拟数据
                List<GradeQueryRes> grades = mockGrades();
                
                // 过滤数据
                List<GradeQueryRes> studentGrades = grades.stream()
                        .filter(grade -> grade.getStudentId().equals(studentId))
                        .collect(Collectors.toList());
                
                // 计算成绩汇总
                GradeSummaryRes summary = new GradeSummaryRes();
                summary.setStudentId(studentId);
                summary.setTotalCourses(studentGrades.size());
                
                // 计算平均分
                double avgScore = studentGrades.stream()
                        .mapToDouble(GradeQueryRes::getScore)
                        .average()
                        .orElse(0.0);
                summary.setAverageScore(avgScore);
                
                // 计算优秀、良好、及格、不及格课程数
                long excellentCount = studentGrades.stream().filter(grade -> grade.getScore() >= 90).count();
                long goodCount = studentGrades.stream().filter(grade -> grade.getScore() >= 80 && grade.getScore() < 90).count();
                long passCount = studentGrades.stream().filter(grade -> grade.getScore() >= 60 && grade.getScore() < 80).count();
                long failCount = studentGrades.stream().filter(grade -> grade.getScore() < 60).count();
                
                summary.setExcellentCourses((int) excellentCount);
                summary.setGoodCourses((int) goodCount);
                summary.setPassCourses((int) passCount);
                summary.setFailCourses((int) failCount);
                
                logger.info("获取学生 {} 的成绩汇总信息成功", studentId);
                return summary;
                
            } catch (Exception e) {
                logger.error("获取学生成绩汇总信息失败: {}", e.getMessage(), e);
                return new GradeSummaryRes();
            }
        });
    }

    @Override
    public CompletableFuture<Map<String, List<GradeQueryRes>>> getGradesBySemesterAsync(String studentId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始按学期获取学生 {} 的成绩信息", studentId);
                
                // 模拟数据
                List<GradeQueryRes> grades = mockGrades();
                
                // 过滤数据
                List<GradeQueryRes> studentGrades = grades.stream()
                        .filter(grade -> grade.getStudentId().equals(studentId))
                        .collect(Collectors.toList());
                
                // 按学期分组
                Map<String, List<GradeQueryRes>> gradesBySemester = studentGrades.stream()
                        .collect(Collectors.groupingBy(grade -> grade.getYear() + "-" + grade.getSemester()));
                
                logger.info("按学期获取学生 {} 的成绩信息成功，共 {} 个学期", studentId, gradesBySemester.size());
                return gradesBySemester;
                
            } catch (Exception e) {
                logger.error("按学期获取学生成绩信息失败: {}", e.getMessage(), e);
                return new HashMap<>();
            }
        });
    }
    
    // 模拟数据
    private List<GradeQueryRes> mockGrades() {
        List<GradeQueryRes> grades = new ArrayList<>();
        
        String[] studentIds = {"S1001", "S1002", "S1003", "S1004", "S1005"};
        String[] courseIds = {"CS101", "CS102", "MA101", "PH101", "EN101"};
        String[] courseNames = {"计算机基础", "编程入门", "高等数学", "大学物理", "大学英语"};
        String[] years = {"2022", "2023", "2024"};
        String[] semesters = {"1", "2"};
        
        Random random = new Random();
        
        for (String studentId : studentIds) {
            for (int i = 0; i < courseIds.length; i++) {
                for (String year : years) {
                    for (String semester : semesters) {
                        // 不是每个学生每个学期都上所有课程
                        if (random.nextBoolean()) {
                            GradeQueryRes grade = new GradeQueryRes();
                            grade.setStudentId(studentId);
                            grade.setCourseId(courseIds[i]);
                            grade.setCourseName(courseNames[i]);
                            grade.setYear(year);
                            grade.setSemester(semester);
                            grade.setScore(60 + random.nextInt(41)); // 60-100分
                            grade.setCredits(2 + random.nextInt(3)); // 2-4学分
                            grade.setGrade(String.valueOf((char)('A' + random.nextInt(5)))); // A-E等级
                            grades.add(grade);
                        }
                    }
                }
            }
        }
        
        return grades;
    }
}