// File: ExamApplyController.java
package com.system.schedule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.system.schedule.service.IExamApplyService;
import com.system.schedule.model.dto.ExamApplyCreateReq;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/exam-apply")
public class ExamApplyController {

    private final IExamApplyService examApplyService;

    @Autowired
    public ExamApplyController(IExamApplyService examApplyService) {
        this.examApplyService = examApplyService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getList(@RequestParam String teacherId) {
        var list = examApplyService.getApplyListByTeacher(teacherId);
        return ResponseEntity.ok(Map.of("isSuccess", true, "result", list));
    }

    @GetMapping("/my-courses")
    public ResponseEntity<?> getMyCourses(@RequestParam String teacherId) {
        var courses = examApplyService.getCoursesByTeacher(teacherId);
        return ResponseEntity.ok(Map.of("isSuccess", true, "result", courses));
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@ModelAttribute ExamApplyCreateReq req, @RequestParam String teacherId) {
        if (teacherId == null || teacherId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("isSuccess", false, "message", "无法获取用户信息"));
        }

        var success = examApplyService.createApply(req, teacherId);
        if (success) {
            return ResponseEntity.ok(Map.of("isSuccess", true, "message", "申请提交成功"));
        }
        return ResponseEntity.badRequest().body(Map.of("isSuccess", false, "message", "申请提交失败"));
    }

    @GetMapping("/buildings")
    public ResponseEntity<?> getBuildings() {
        var buildings = examApplyService.getBuildings();
        return ResponseEntity.ok(Map.of("isSuccess", true, "result", buildings));
    }

    @GetMapping("/available-classrooms")
    public ResponseEntity<?> getAvailableClassrooms(
            @RequestParam LocalDateTime startTime,
            @RequestParam int duration,
            @RequestParam String buildingId) {
        
        if (startTime == null || duration <= 0 || buildingId == null || buildingId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("isSuccess", false, "message", "缺少必要的查询参数"));
        }
        
        var classrooms = examApplyService.getAvailableClassrooms(startTime, duration, buildingId);
        return ResponseEntity.ok(Map.of("isSuccess", true, "result", classrooms));
    }

    @GetMapping("/download-paper/{applyId}")
    public ResponseEntity<?> downloadPaper(@PathVariable String applyId) {
        var fileData = examApplyService.downloadPaper(applyId);
        
        if (fileData.getFileContents() == null || fileData.getFileContents().length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到文件或文件为空");
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileData.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileData.getFileContents());
    }

    @PostMapping("/cancel/{applyId}")
    public ResponseEntity<?> cancel(@PathVariable String applyId) {
        var success = examApplyService.cancelApply(applyId);
        if (success) {
            return ResponseEntity.ok(Map.of("isSuccess", true, "message", "申请已成功取消"));
        }
        return ResponseEntity.badRequest().body(Map.of("isSuccess", false, "message", "取消申请失败"));
    }
}