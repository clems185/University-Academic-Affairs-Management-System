package com.system.schedule.service.impl;

import com.system.schedule.dto.myteaching.MyTeachingRes;
import com.system.schedule.dto.myteaching.MyTeachingSearchParams;
import com.system.schedule.dto.myteaching.TeachingClassRes;
import com.system.schedule.dto.myteaching.ClassStudentRes;
import com.system.schedule.dto.other.ApiResult;
import com.system.schedule.dto.other.PageResult;
import com.system.schedule.service.IMyTeachingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class MyTeachingServiceImpl implements IMyTeachingService {

    private static final Logger logger = LoggerFactory.getLogger(MyTeachingServiceImpl.class);

    @Override
    public CompletableFuture<ApiResult> getMyTeachingAsync(MyTeachingSearchParams searchParams) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始获取教师 {} 的授课信息，学年: {}, 学期: {}", 
                        searchParams.getTeacherId(), searchParams.getYear(), searchParams.getSemester());
                
                // 模拟数据
                List<TeachesMock> teachesList = mockTeaches();
                List<CourseMock> courses = mockCourses();
                List<ClassroomMock> classrooms = mockClassrooms();
                List<TimeSlotMock> timeSlots = mockTimeSlots();
                List<TakesMock> takesList = mockTakes();
                
                // 过滤数据
                List<TeachesMock> filteredTeaches = teachesList.stream()
                        .filter(teach -> teach.getTeacherId().equals(searchParams.getTeacherId()))
                        .filter(teach -> searchParams.getYear() == null || teach.getYear().equals(searchParams.getYear()))
                        .filter(teach -> searchParams.getSemester() == null || teach.getSemester().equals(searchParams.getSemester()))
                        .collect(Collectors.toList());
                
                // 转换为MyTeachingRes
                List<MyTeachingRes> teachingResList = filteredTeaches.stream()
                        .map(teach -> {
                            MyTeachingRes res = new MyTeachingRes();
                            
                            // 查找课程信息
                            CourseMock course = courses.stream()
                                    .filter(c -> c.getCourseId().equals(teach.getCourseId()))
                                    .findFirst().orElse(null);
                            if (course != null) {
                                res.setCourseId(course.getCourseId());
                                res.setCourseName(course.getName());
                                res.setCourseCode(course.getCourseCode());
                                res.setCredits(course.getCredits());
                            }
                            
                            // 查找教室信息
                            ClassroomMock classroom = classrooms.stream()
                                    .filter(cr -> cr.getClassroomId().equals(teach.getClassroomId()))
                                    .findFirst().orElse(null);
                            if (classroom != null) {
                                res.setClassroom(classroom.getRoomNumber());
                            }
                            
                            // 查找时间信息
                            TimeSlotMock timeSlot = timeSlots.stream()
                                    .filter(ts -> ts.getTimeSlotId().equals(teach.getTimeSlotId()))
                                    .findFirst().orElse(null);
                            if (timeSlot != null) {
                                res.setDayOfWeek(timeSlot.getDayOfWeek());
                                res.setStartTime(timeSlot.getStartTime());
                                res.setEndTime(timeSlot.getEndTime());
                            }
                            
                            // 计算学生人数
                            long studentCount = takesList.stream()
                                    .filter(take -> take.getCourseId().equals(teach.getCourseId()) && 
                                            take.getYear().equals(teach.getYear()) && 
                                            take.getSemester().equals(teach.getSemester()))
                                    .count();
                            res.setStudentCount((int) studentCount);
                            
                            res.setYear(teach.getYear());
                            res.setSemester(teach.getSemester());
                            res.setClassId(teach.getClassId());
                            
                            return res;
                        })
                        .collect(Collectors.toList());
                
                // 分页处理
                int totalCount = teachingResList.size();
                int page = Math.max(searchParams.getPage(), 1);
                int pageSize = Math.max(searchParams.getPageSize(), 10);
                int startIndex = (page - 1) * pageSize;
                int endIndex = Math.min(startIndex + pageSize, totalCount);
                
                List<MyTeachingRes> paginatedList;
                if (startIndex < totalCount) {
                    paginatedList = teachingResList.subList(startIndex, endIndex);
                } else {
                    paginatedList = new ArrayList<>();
                }
                
                PageResult<MyTeachingRes> pageResult = new PageResult<>(paginatedList, totalCount, page, pageSize);
                
                logger.info("获取授课信息成功，共 {} 条记录，当前第 {} 页", totalCount, page);
                return ApiResult.success(pageResult);
                
            } catch (Exception e) {
                logger.error("获取授课信息失败: {}", e.getMessage(), e);
                return ApiResult.error("获取授课信息失败，请稍后重试");
            }
        });
    }

    @Override
    public CompletableFuture<ApiResult> getMyTeachingClassesAsync(MyTeachingSearchParams searchParams) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始获取教师 {} 课程 {} 的班级列表，学年: {}, 学期: {}", 
                        searchParams.getTeacherId(), searchParams.getCourseId(), 
                        searchParams.getYear(), searchParams.getSemester());
                
                // 模拟数据
                List<ClassMock> classes = mockClasses();
                List<TeachesMock> teachesList = mockTeaches();
                
                // 过滤数据
                List<ClassMock> filteredClasses = classes.stream()
                        .filter(clazz -> {
                            // 检查该班级是否由指定教师教授
                            return teachesList.stream()
                                    .anyMatch(teach -> teach.getClassId().equals(clazz.getClassId()) &&
                                            teach.getTeacherId().equals(searchParams.getTeacherId()) &&
                                            teach.getCourseId().equals(searchParams.getCourseId()) &&
                                            (searchParams.getYear() == null || teach.getYear().equals(searchParams.getYear())) &&
                                            (searchParams.getSemester() == null || teach.getSemester().equals(searchParams.getSemester())));
                        })
                        .collect(Collectors.toList());
                
                // 转换为TeachingClassRes
                List<TeachingClassRes> classResList = filteredClasses.stream()
                        .map(clazz -> {
                            TeachingClassRes res = new TeachingClassRes();
                            res.setClassId(clazz.getClassId());
                            res.setClassName(clazz.getClassName());
                            res.setStudentCount(clazz.getStudentCount());
                            res.setYear(clazz.getYear());
                            res.setSemester(clazz.getSemester());
                            return res;
                        })
                        .collect(Collectors.toList());
                
                logger.info("获取班级列表成功，共 {} 个班级", classResList.size());
                return ApiResult.success(classResList);
                
            } catch (Exception e) {
                logger.error("获取班级列表失败: {}", e.getMessage(), e);
                return ApiResult.error("获取班级列表失败，请稍后重试");
            }
        });
    }

    @Override
    public CompletableFuture<ApiResult> getClassStudentsAsync(MyTeachingSearchParams searchParams) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始获取班级 {} 的学生信息", searchParams.getClassId());
                
                // 模拟数据
                List<StudentMock> students = mockStudents();
                List<TakesMock> takesList = mockTakes();
                
                // 过滤数据
                List<String> studentIds = takesList.stream()
                        .filter(take -> take.getClassId().equals(searchParams.getClassId()))
                        .map(TakesMock::getStudentId)
                        .collect(Collectors.toList());
                
                // 转换为ClassStudentRes
                List<ClassStudentRes> studentResList = students.stream()
                        .filter(student -> studentIds.contains(student.getId()))
                        .map(student -> {
                            ClassStudentRes res = new ClassStudentRes();
                            res.setStudentId(student.getId());
                            res.setStudentName(student.getName());
                            res.setMajor(student.getMajor());
                            res.setGrade(student.getGrade());
                            res.setClassId(student.getClassId());
                            return res;
                        })
                        .collect(Collectors.toList());
                
                logger.info("获取学生信息成功，共 {} 名学生", studentResList.size());
                return ApiResult.success(studentResList);
                
            } catch (Exception e) {
                logger.error("获取学生信息失败: {}", e.getMessage(), e);
                return ApiResult.error("获取学生信息失败，请稍后重试");
            }
        });
    }
    
    // 模拟数据方法
    private List<TeachesMock> mockTeaches() {
        List<TeachesMock> teachesList = new ArrayList<>();
        
        String[] teacherIds = {"T001", "T002"};
        String[] courseIds = {"CS101", "CS102", "MA101", "PH101"};
        String[] classIds = {"Class001", "Class002", "Class003", "Class004"};
        String[] classroomIds = {"CR101", "CR102", "CR201", "CR202"};
        String[] timeSlotIds = {"TS1", "TS2", "TS3", "TS4"};
        String[] years = {"2023", "2024"};
        String[] semesters = {"1", "2"};
        
        for (int i = 0; i < 8; i++) {
            TeachesMock teach = new TeachesMock();
            teach.setId(String.valueOf(i + 1));
            teach.setTeacherId(teacherIds[i % 2]);
            teach.setCourseId(courseIds[i % 4]);
            teach.setClassId(classIds[i % 4]);
            teach.setClassroomId(classroomIds[i % 4]);
            teach.setTimeSlotId(timeSlotIds[i % 4]);
            teach.setYear(years[i % 2]);
            teach.setSemester(semesters[i % 2]);
            teachesList.add(teach);
        }
        
        return teachesList;
    }
    
    private List<CourseMock> mockCourses() {
        List<CourseMock> courses = new ArrayList<>();
        
        String[] courseIds = {"CS101", "CS102", "MA101", "PH101"};
        String[] courseNames = {"计算机基础", "编程入门", "高等数学", "大学物理"};
        String[] courseCodes = {"08090101", "08090102", "07010101", "07020101"};
        int[] credits = {2, 4, 4, 3};
        
        for (int i = 0; i < courseIds.length; i++) {
            CourseMock course = new CourseMock();
            course.setCourseId(courseIds[i]);
            course.setName(courseNames[i]);
            course.setCourseCode(courseCodes[i]);
            course.setCredits(credits[i]);
            courses.add(course);
        }
        
        return courses;
    }
    
    private List<ClassroomMock> mockClassrooms() {
        List<ClassroomMock> classrooms = new ArrayList<>();
        
        String[] classroomIds = {"CR101", "CR102", "CR201", "CR202"};
        String[] roomNumbers = {"101", "102", "201", "202"};
        
        for (int i = 0; i < classroomIds.length; i++) {
            ClassroomMock classroom = new ClassroomMock();
            classroom.setClassroomId(classroomIds[i]);
            classroom.setRoomNumber(roomNumbers[i]);
            classrooms.add(classroom);
        }
        
        return classrooms;
    }
    
    private List<TimeSlotMock> mockTimeSlots() {
        List<TimeSlotMock> timeSlots = new ArrayList<>();
        
        String[] timeSlotIds = {"TS1", "TS2", "TS3", "TS4"};
        int[] daysOfWeek = {1, 2, 3, 4}; // 周一到周四
        String[] startTimes = {"08:00", "10:00", "14:00", "16:00"};
        String[] endTimes = {"09:40", "11:40", "15:40", "17:40"};
        
        for (int i = 0; i < timeSlotIds.length; i++) {
            TimeSlotMock timeSlot = new TimeSlotMock();
            timeSlot.setTimeSlotId(timeSlotIds[i]);
            timeSlot.setDayOfWeek(daysOfWeek[i]);
            timeSlot.setStartTime(startTimes[i]);
            timeSlot.setEndTime(endTimes[i]);
            timeSlots.add(timeSlot);
        }
        
        return timeSlots;
    }
    
    private List<TakesMock> mockTakes() {
        List<TakesMock> takesList = new ArrayList<>();
        
        String[] classIds = {"Class001", "Class002", "Class003", "Class004"};
        
        for (int i = 0; i < 4; i++) {
            // 每个班级30-40个学生
            int studentCount = 30 + new Random().nextInt(11);
            for (int j = 0; j < studentCount; j++) {
                TakesMock take = new TakesMock();
                take.setStudentId("S" + (i * 100 + j));
                take.setClassId(classIds[i]);
                take.setCourseId("CS" + (101 + i));
                take.setYear("2024");
                take.setSemester("1");
                takesList.add(take);
            }
        }
        
        return takesList;
    }
    
    private List<ClassMock> mockClasses() {
        List<ClassMock> classes = new ArrayList<>();
        
        String[] classIds = {"Class001", "Class002", "Class003", "Class004"};
        String[] classNames = {"计科2001", "计科2002", "软工2001", "软工2002"};
        int[] studentCounts = {35, 32, 38, 30};
        
        for (int i = 0; i < classIds.length; i++) {
            ClassMock clazz = new ClassMock();
            clazz.setClassId(classIds[i]);
            clazz.setClassName(classNames[i]);
            clazz.setStudentCount(studentCounts[i]);
            clazz.setYear("2024");
            clazz.setSemester("1");
            classes.add(clazz);
        }
        
        return classes;
    }
    
    private List<StudentMock> mockStudents() {
        List<StudentMock> students = new ArrayList<>();
        
        String[] classIds = {"Class001", "Class002", "Class003", "Class004"};
        String[] majors = {"计算机科学与技术", "计算机科学与技术", "软件工程", "软件工程"};
        
        for (int i = 0; i < 4; i++) {
            int studentCount = 30 + new Random().nextInt(11);
            for (int j = 0; j < studentCount; j++) {
                StudentMock student = new StudentMock();
                student.setId("S" + (i * 100 + j));
                student.setName("学生" + (i * 100 + j));
                student.setMajor(majors[i]);
                student.setGrade(2020);
                student.setClassId(classIds[i]);
                students.add(student);
            }
        }
        
        return students;
    }
    
    // 模拟数据的实体类
    private static class TeachesMock {
        private String id;
        private String teacherId;
        private String courseId;
        private String classId;
        private String classroomId;
        private String timeSlotId;
        private String year;
        private String semester;
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getTeacherId() { return teacherId; }
        public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
        public String getCourseId() { return courseId; }
        public void setCourseId(String courseId) { this.courseId = courseId; }
        public String getClassId() { return classId; }
        public void setClassId(String classId) { this.classId = classId; }
        public String getClassroomId() { return classroomId; }
        public void setClassroomId(String classroomId) { this.classroomId = classroomId; }
        public String getTimeSlotId() { return timeSlotId; }
        public void setTimeSlotId(String timeSlotId) { this.timeSlotId = timeSlotId; }
        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }
        public String getSemester() { return semester; }
        public void setSemester(String semester) { this.semester = semester; }
    }
    
    private static class CourseMock {
        private String courseId;
        private String name;
        private String courseCode;
        private int credits;
        
        public String getCourseId() { return courseId; }
        public void setCourseId(String courseId) { this.courseId = courseId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
        public int getCredits() { return credits; }
        public void setCredits(int credits) { this.credits = credits; }
    }
    
    private static class ClassroomMock {
        private String classroomId;
        private String roomNumber;
        
        public String getClassroomId() { return classroomId; }
        public void setClassroomId(String classroomId) { this.classroomId = classroomId; }
        public String getRoomNumber() { return roomNumber; }
        public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    }
    
    private static class TimeSlotMock {
        private String timeSlotId;
        private int dayOfWeek;
        private String startTime;
        private String endTime;
        
        public String getTimeSlotId() { return timeSlotId; }
        public void setTimeSlotId(String timeSlotId) { this.timeSlotId = timeSlotId; }
        public int getDayOfWeek() { return dayOfWeek; }
        public void setDayOfWeek(int dayOfWeek) { this.dayOfWeek = dayOfWeek; }
        public String getStartTime() { return startTime; }
        public void setStartTime(String startTime) { this.startTime = startTime; }
        public String getEndTime() { return endTime; }
        public void setEndTime(String endTime) { this.endTime = endTime; }
    }
    
    private static class TakesMock {
        private String studentId;
        private String classId;
        private String courseId;
        private String year;
        private String semester;
        
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public String getClassId() { return classId; }
        public void setClassId(String classId) { this.classId = classId; }
        public String getCourseId() { return courseId; }
        public void setCourseId(String courseId) { this.courseId = courseId; }
        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }
        public String getSemester() { return semester; }
        public void setSemester(String semester) { this.semester = semester; }
    }
    
    private static class ClassMock {
        private String classId;
        private String className;
        private int studentCount;
        private String year;
        private String semester;
        
        public String getClassId() { return classId; }
        public void setClassId(String classId) { this.classId = classId; }
        public String getClassName() { return className; }
        public void setClassName(String className) { this.className = className; }
        public int getStudentCount() { return studentCount; }
        public void setStudentCount(int studentCount) { this.studentCount = studentCount; }
        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }
        public String getSemester() { return semester; }
        public void setSemester(String semester) { this.semester = semester; }
    }
    
    private static class StudentMock {
        private String id;
        private String name;
        private String major;
        private int grade;
        private String classId;
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getMajor() { return major; }
        public void setMajor(String major) { this.major = major; }
        public int getGrade() { return grade; }
        public void setGrade(int grade) { this.grade = grade; }
        public String getClassId() { return classId; }
        public void setClassId(String classId) { this.classId = classId; }
    }
}