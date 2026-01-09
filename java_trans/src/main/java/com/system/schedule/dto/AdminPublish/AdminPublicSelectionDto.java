package com.system.schedule.dto.adminpublish;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminPublicSelectionDto {
    private String selectionId = "";
    private String information = "";
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private boolean isActive;
    private int type; // 0:随机踢人 1:人满为止
    private int semester; // 0:第一学期 1:第二学期
    private int year; // 学年
    private String isProcessed; // 是否已处理
    private List<SelectionCourseDto> courses = new ArrayList<>();
    private List<SelectionMajorDto> majors = new ArrayList<>();

    @Getter
    @Setter
    public static class SelectionCourseDto {
        private String courseId = "";
        private String courseName = "";
        private int credits;
        private String isSelect = "N";
    }

    @Getter
    @Setter
    public static class SelectionMajorDto {
        private String majorId = "";
        private String majorName = "";
        private String departmentName = "";
    }

    @Getter
    @Setter
    public static class CreateSelectionDto {
        private String information = "";
        private LocalDateTime beginTime;
        private LocalDateTime endTime;
        private int type; // 0:随机踢人 1:人满为止
        private int semester; // 0:第一学期 1:第二学期
        private int year; // 学年
        private List<String> courseIds = new ArrayList<>();
        private List<String> majorIds = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class UpdateSelectionDto {
        private String selectionId = "";
        private String information;
        private LocalDateTime beginTime;
        private LocalDateTime endTime;
        private Integer type; // 0:随机踢人 1:人满为止
        private Integer semester; // 0:第一学期 1:第二学期
        private Integer year; // 学年
        private List<String> courseIds;
        private List<String> majorIds;
    }

    // 处理选课结果DTO
    @Getter
    @Setter
    public static class ProcessSelectionResultDto {
        private String selectionId = "";
        private boolean forceProcess = false; // 是否强制处理（即使选课未结束）
    }
}