package com.system.schedule.service.impl;

import com.system.schedule.dto.myexam.MyExamRes;
import com.system.schedule.service.IMyExamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class MyExamServiceImpl implements IMyExamService {

    private static final Logger logger = LoggerFactory.getLogger(MyExamServiceImpl.class);

    @Override
    public CompletableFuture<List<MyExamRes>> getMyExams(String studentId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("═══════════════════════════════════════");
                logger.info("GetMyExams 开始执行");
                logger.info("参数 studentId = '{}' (长度: {})",
                        studentId, studentId != null ? studentId.length() : 0);
                logger.info("当前时间: {}", new java.util.Date());
                logger.info("═══════════════════════════════════════");

                if (studentId == null || studentId.trim().isEmpty()) {
                    logger.error("错误：studentId 为空或空白");
                    throw new IllegalArgumentException("学生ID不能为空");
                }

                // 模拟查询学生选修的课程
                logger.info("正在查询学生 {} 的选课记录...", studentId);
                List<TakesMock> studentTakes = mockStudentTakes(studentId);

                logger.info("学生 {} 选课查询结果:", studentId);
                logger.info("查询到的记录数: {}", studentTakes.size());

                if (studentTakes.isEmpty()) {
                    logger.warn("⚠️ 没有找到学生 {} 的选课记录", studentId);
                    logger.warn("建议检查: 1. Takes表中是否有该学生的记录");
                    logger.warn("         2. StudentId字段值是否正确");
                    logger.warn("         3. StudentId字段是否有空格");
                    return new ArrayList<>();
                }

                for (TakesMock take : studentTakes) {
                    logger.info("课程ID: '{}'", take.getCourseId());
                }

                List<String> courseIds = studentTakes.stream()
                        .map(TakesMock::getCourseId)
                        .collect(Collectors.toList());

                logger.info("提取的课程ID列表: [{}]",
                        String.join(", ", courseIds.stream().map(id -> "'" + id + "'").collect(Collectors.toList())));

                // 模拟查询考试信息
                logger.info("═══════════════════════════════════════");
                logger.info("正在查询考试信息...");

                List<ExamMock> exams = mockExams();
                List<CourseMock> courses = mockCourses();

                // 执行连接查询逻辑
                List<MyExamRes> result = exams.stream()
                        .filter(exam -> courseIds.contains(exam.getCourseId()))
                        .map(exam -> {
                            CourseMock course = courses.stream()
                                    .filter(c -> c.getCourseId().equals(exam.getCourseId()))
                                    .findFirst()
                                    .orElse(null);

                            MyExamRes myExamRes = new MyExamRes();
                            myExamRes.setExamId(exam.getExamId());
                            myExamRes.setCourseId(exam.getCourseId());
                            myExamRes.setCourseName(course != null ? course.getName() : "未知课程");
                            myExamRes.setTimeSlotId(exam.getTimeSlotId());
                            myExamRes.setClassroomId(exam.getClassroomId());
                            myExamRes.setSemester(exam.getSemester());
                            myExamRes.setYear(exam.getYear());
                            return myExamRes;
                        })
                        .collect(Collectors.toList());

                logger.info("考试查询结果:");
                logger.info("找到考试数量: {}", result.size());

                if (result.isEmpty()) {
                    logger.warn("⚠️ 没有找到相关课程的考试信息");
                    logger.warn("可能原因: 1. 这些课程没有安排考试");
                    logger.warn("         2. Exams表中没有这些课程的记录");
                    logger.warn("         3. 课程ID匹配有问题（检查空格）");
                } else {
                    for (MyExamRes exam : result) {
                        logger.info("────────────────────────────────────");
                        logger.info("考试ID: '{}'", exam.getExamId());
                        logger.info("课程ID: '{}'", exam.getCourseId());
                        logger.info("课程名称: '{}'", exam.getCourseName());
                        logger.info("时间段ID: '{}'", exam.getTimeSlotId());
                        logger.info("教室ID: '{}'", exam.getClassroomId());
                        logger.info("学期: '{}'", exam.getSemester());
                        logger.info("年份: {}", exam.getYear());
                    }
                }

                logger.info("═══════════════════════════════════════");
                logger.info("GetMyExams 执行完成，返回 {} 条记录", result.size());
                logger.info("═══════════════════════════════════════");

                return result;
            } catch (Exception ex) {
                logger.error("❌❌❌ 获取学生考试列表失败 ❌❌❌");
                logger.error("错误类型: {}", ex.getClass().getName());
                logger.error("错误消息: {}", ex.getMessage());
                logger.error("堆栈跟踪: {}", ex.getStackTrace());

                if (ex.getCause() != null) {
                    logger.error("内部异常: {}", ex.getCause().getMessage());
                }

                throw new RuntimeException("获取学生考试列表失败: " + ex.getMessage(), ex);
            }
        });
    }

    // 模拟数据类
    private static class TakesMock {
        private String studentId;
        private String courseId;

        public TakesMock(String studentId, String courseId) {
            this.studentId = studentId;
            this.courseId = courseId;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getCourseId() {
            return courseId;
        }
    }

    private static class ExamMock {
        private String examId;
        private String courseId;
        private String timeSlotId;
        private String classroomId;
        private int semester;
        private int year;

        public ExamMock(String examId, String courseId, String timeSlotId, String classroomId, int semester, int year) {
            this.examId = examId;
            this.courseId = courseId;
            this.timeSlotId = timeSlotId;
            this.classroomId = classroomId;
            this.semester = semester;
            this.year = year;
        }

        public String getExamId() {
            return examId;
        }

        public String getCourseId() {
            return courseId;
        }

        public String getTimeSlotId() {
            return timeSlotId;
        }

        public String getClassroomId() {
            return classroomId;
        }

        public int getSemester() {
            return semester;
        }

        public int getYear() {
            return year;
        }
    }

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

    // 模拟数据
    private List<TakesMock> mockStudentTakes(String studentId) {
        List<TakesMock> list = new ArrayList<>();
        
        if ("20220101".equals(studentId)) {
            list.add(new TakesMock(studentId, "CS101"));
            list.add(new TakesMock(studentId, "CS102"));
            list.add(new TakesMock(studentId, "CS103"));
        } else if ("20220102".equals(studentId)) {
            list.add(new TakesMock(studentId, "MA101"));
            list.add(new TakesMock(studentId, "MA102"));
        }

        return list;
    }

    private List<ExamMock> mockExams() {
        List<ExamMock> list = new ArrayList<>();

        list.add(new ExamMock("E001", "CS101", "T001", "C101", 1, 2025));
        list.add(new ExamMock("E002", "CS102", "T002", "C102", 1, 2025));
        list.add(new ExamMock("E003", "CS103", "T003", "C103", 1, 2025));
        list.add(new ExamMock("E004", "MA101", "T004", "C201", 1, 2025));
        list.add(new ExamMock("E005", "MA102", "T005", "C202", 1, 2025));

        return list;
    }

    private List<CourseMock> mockCourses() {
        List<CourseMock> list = new ArrayList<>();

        list.add(new CourseMock("CS101", "计算机科学导论"));
        list.add(new CourseMock("CS102", "数据结构"));
        list.add(new CourseMock("CS103", "算法设计与分析"));
        list.add(new CourseMock("MA101", "高等数学"));
        list.add(new CourseMock("MA102", "线性代数"));

        return list;
    }
}