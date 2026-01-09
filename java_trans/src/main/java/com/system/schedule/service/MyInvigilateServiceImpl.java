package com.system.schedule.service.impl;

import com.system.schedule.dto.myinvigilate.GenerateSeatingReq;
import com.system.schedule.dto.myinvigilate.InvigilateCourseRes;
import com.system.schedule.dto.myinvigilate.SeatInfoRes;
import com.system.schedule.dto.myinvigilate.SeatingChartRes;
import com.system.schedule.dto.other.ApiResult;
import com.system.schedule.service.IMyInvigilateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class MyInvigilateServiceImpl implements IMyInvigilateService {

    private static final Logger logger = LoggerFactory.getLogger(MyInvigilateServiceImpl.class);

    @Override
    public CompletableFuture<List<InvigilateCourseRes>> getInvigilateCourses(String teacherId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("GetInvigilateCourses start, teacherId={}", teacherId);

                // 模拟查询教师教授的课程
                List<CourseMock> courses = mockCourses();
                List<TeachesMock> teaches = mockTeaches();

                // 先查询TEACHES表中是否有该教师的记录
                long teachesCount = teaches.stream()
                        .filter(t -> t.getTeacherId().equals(teacherId))
                        .count();
                logger.info("TEACHES表中教师{}的记录数: {}", teacherId, teachesCount);

                // 查询教师教授的课程
                List<InvigilateCourseRes> result = teaches.stream()
                        .filter(t -> t.getTeacherId().equals(teacherId))
                        .map(t -> {
                            CourseMock course = courses.stream()
                                    .filter(c -> c.getCourseId().equals(t.getCourseId()))
                                    .findFirst()
                                    .orElse(null);

                            InvigilateCourseRes res = new InvigilateCourseRes();
                            res.setCourseId(t.getCourseId());
                            res.setCourseName(course != null ? course.getName() : "未知课程");
                            res.setClassId(t.getClassId());
                            res.setSemester(1); // 固定为第1学期
                            res.setYear(2025); // 固定为2025年
                            res.setStudentCount(50); // 固定50人
                            return res;
                        })
                        .collect(Collectors.toList());

                logger.info("GetInvigilateCourses end, count={}", result.size());
                if (result.isEmpty()) {
                    logger.warn("教师{}没有找到任何监考课程", teacherId);
                }
                return result;
            } catch (Exception ex) {
                logger.error("获取监考课程列表失败, teacherId={}", teacherId, ex);
                throw new RuntimeException("获取监考课程列表失败: " + ex.getMessage());
            }
        });
    }

    @Override
    public CompletableFuture<ApiResult> generateSeatingChart(GenerateSeatingReq req) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("GenerateSeatingChart start, CourseId={}, ClassId={}, Rows={}, Columns={}",
                        req.getCourseId(), req.getClassId(), req.getRows(), req.getColumns());

                // 验证座位数量是否足够
                int totalSeats = req.getRows() * req.getColumns();
                logger.info("座位验证: 总座位={}, 学生数={}", totalSeats, req.getStudentCount());
                if (totalSeats < req.getStudentCount()) {
                    logger.warn("座位数量不足: 总座位={}, 需要={}", totalSeats, req.getStudentCount());
                    return ApiResult.error("座位数量不足，当前设置" + totalSeats + "个座位，需要" + req.getStudentCount() + "个座位");
                }

                // 获取课程信息
                logger.info("查询课程信息, CourseId={}", req.getCourseId());
                CourseMock course = mockCourses().stream()
                        .filter(c -> c.getCourseId().equals(req.getCourseId()))
                        .findFirst()
                        .orElse(null);

                if (course == null) {
                    logger.warn("课程不存在, CourseId={}", req.getCourseId());
                    return ApiResult.error("课程不存在");
                }
                logger.info("找到课程: {} ({})", course.getName(), course.getCourseId());

                // 生成学生姓名列表
                List<String> students = new ArrayList<>();
                for (int i = 1; i <= req.getStudentCount(); i++) {
                    students.add("stu_" + String.format("%02d", i));
                }

                // 打乱学生顺序（随机分配）
                Collections.shuffle(students);

                // 创建座位表二维数组
                SeatInfoRes[][] seats = new SeatInfoRes[req.getRows()][req.getColumns()];

                // 分配座位
                int studentIndex = 0;
                for (int row = 0; row < req.getRows(); row++) {
                    for (int col = 0; col < req.getColumns(); col++) {
                        SeatInfoRes seatInfo = new SeatInfoRes();
                        seatInfo.setRow(row + 1);
                        seatInfo.setColumn(col + 1);
                        
                        if (studentIndex < req.getStudentCount()) {
                            seatInfo.setStudentName(students.get(studentIndex));
                            studentIndex++;
                        } else {
                            seatInfo.setStudentName(""); // 空座位
                        }
                        
                        seats[row][col] = seatInfo;
                    }
                }

                SeatingChartRes seatingChart = new SeatingChartRes();
                seatingChart.setCourseId(req.getCourseId());
                seatingChart.setCourseName(course.getName());
                seatingChart.setClassId(req.getClassId());
                seatingChart.setRows(req.getRows());
                seatingChart.setColumns(req.getColumns());
                seatingChart.setTotalSeats(totalSeats);
                seatingChart.setStudentCount(req.getStudentCount());
                seatingChart.setSeats(seats);

                logger.info("GenerateSeatingChart end, success");
                return ApiResult.success(seatingChart);
            } catch (Exception ex) {
                logger.error("生成座位表失败, CourseId={}", req.getCourseId(), ex);
                return ApiResult.error("生成座位表失败: " + ex.getMessage());
            }
        });
    }

    // 模拟数据类
    private static class CourseMock {
        private String courseId;
        private String name;

        public CourseMock(String courseId, String name) {
            this.courseId = courseId;
            this.name = name;
        }

        public String getCourseId() {
            return courseId;
        }

        public String getName() {
            return name;
        }
    }

    private static class TeachesMock {
        private String teacherId;
        private String courseId;
        private String classId;

        public TeachesMock(String teacherId, String courseId, String classId) {
            this.teacherId = teacherId;
            this.courseId = courseId;
            this.classId = classId;
        }

        public String getTeacherId() {
            return teacherId;
        }

        public String getCourseId() {
            return courseId;
        }

        public String getClassId() {
            return classId;
        }
    }

    // 模拟数据
    private List<CourseMock> mockCourses() {
        List<CourseMock> list = new ArrayList<>();

        list.add(new CourseMock("CS101", "计算机科学导论"));
        list.add(new CourseMock("CS102", "数据结构"));
        list.add(new CourseMock("CS103", "算法设计与分析"));
        list.add(new CourseMock("MA101", "高等数学"));
        list.add(new CourseMock("MA102", "线性代数"));

        return list;
    }

    private List<TeachesMock> mockTeaches() {
        List<TeachesMock> list = new ArrayList<>();

        list.add(new TeachesMock("T001", "CS101", "C001"));
        list.add(new TeachesMock("T001", "CS102", "C002"));
        list.add(new TeachesMock("T002", "MA101", "M001"));
        list.add(new TeachesMock("T003", "MA102", "M002"));

        return list;
    }
}