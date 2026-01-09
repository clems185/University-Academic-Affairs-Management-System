// GradeController.java
package com.system.schedule.controller;

import com.system.schedule.common.ApiResult;
import com.system.schedule.dto.GenerateGradeExcelReq;
import com.system.schedule.dto.UploadGradeExcelReq;
import com.system.schedule.service.IGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/grade")
@Api(tags = "成绩控制器")
@PreAuthorize("isAuthenticated()")
@Validated
public class GradeController extends BaseController {
    
    @Autowired
    private IGradeService gradeService;
    
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".csv", ".xls", ".xlsx");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    
    @GetMapping("/courses/{teacherId}")
    @ApiOperation("获取教师成绩登记课程列表")
    public ApiResult<?> getGradeCourses(@PathVariable String teacherId) {
        try {
            if (teacherId == null || teacherId.isEmpty()) {
                return ApiResult.error("教师ID不能为空");
            }
            
            Object courses = gradeService.getGradeCourses(teacherId);
            logger.info("[GradeController] GetGradeCourses done, teacherId={}, resultCount={}", 
                    teacherId, courses != null ? ((java.util.List<?>)courses).size() : 0);
            return ApiResult.success(courses);
        } catch (Exception ex) {
            logger.error("[GradeController] 获取成绩登记课程列表失败", ex);
            return ApiResult.error("获取成绩登记课程列表失败");
        }
    }
    
    @PostMapping("/generate-excel")
    @ApiOperation("生成成绩登记Excel文件")
    public ResponseEntity<Resource> generateGradeExcel(@Valid @RequestBody GenerateGradeExcelReq req) {
        try {
            logger.info("Grade generate-excel start, CourseId={}, ClassId={}, Semester={}, Year={}",
                    req.getCourseId(), req.getClassId(), req.getSemester(), req.getYear());
            
            logger.info("开始调用服务生成Excel, 参数: CourseId={}, ClassId={}, Semester={}, Year={}",
                    req.getCourseId(), req.getClassId(), req.getSemester(), req.getYear());
            
            byte[] excelData = gradeService.generateGradeExcel(req);
            
            logger.info("服务返回数据长度: {} bytes", excelData != null ? excelData.length : 0);
            
            if (excelData == null || excelData.length == 0) {
                logger.warn("生成的Excel数据为空");
                return ResponseEntity.badRequest().body(null);
            }
            
            String fileName = String.format("成绩登记表_%s_%s_%s.csv",
                    req.getCourseId(), req.getClassId(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            
            logger.info("Grade generate-excel end, fileName={}, dataSize={}", fileName, excelData.length);
            
            ByteArrayResource resource = new ByteArrayResource(excelData);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .contentLength(excelData.length)
                    .body(resource);
        } catch (Exception ex) {
            logger.error("生成成绩Excel失败", ex);
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @PostMapping(value = "/upload-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("上传成绩Excel文件")
    public ApiResult<?> uploadGradeExcel(
            @ModelAttribute @Valid UploadGradeExcelReq req,
            @RequestParam(value = "excelFile", required = false) MultipartFile excelFile) {
        try {
            logger.info("Grade upload-excel start, CourseId={}, ClassId={}, FileName={}",
                    req.getCourseId(), req.getClassId(), excelFile != null ? excelFile.getOriginalFilename() : "null");
            
            if (excelFile == null || excelFile.isEmpty()) {
                return ApiResult.error("请选择Excel文件");
            }
            
            // 验证文件类型
            String originalFilename = excelFile.getOriginalFilename();
            if (originalFilename == null) {
                return ApiResult.error("文件名不能为空");
            }
            
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
                return ApiResult.error("只支持CSV、XLS、XLSX格式的文件");
            }
            
            // 验证文件大小
            if (excelFile.getSize() > MAX_FILE_SIZE) {
                return ApiResult.error("文件大小不能超过5MB");
            }
            
            byte[] excelData = excelFile.getBytes();
            ApiResult<?> result = gradeService.uploadGradeExcel(req, excelData);
            logger.info("Grade upload-excel end");
            return result;
        } catch (Exception ex) {
            logger.error("上传成绩Excel失败", ex);
            return ApiResult.error("上传成绩Excel失败");
        }
    }
}