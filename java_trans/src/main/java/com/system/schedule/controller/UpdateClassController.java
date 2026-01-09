package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.common.ResultHelper;
import com.system.schedule.dto.UpdateClassDto.*;
import com.system.schedule.entity.UpdateClassApply;
import com.system.schedule.service.UpdateClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/updateclass")
public class UpdateClassController extends BaseController {

    @Autowired
    private UpdateClassService updateClassService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取授课申请列表
     */
    @GetMapping("/list")
    public ApiResult getUpdateClassList(
            @RequestParam(required = false) String applyId,
            @RequestParam(required = false) String teacherId,
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        
        var searchParams = new UpdateClassHandleSearchParams();
        searchParams.setApplyId(applyId);
        searchParams.setTeacherId(teacherId);
        searchParams.setCourseId(courseId);
        searchParams.setState(state);
        searchParams.setStartDate(startDate);
        searchParams.setEndDate(endDate);
        
        return updateClassService.getUpdateClassListAsync(searchParams);
    }

    /**
     * 测试方法 - 验证实体映射和数据查询
     */
    @GetMapping("/test")
    public ApiResult testQuery() {
        try {
            System.out.println("=== 开始全面测试授课申请数据 ===");
            Map<String, Object> testResults = new HashMap<>();

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
                String tableExistsQuery = "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = 'UPDATE_CLASS_APPLY_TTOA'";
                var tableExists = jdbcTemplate.queryForObject(tableExistsQuery, Integer.class);
                
                String schemaTableQuery = "SELECT COUNT(*) FROM ALL_TABLES WHERE OWNER = 'SCHEDULE' AND TABLE_NAME = 'UPDATE_CLASS_APPLY_TTOA'";
                var schemaTableExists = jdbcTemplate.queryForObject(schemaTableQuery, Integer.class);
                
                Map<String, Boolean> tableCheck = new HashMap<>();
                tableCheck.put("CurrentUserTable", tableExists > 0);
                tableCheck.put("ScheduleSchemaTable", schemaTableExists > 0);
                testResults.put("TableExistsCheck", tableCheck);
                
                System.out.println("表存在性检查: 当前用户下=" + (tableExists > 0) + ", SCHEDULE模式下=" + (schemaTableExists > 0));
            } catch (Exception tableEx) {
                testResults.put("TableExistsCheck", "FAILED: " + tableEx.getMessage());
                System.out.println("表存在性检查失败: " + tableEx.getMessage());
            }

            // 测试3: 直接SQL查询总数
            try {
                String countSql = "SELECT COUNT(*) FROM SCHEDULE.UPDATE_CLASS_APPLY_TTOA";
                var totalCount = jdbcTemplate.queryForObject(countSql, Integer.class);
                testResults.put("DirectSqlCount", totalCount);
                System.out.println("直接SQL查询总记录数: " + totalCount);
            } catch (Exception countEx) {
                testResults.put("DirectSqlCount", "FAILED: " + countEx.getMessage());
                System.out.println("直接SQL计数失败: " + countEx.getMessage());
            }

            // 测试4: 直接SQL查询样本数据
            try {
                String sampleSql = "SELECT APPLY_ID, TEACHER_ID, COURSE_ID, STATE, INFORMATION, APPLY_TIME " +
                                 "FROM SCHEDULE.UPDATE_CLASS_APPLY_TTOA " +
                                 "WHERE ROWNUM <= 3 " +
                                 "ORDER BY APPLY_TIME DESC";
                
                var sampleData = jdbcTemplate.queryForList(sampleSql);
                var sqlData = new java.util.ArrayList<>();
                
                for (Map<String, Object> row : sampleData) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("ApplyId", row.get("APPLY_ID") != null ? row.get("APPLY_ID").toString() : "");
                    data.put("TeacherId", row.get("TEACHER_ID") != null ? row.get("TEACHER_ID").toString() : "");
                    data.put("CourseId", row.get("COURSE_ID") != null ? row.get("COURSE_ID").toString() : "");
                    data.put("State", row.get("STATE") != null ? row.get("STATE").toString() : "");
                    data.put("Information", row.get("INFORMATION") != null ? row.get("INFORMATION").toString() : "");
                    data.put("ApplyTime", row.get("APPLY_TIME") != null ? row.get("APPLY_TIME").toString() : "");
                    sqlData.add(data);
                }
                
                Map<String, Object> sampleResult = new HashMap<>();
                sampleResult.put("Count", sampleData.size());
                sampleResult.put("Data", sqlData);
                testResults.put("DirectSqlSample", sampleResult);
                
                System.out.println("直接SQL样本查询结果数量: " + sampleData.size());
            } catch (Exception sqlEx) {
                testResults.put("DirectSqlSample", "FAILED: " + sqlEx.getMessage());
                System.out.println("直接SQL样本查询失败: " + sqlEx.getMessage());
            }

            System.out.println("=== 全面测试完成 ===");
            
            Map<String, Object> result = new HashMap<>();
            result.put("Message", "综合测试完成");
            result.put("TestResults", testResults);
            result.put("TestTime", LocalDateTime.now().toString());
            
            return ResultHelper.success(result);
        } catch (Exception ex) {
            System.out.println("测试过程异常: " + ex.getMessage());
            ex.printStackTrace();
            return ResultHelper.error("测试失败: " + ex.getMessage());
        }
    }

    /**
     * 批量同意授课申请
     */
    @PostMapping("/batchApprove")
    public ApiResult batchApprove(@RequestBody UpdateClassBatchParams params) {
        return updateClassService.batchApproveAsync(params.getIds());
    }

    /**
     * 批量拒绝授课申请
     */
    @PostMapping("/batchReject")
    public ApiResult batchReject(@RequestBody UpdateClassBatchParams params) {
        return updateClassService.batchRejectAsync(params.getIds());
    }

    /**
     * 同意单个授课申请
     */
    @PostMapping("/approve")
    public ApiResult approve(@RequestBody UpdateClassSingleParams params) {
        return updateClassService.approveAsync(params.getId(), params.getReviewComments(), params.getReviewerId());
    }

    /**
     * 拒绝单个授课申请
     */
    @PostMapping("/reject")
    public ApiResult reject(@RequestBody UpdateClassSingleParams params) {
        return updateClassService.rejectAsync(params.getId(), params.getReviewComments(), params.getReviewerId());
    }
}