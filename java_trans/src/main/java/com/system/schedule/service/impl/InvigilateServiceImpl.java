package com.system.schedule.service.impl;

import com.system.schedule.dto.invigilate.InvigilateDto;
import com.system.schedule.dto.invigilate.SeatArrangementDto;
import com.system.schedule.dto.invigilate.SeatArrangementRequest;
import com.system.schedule.service.IInvigilateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class InvigilateServiceImpl implements IInvigilateService {

    private static final Logger logger = LoggerFactory.getLogger(InvigilateServiceImpl.class);

    @Override
    public CompletableFuture<List<InvigilateDto>> getInvigilateList(String teacherId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始获取教师 {} 的监考列表", teacherId);
                
                // 模拟监考数据
                List<TeacherExamMock> teacherExams = mockTeacherExams();
                List<CourseMock> courses = mockCourses();
                List<ClassroomMock> classrooms = mockClassrooms();
                List<BuildingMock> buildings = mockBuildings();
                List<TimeSlotMock> timeSlots = mockTimeSlots();
                List<TakesMock> takes = mockTakes();
                
                // 关联查询并转换为InvigilateDto
                List<InvigilateDto> invigilateList = teacherExams.stream()
                        .filter(te -> te.getTeacherId().equals(teacherId))
                        .map(te -> {
                            InvigilateDto dto = new InvigilateDto();
                            dto.setCourseId(te.getExam().getCourseId());
                            dto.setSemester(te.getExam().getSemester());
                            dto.setYear(te.getExam().getYear());
                            
                            // 查找课程名称
                            CourseMock course = courses.stream()
                                    .filter(c -> c.getCourseId().equals(te.getExam().getCourseId()))
                                    .findFirst().orElse(null);
                            dto.setCourseName(course != null ? course.getName() : "未知课程");
                            
                            // 查找考试时间
                            TimeSlotMock timeSlot = timeSlots.stream()
                                    .filter(ts -> ts.getTimeSlotId().equals(te.getExam().getTimeSlotId()))
                                    .findFirst().orElse(null);
                            dto.setExamTime(timeSlot != null ? timeSlot.getStartTime() : null);
                            
                            // 查找考试地点
                            ClassroomMock classroom = classrooms.stream()
                                    .filter(cr -> cr.getClassroomId().equals(te.getExam().getClassroomId()))
                                    .findFirst().orElse(null);
                            if (classroom != null) {
                                BuildingMock building = buildings.stream()
                                        .filter(bu -> bu.getBuildingId().equals(classroom.getBuildingId()))
                                        .findFirst().orElse(null);
                                dto.setExamLocation(building != null ? building.getName() + " " + classroom.getRoomNumber() : classroom.getRoomNumber());
                            } else {
                                dto.setExamLocation("未知地点");
                            }
                            
                            // 计算学生人数
                            long studentCount = takes.stream()
                                    .filter(t -> t.getCourseId().equals(dto.getCourseId()) && 
                                            t.getSemester().equals(dto.getSemester()) && 
                                            t.getYear() == dto.getYear())
                                    .count();
                            dto.setStudentCount((int) studentCount);
                            
                            return dto;
                        })
                        .collect(Collectors.toList());
                
                logger.info("为教师 {} 查询到 {} 条监考记录", teacherId, invigilateList.size());
                return invigilateList;
                
            } catch (Exception e) {
                logger.error("获取教师 {} 的监考列表时发生异常: {}", teacherId, e.getMessage(), e);
                return new ArrayList<>();
            }
        });
    }

    @Override
    public CompletableFuture<SeatArrangementDto> generateSeatArrangement(SeatArrangementRequest req) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始为课程 {} (学期: {}, 学年: {}) 进行排座，教室: {}行 {}列", 
                        req.getCourseId(), req.getSemester(), req.getYear(), req.getRows(), req.getCols());
                
                // 模拟选课数据
                List<TakesMock> takes = mockTakes();
                List<StudentMock> students = mockStudents();
                
                // 获取该课程的学生ID列表
                List<String> studentIds = takes.stream()
                        .filter(t -> t.getCourseId().equals(req.getCourseId()) && 
                                t.getSemester().equals(req.getSemester()) && 
                                t.getYear() == req.getYear())
                        .map(TakesMock::getStudentId)
                        .collect(Collectors.toList());
                
                if (studentIds.isEmpty()) {
                    logger.warn("未找到参加该场考试的学生，无法排座");
                    return new SeatArrangementDto();
                }
                
                // 获取学生详细信息
                List<StudentSeatInfo> studentsToArrange = students.stream()
                        .filter(s -> studentIds.contains(s.getId()))
                        .map(s -> new StudentSeatInfo(s.getId(), s.getName()))
                        .collect(Collectors.toList());
                
                // 检查学生人数是否超过教室容量
                int classroomCapacity = req.getRows() * req.getCols();
                if (studentsToArrange.size() > classroomCapacity) {
                    logger.warn("排座失败：学生总数 {} 大于教室容量 {}", studentsToArrange.size(), classroomCapacity);
                    return new SeatArrangementDto();
                }
                
                // Fisher-Yates洗牌算法随机打乱学生顺序
                Collections.shuffle(studentsToArrange, new Random());
                
                // 生成座位表
                List<List<StudentSeatInfo>> seatingPlan = new ArrayList<>();
                int studentIndex = 0;
                
                for (int i = 0; i < req.getRows(); i++) {
                    List<StudentSeatInfo> row = new ArrayList<>();
                    for (int j = 0; j < req.getCols(); j++) {
                        if (studentIndex < studentsToArrange.size()) {
                            StudentSeatInfo student = studentsToArrange.get(studentIndex++);
                            student.setRow(i + 1);
                            student.setCol(j + 1);
                            row.add(student);
                        } else {
                            row.add(null);
                        }
                    }
                    seatingPlan.add(row);
                }
                
                logger.info("成功为课程 {} 生成座位表，共安排 {} 名学生", req.getCourseId(), studentsToArrange.size());
                
                return new SeatArrangementDto(seatingPlan);
                
            } catch (Exception e) {
                logger.error("生成座位安排时发生异常: {}", e.getMessage(), e);
                return new SeatArrangementDto();
            }
        });
    }
    
    // 以下是模拟数据方法
    private List<TeacherExamMock> mockTeacherExams() {
        List<TeacherExamMock> teacherExams = new ArrayList<>();
        
        // 为教师1001创建5条监考记录
        for (int i = 1; i <= 5; i++) {
            TeacherExamMock te = new TeacherExamMock();
            te.setId(String.valueOf(i));
            te.setTeacherId("1001");
            
            ExamMock exam = new ExamMock();
            exam.setExamId("exam" + i);
            exam.setCourseId("course" + i);
            exam.setSemester("2024-1");
            exam.setYear(2024);
            exam.setClassroomId("classroom" + i);
            exam.setTimeSlotId("timeSlot" + i);
            
            te.setExam(exam);
            teacherExams.add(te);
        }
        
        return teacherExams;
    }
    
    private List<CourseMock> mockCourses() {
        List<CourseMock> courses = new ArrayList<>();
        String[] courseNames = {"高等数学", "线性代数", "大学物理", "计算机基础", "数据库系统"};
        
        for (int i = 1; i <= 5; i++) {
            CourseMock course = new CourseMock();
            course.setCourseId("course" + i);
            course.setName(courseNames[i-1]);
            courses.add(course);
        }
        
        return courses;
    }
    
    private List<ClassroomMock> mockClassrooms() {
        List<ClassroomMock> classrooms = new ArrayList<>();
        
        for (int i = 1; i <= 5; i++) {
            ClassroomMock classroom = new ClassroomMock();
            classroom.setClassroomId("classroom" + i);
            classroom.setRoomNumber("30" + i);
            classroom.setBuildingId("building1");
            classrooms.add(classroom);
        }
        
        return classrooms;
    }
    
    private List<BuildingMock> mockBuildings() {
        List<BuildingMock> buildings = new ArrayList<>();
        
        BuildingMock building = new BuildingMock();
        building.setBuildingId("building1");
        building.setName("第一教学楼");
        buildings.add(building);
        
        return buildings;
    }
    
    private List<TimeSlotMock> mockTimeSlots() {
        List<TimeSlotMock> timeSlots = new ArrayList<>();
        
        for (int i = 1; i <= 5; i++) {
            TimeSlotMock timeSlot = new TimeSlotMock();
            timeSlot.setTimeSlotId("timeSlot" + i);
            timeSlot.setStartTime(LocalDateTime.now().plusDays(i).withHour(9).withMinute(0));
            timeSlots.add(timeSlot);
        }
        
        return timeSlots;
    }
    
    private List<TakesMock> mockTakes() {
        List<TakesMock> takes = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 1; i <= 5; i++) {
            // 每个课程随机生成30-50个学生
            int studentCount = 30 + random.nextInt(21);
            for (int j = 1; j <= studentCount; j++) {
                TakesMock take = new TakesMock();
                take.setStudentId("student" + (i * 100 + j));
                take.setCourseId("course" + i);
                take.setSemester("2024-1");
                take.setYear(2024);
                takes.add(take);
            }
        }
        
        return takes;
    }
    
    private List<StudentMock> mockStudents() {
        List<StudentMock> students = new ArrayList<>();
        
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 50; j++) {
                StudentMock student = new StudentMock();
                student.setId("student" + (i * 100 + j));
                student.setName("学生" + (i * 100 + j));
                students.add(student);
            }
        }
        
        return students;
    }
    
    // 模拟数据的实体类
    private static class TeacherExamMock {
        private String id;
        private String teacherId;
        private ExamMock exam;
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getTeacherId() { return teacherId; }
        public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
        public ExamMock getExam() { return exam; }
        public void setExam(ExamMock exam) { this.exam = exam; }
    }
    
    private static class ExamMock {
        private String examId;
        private String courseId;
        private String semester;
        private int year;
        private String classroomId;
        private String timeSlotId;
        
        public String getExamId() { return examId; }
        public void setExamId(String examId) { this.examId = examId; }
        public String getCourseId() { return courseId; }
        public void setCourseId(String courseId) { this.courseId = courseId; }
        public String getSemester() { return semester; }
        public void setSemester(String semester) { this.semester = semester; }
        public int getYear() { return year; }
        public void setYear(int year) { this.year = year; }
        public String getClassroomId() { return classroomId; }
        public void setClassroomId(String classroomId) { this.classroomId = classroomId; }
        public String getTimeSlotId() { return timeSlotId; }
        public void setTimeSlotId(String timeSlotId) { this.timeSlotId = timeSlotId; }
    }
    
    private static class CourseMock {
        private String courseId;
        private String name;
        
        public String getCourseId() { return courseId; }
        public void setCourseId(String courseId) { this.courseId = courseId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
    
    private static class ClassroomMock {
        private String classroomId;
        private String roomNumber;
        private String buildingId;
        
        public String getClassroomId() { return classroomId; }
        public void setClassroomId(String classroomId) { this.classroomId = classroomId; }
        public String getRoomNumber() { return roomNumber; }
        public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
        public String getBuildingId() { return buildingId; }
        public void setBuildingId(String buildingId) { this.buildingId = buildingId; }
    }
    
    private static class BuildingMock {
        private String buildingId;
        private String name;
        
        public String getBuildingId() { return buildingId; }
        public void setBuildingId(String buildingId) { this.buildingId = buildingId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
    
    private static class TimeSlotMock {
        private String timeSlotId;
        private LocalDateTime startTime;
        
        public String getTimeSlotId() { return timeSlotId; }
        public void setTimeSlotId(String timeSlotId) { this.timeSlotId = timeSlotId; }
        public LocalDateTime getStartTime() { return startTime; }
        public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    }
    
    private static class TakesMock {
        private String studentId;
        private String courseId;
        private String semester;
        private int year;
        
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public String getCourseId() { return courseId; }
        public void setCourseId(String courseId) { this.courseId = courseId; }
        public String getSemester() { return semester; }
        public void setSemester(String semester) { this.semester = semester; }
        public int getYear() { return year; }
        public void setYear(int year) { this.year = year; }
    }
    
    private static class StudentMock {
        private String id;
        private String name;
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
    
    private static class StudentSeatInfo {
        private String studentId;
        private String studentName;
        private int row;
        private int col;
        
        public StudentSeatInfo(String studentId, String studentName) {
            this.studentId = studentId;
            this.studentName = studentName;
        }
        
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        public int getRow() { return row; }
        public void setRow(int row) { this.row = row; }
        public int getCol() { return col; }
        public void setCol(int col) { this.col = col; }
    }
}
package com.system.schedule.service.impl;

import com.system.schedule.dto.invigilate.InvigilateDto;
import com.system.schedule.dto.invigilate.SeatArrangementDto;
import com.system.schedule.dto.invigilate.SeatArrangementRequest;
import com.system.schedule.service.IInvigilateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class InvigilateServiceImpl implements IInvigilateService {

    private static final Logger logger = LoggerFactory.getLogger(InvigilateServiceImpl.class);

    @Override
    public CompletableFuture<List<InvigilateDto>> getInvigilateList(String teacherId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 模拟数据 - 教师考试表
                List<TeacherExamMock> teacherExams = mockTeacherExams();
                List<ExamMock> exams = mockExams();
                List<CourseMock> courses = mockCourses();
                List<ClassroomMock> classrooms = mockClassrooms();
                List<BuildingMock> buildings = mockBuildings();
                List<TimeSlotMock> timeSlots = mockTimeSlots();
                List<TakesMock> takes = mockTakes();

                // 筛选该教师的监考信息
                List<InvigilateDto> examInfos = teacherExams.stream()
                        .filter(te -> te.getTeacherId().equals(teacherId))
                        .map(te -> {
                            // 查找考试信息
                            ExamMock exam = exams.stream()
                                    .filter(ex -> ex.getExamId().equals(te.getExamId()))
                                    .findFirst()
                                    .orElse(null);

                            if (exam == null) {
                                return null;
                            }

                            // 查找课程信息
                            CourseMock course = courses.stream()
                                    .filter(co -> co.getCourseId().equals(exam.getCourseId()))
                                    .findFirst()
                                    .orElse(null);

                            // 查找教室信息
                            ClassroomMock classroom = classrooms.stream()
                                    .filter(cr -> cr.getClassroomId().equals(exam.getClassroomId()))
                                    .findFirst()
                                    .orElse(null);

                            // 查找教学楼信息
                            BuildingMock building = buildings.stream()
                                    .filter(bu -> classroom != null && bu.getBuildingId().equals(classroom.getBuildingId()))
                                    .findFirst()
                                    .orElse(null);

                            // 查找时间段信息
                            TimeSlotMock timeSlot = timeSlots.stream()
                                    .filter(ts -> ts.getTimeSlotId().equals(exam.getTimeSlotId()))
                                    .findFirst()
                                    .orElse(null);

                            // 构建结果
                            InvigilateDto dto = new InvigilateDto();
                            dto.setCourseId(exam.getCourseId());
                            dto.setSemester(exam.getSemester());
                            dto.setYear(exam.getYear());
                            dto.setCourseName(course != null ? course.getName() : "未知课程");
                            dto.setExamTime(timeSlot != null ? timeSlot.getStartTime() : "");
                            dto.setExamLocation(building != null && classroom != null 
                                    ? building.getName() + " " + classroom.getRoomNumber() 
                                    : "未知地点");
                            dto.setStudentCount(0);

                            // 计算学生人数
                            long studentCount = takes.stream()
                                    .filter(t -> t.getCourseId().equals(exam.getCourseId()) 
                                            && t.getSemester() == exam.getSemester() 
                                            && t.getYear() == exam.getYear())
                                    .count();
                            dto.setStudentCount((int) studentCount);

                            return dto;
                        })
                        .filter(dto -> dto != null)
                        .collect(Collectors.toList());

                logger.info("为教师 {} 查询到 {} 条监考记录", teacherId, examInfos.size());
                return examInfos;
            } catch (Exception ex) {
                logger.error("获取教师 {} 的监考列表时发生异常", teacherId, ex);
                return new ArrayList<>(); // 出错时返回空列表，防止程序崩溃
            }
        });
    }

    @Override
    public CompletableFuture<SeatArrangementDto> generateSeatArrangement(SeatArrangementRequest req) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("开始为课程 {} (学期: {}, 学年: {}) 进行排座", req.getCourseId(), req.getSemester(), req.getYear());

            try {
                // 模拟数据 - 学生选课记录
                List<TakesMock> takes = mockTakes();
                List<StudentMock> students = mockStudents();

                // 根据课程、学期、学年，找到所有要参加考试的学生ID
                List<String> studentIds = takes.stream()
                        .filter(t -> t.getCourseId().equals(req.getCourseId()) 
                                && t.getSemester() == req.getSemester() 
                                && t.getYear() == req.getYear())
                        .map(TakesMock::getStudentId)
                        .collect(Collectors.toList());

                if (studentIds.isEmpty()) {
                    logger.warn("未找到参加该场考试的学生，无法排座。");
                    return new SeatArrangementDto();
                }

                // 根据学生ID列表，查询这些学生的学号和姓名
                List<StudentSeatInfo> studentSeatInfos = students.stream()
                        .filter(s -> studentIds.contains(s.getId()))
                        .map(s -> new StudentSeatInfo(s.getId(), s.getName()))
                        .collect(Collectors.toList());

                if (studentSeatInfos.size() > (req.getRows() * req.getCols())) {
                    logger.warn("排座失败：学生总数 {} 大于教室容量 {}", studentSeatInfos.size(), req.getRows() * req.getCols());
                    return new SeatArrangementDto();
                }

                // 核心算法：Fisher-Yates洗牌算法，随机打乱学生列表顺序
                Random random = new Random();
                List<StudentSeatInfo> shuffledStudents = new ArrayList<>(studentSeatInfos);
                Collections.shuffle(shuffledStudents, random);

                // 生成座位表，填入被打乱后的学生
                List<List<StudentSeatInfo>> seatingPlan = new ArrayList<>();
                int studentIndex = 0;
                for (int i = 0; i < req.getRows(); i++) {
                    List<StudentSeatInfo> row = new ArrayList<>();
                    for (int j = 0; j < req.getCols(); j++) {
                        if (studentIndex < shuffledStudents.size()) {
                            row.add(shuffledStudents.get(studentIndex++));
                        } else {
                            row.add(null); // 空座位
                        }
                    }
                    seatingPlan.add(row);
                }

                SeatArrangementDto result = new SeatArrangementDto();
                result.setSeatingPlan(seatingPlan);
                logger.info("排座成功，共处理 {} 名学生", studentSeatInfos.size());
                return result;
            } catch (Exception ex) {
                logger.error("排座时发生错误", ex);
                return new SeatArrangementDto();
            }
        });
    }

    // 模拟数据类
    private static class TeacherExamMock {
        private String teacherId;
        private String examId;

        public TeacherExamMock(String teacherId, String examId) {
            this.teacherId = teacherId;
            this.examId = examId;
        }

        public String getTeacherId() {
            return teacherId;
        }

        public String getExamId() {
            return examId;
        }
    }

    private static class ExamMock {
        private String examId;
        private String courseId;
        private String classroomId;
        private String timeSlotId;
        private String semester;
        private int year;

        public ExamMock(String examId, String courseId, String classroomId, String timeSlotId, String semester, int year) {
            this.examId = examId;
            this.courseId = courseId;
            this.classroomId = classroomId;
            this.timeSlotId = timeSlotId;
            this.semester = semester;
            this.year = year;
        }

        public String getExamId() {
            return examId;
        }

        public String getCourseId() {
            return courseId;
        }

        public String getClassroomId() {
            return classroomId;
        }

        public String getTimeSlotId() {
            return timeSlotId;
        }

        public String getSemester() {
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

    private static class ClassroomMock {
        private String classroomId;
        private String buildingId;
        private String roomNumber;

        public ClassroomMock(String classroomId, String buildingId, String roomNumber) {
            this.classroomId = classroomId;
            this.buildingId = buildingId;
            this.roomNumber = roomNumber;
        }

        public String getClassroomId() {
            return classroomId;
        }

        public String getBuildingId() {
            return buildingId;
        }

        public String getRoomNumber() {
            return roomNumber;
        }
    }

    private static class BuildingMock {
        private String buildingId;
        private String name;

        public BuildingMock(String buildingId, String name) {
            this.buildingId = buildingId;
            this.name = name;
        }

        public String getBuildingId() {
            return buildingId;
        }

        public String getName() {
            return name;
        }
    }

    private static class TimeSlotMock {
        private String timeSlotId;
        private String startTime;

        public TimeSlotMock(String timeSlotId, String startTime) {
            this.timeSlotId = timeSlotId;
            this.startTime = startTime;
        }

        public String getTimeSlotId() {
            return timeSlotId;
        }

        public String getStartTime() {
            return startTime;
        }
    }

    private static class TakesMock {
        private String studentId;
        private String courseId;
        private String semester;
        private int year;

        public TakesMock(String studentId, String courseId, String semester, int year) {
            this.studentId = studentId;
            this.courseId = courseId;
            this.semester = semester;
            this.year = year;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getCourseId() {
            return courseId;
        }

        public String getSemester() {
            return semester;
        }

        public int getYear() {
            return year;
        }
    }

    private static class StudentMock {
        private String id;
        private String name;

        public StudentMock(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    // 学生座位信息类
    private static class StudentSeatInfo {
        private String studentId;
        private String studentName;

        public StudentSeatInfo(String studentId, String studentName) {
            this.studentId = studentId;
            this.studentName = studentName;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getStudentName() {
            return studentName;
        }
    }

    // 模拟数据
    private List<TeacherExamMock> mockTeacherExams() {
        List<TeacherExamMock> list = new ArrayList<>();
        list.add(new TeacherExamMock("T001", "E001"));
        list.add(new TeacherExamMock("T001", "E002"));
        list.add(new TeacherExamMock("T002", "E003"));
        return list;
    }

    private List<ExamMock> mockExams() {
        List<ExamMock> list = new ArrayList<>();
        list.add(new ExamMock("E001", "CS101", "C101", "T001", "1", 2025));
        list.add(new ExamMock("E002", "CS102", "C102", "T002", "1", 2025));
        list.add(new ExamMock("E003", "MA101", "C201", "T003", "1", 2025));
        return list;
    }

    private List<CourseMock> mockCourses() {
        List<CourseMock> list = new ArrayList<>();
        list.add(new CourseMock("CS101", "计算机科学导论"));
        list.add(new CourseMock("CS102", "数据结构"));
        list.add(new CourseMock("MA101", "高等数学"));
        return list;
    }

    private List<ClassroomMock> mockClassrooms() {
        List<ClassroomMock> list = new ArrayList<>();
        list.add(new ClassroomMock("C101", "B001", "101"));
        list.add(new ClassroomMock("C102", "B001", "102"));
        list.add(new ClassroomMock("C201", "B002", "201"));
        return list;
    }

    private List<BuildingMock> mockBuildings() {
        List<BuildingMock> list = new ArrayList<>();
        list.add(new BuildingMock("B001", "第一教学楼"));
        list.add(new BuildingMock("B002", "第二教学楼"));
        return list;
    }

    private List<TimeSlotMock> mockTimeSlots() {
        List<TimeSlotMock> list = new ArrayList<>();
        list.add(new TimeSlotMock("T001", "09:00-11:00"));
        list.add(new TimeSlotMock("T002", "14:00-16:00"));
        list.add(new TimeSlotMock("T003", "16:30-18:30"));
        return list;
    }

    private List<TakesMock> mockTakes() {
        List<TakesMock> list = new ArrayList<>();
        // CS101课程的学生
        for (int i = 1; i <= 30; i++) {
            list.add(new TakesMock("202201" + String.format("%02d", i), "CS101", "1", 2025));
        }
        // CS102课程的学生
        for (int i = 1; i <= 25; i++) {
            list.add(new TakesMock("202202" + String.format("%02d", i), "CS102", "1", 2025));
        }
        // MA101课程的学生
        for (int i = 1; i <= 40; i++) {
            list.add(new TakesMock("202203" + String.format("%02d", i), "MA101", "1", 2025));
        }
        return list;
    }

    private List<StudentMock> mockStudents() {
        List<StudentMock> list = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            list.add(new StudentMock("202201" + String.format("%02d", i), "学生" + i));
            list.add(new StudentMock("202202" + String.format("%02d", i), "学生" + (i + 40)));
            list.add(new StudentMock("202203" + String.format("%02d", i), "学生" + (i + 80)));
        }
        return list;
    }
}