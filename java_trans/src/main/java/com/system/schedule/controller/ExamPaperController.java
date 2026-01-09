// File: ExamPaperController.java
package com.system.schedule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import com.system.schedule.service.IExamPaperService;
import com.system.schedule.model.dto.ExamPaperSearchParams;
import com.system.schedule.model.ApiResult;
import com.system.schedule.utils.ResultHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@RestController
@RequestMapping("/api/exam-paper")
public class ExamPaperController {

    private final IExamPaperService examPaperService;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExamPaperController(IExamPaperService examPaperService, JdbcTemplate jdbcTemplate) {
        this.examPaperService = examPaperService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/list")
    public ApiResult getExamPaperList(
            @RequestParam(required = false) String examId,
            @RequestParam(required = false) String comment) {
        
        var searchParams = new ExamPaperSearchParams();
        searchParams.setExamId(examId);
        searchParams.setComment(comment);

        return examPaperService.getExamPaperList(searchParams);
    }

    @GetMapping("/detail/{examId}")
    public ApiResult getExamPaperDetail(@PathVariable String examId) {
        return examPaperService.getExamPaperDetail(examId);
    }

    @GetMapping("/download/{examId}")
    public ApiResult downloadExamPaper(@PathVariable String examId) {
        return examPaperService.downloadExamPaper(examId);
    }

    @GetMapping("/test")
    public ApiResult testQuery() {
        try {
            System.out.println("=== 开始全面测试试卷数据 ===");
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
                var userTableQuery = "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = 'EXAM_PAPERS'";
                var tableExists = jdbcTemplate.queryForObject(userTableQuery, Integer.class);
                
                var schemaTableQuery = "SELECT COUNT(*) FROM ALL_TABLES WHERE OWNER = 'SCHEDULE' AND TABLE_NAME = 'EXAM_PAPERS'";
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
                var countSql = "SELECT COUNT(*) FROM SCHEDULE.EXAM_PAPERS";
                var totalCount = jdbcTemplate.queryForObject(countSql, Integer.class);
                testResults.put("DirectSqlCount", totalCount);
                System.out.println("直接SQL查询总记录数: " + totalCount);
            } catch (Exception countEx) {
                testResults.put("DirectSqlCount", "FAILED: " + countEx.getMessage());
                System.out.println("直接SQL计数失败: " + countEx.getMessage());
            }

            System.out.println("=== 全面测试完成 ===");
            
            var result = new HashMap<>();
            result.put("message", "综合测试完成");
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