package com.system.schedule.dto.examapply;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

// 用于接收前端创建申请时的表单数据
@Getter
@Setter
public class ExamApplyCreateReq {
    private String courseId;
    private String classId;
    private String classroomId;
    private int semester;
    private int year;
    private String bookType;
    private String examType;
    private LocalDateTime examTime; // 选择的考试时间
    private int examDuration; // 单位：分钟
    private MultipartFile paperFile; // 上传的试卷文件
}