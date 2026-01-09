// File: ExamController.java
package com.system.schedule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import com.system.schedule.service.IExamService;
import com.system.schedule.model.dto.ExamScheduleSearchParams;
import com.system.schedule.model.ApiResult;
import com.system.schedule.utils.ResultHelper;
import com.system.schedule.model.entity.Exams;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/exam")
public class ExamController {

    private final IExamService examService;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExamController(IExamService examService, JdbcTemplate jdbcTemplate) {
        this.examService = examService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/schedule")
    public ApiResult getExamSchedule(
            @RequestParam(required = false) String examId,
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) String timeSlotId,
            @RequestParam(required = false) String classroomId,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) Integer year) {
        
        var searchParams = new ExamScheduleSearchParams();
        searchParams.setExamId(examId);
        searchParams.setCourseId(courseId);
        searchParams.setTimeSlotId(timeSlotId);
        searchParams.setClassroomId(classroomId);
        searchParams.setSemester(semester);
        searchParams.setYear(year);

        return examService.getExamScheduleList(searchParams);
    }

    @GetMapping("/test")
    public ApiResult testQuery() {
        try {
            System.out.println("=== 开始全面测试考试安排数据 ===");
            var testResults = new HashMap<String, Object>();

            // 测试1: 数据库连接测试
            try {
                var connectionTest = jdbcTemplate.queryForObject("SELECT 1 FROM DUAL", Integer.class);
                testResults.put("ConnectionTest", "SUCCESS");
                System.out.println("数据库连接测试: 成功");
            } catch (Exception connEx) {
                testResults.put("ConnectionTest", "FAILED: " + connEx.getMessage());
                System.out.println("数据库连接测试失败: " + connEx.getMessage());
            }

            // 测试2: 表存在性检查
            try {
                var userTableQuery = "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = 'EXAMS'";
                var tableExists = jdbcTemplate.queryForObject(userTableQuery, Integer.class);
                
                var schemaTableQuery = "SELECT COUNT(*) FROM ALL_TABLES WHERE OWNER = 'SCHEDULE' AND TABLE_NAME = 'EXAMS'";
                var schemaTableExists = jdbcTemplate.queryForObject(schemaTableQuery, Integer.class);
                
                var tableCheckResult = new HashMap<>();
                tableCheckResult.put("currentUserTable", tableExists > 0);
                tableCheckResult.put("scheduleSchemaTable", schemaTableExists > 0);
                testResults.put("TableExistsCheck", tableCheckResult);
                
                System.out.println("表存在性检查: 当前用户下=" + (tableExists > 0) + ", SCHEDULE模式下=" + (schemaTableExists > 0));
            } catch (Exception tableEx) {
                testResults.put("TableExistsCheck", "FAILED: " + tableEx.getMessage());
                System.out.println("表存在性检查失败: " + tableEx.getMessage());
            }

            // 测试3: 直接SQL查询总数
            try {
                var countSql = "SELECT COUNT(*) FROM SCHEDULE.EXAMS";
                var totalCount = jdbcTemplate.queryForObject(countSql, Integer.class);
                testResults.put("DirectSqlCount", totalCount);
                System.out.println("直接SQL查询总记录数: " + totalCount);
            } catch (Exception countEx) {
                testResults.put("DirectSqlCount", "FAILED: " + countEx.getMessage());
                System.out.println("直接SQL计数失败: " + countEx.getMessage());
            }

            // 测试4: 使用MyBatis或JPA查询
            try {
                // 这里假设使用MyBatis，实际实现需要根据你的持久层框架调整
                var examEntities = examService.getTestExams(3);
                var entityData = examEntities.stream()
                        .map(e -> Map.of(
                                "examId", e.getExamId() != null ? e.getExamId() : "",
                                "courseId", e.getCourseId() != null ? e.getCourseId() : "",
                                "timeSlotId", e.getTimeSlotId() != null ? e.getTimeSlotId() : "",
                                "classroomId", e.getClassroomId() != null ? e.getClassroomId() : "",
                                "semester", e.getSemester(),
                                "year", e.getYear()
                        ))
                        .collect(java.util.stream.Collectors.toList());
                
                var entityQueryResult = new HashMap<>();
                entityQueryResult.put("count", examEntities.size());
                entityQueryResult.put("data", entityData);
                testResults.put("EntityQuery", entityQueryResult);
                
                System.out.println("实体查询结果数量: " + examEntities.size());
            } catch (Exception entityEx) {
                testResults.put("EntityQuery", "FAILED: " + entityEx.getMessage());
                System.out.println("实体查询失败: " + entityEx.getMessage());
            }

            System.out.println("=== 全面测试完成 ===");
            
            var result = new HashMap<>();
            result.put("message", "考试安排综合测试完成");
            result.put("testResults", testResults);
            result.put("testTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            return ResultHelper.success(result);
        } catch (Exception ex) {
            System.out.println("测试过程异常: " + ex.getMessage());
            ex.printStackTrace();
            return ResultHelper.error("测试失败: " + ex.getMessage());
        }
    }
}