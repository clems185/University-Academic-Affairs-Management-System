package com.system.schedule.service.impl;

import com.system.schedule.dto.ScheduleItemDto;
import com.system.schedule.dto.semestercourse.CourseInfoDto;
import com.system.schedule.service.ISemesterCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class SemesterCourseServiceImpl implements ISemesterCourseService {

    private static final Logger logger = LoggerFactory.getLogger(SemesterCourseServiceImpl.class);

    @Override
    public CompletableFuture<List<ScheduleItemDto>> getStudentScheduleAsync(String studentId, String year, String semester) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始获取学生 {} 在 {} 学年 {} 学期的课程表", studentId, year, semester);
                
                // 模拟数据
                List<TakesMock> takes = mockTakes();
                List<CourseMock> courses = mockCourses();
                List<TeachesMock> teaches = mockTeaches();
                List<ClassroomMock> classrooms = mockClassrooms();
                List<TimeSlotMock> timeSlots = mockTimeSlots();
                List<TeacherMock> teachers = mockTeachers();
                
                // 获取学生所选课程
                List<String> courseIds = takes.stream()
                        .filter(t -> t.getStudentId().equals(studentId) && 
                                t.getYear().equals(year) && 
                                t.getSemester().equals(semester))
                        .map(TakesMock::getCourseId)
                        .collect(Collectors.toList());
                
                // 获取授课信息
                List<TeachesMock> studentTeaches = teaches.stream()
                        .filter(te -> courseIds.contains(te.getCourseId()) && 
                                te.getYear().equals(year) && 
                                te.getSemester().equals(semester))
                        .collect(Collectors.toList());
                
                // 转换为ScheduleItemDto
                List<ScheduleItemDto> scheduleItems = studentTeaches.stream()
                        .map(te -> {
                            ScheduleItemDto item = new ScheduleItemDto();
                            
                            // 课程信息
                            CourseMock course = courses.stream()
                                    .filter(c -> c.getCourseId().equals(te.getCourseId()))
                                    .findFirst().orElse(null);
                            if (course != null) {
                                item.setCourseId(course.getCourseId());
                                item.setCourseName(course.getName());
                                item.setCredits(course.getCredits());
                            }
                            
                            // 教师信息
                            TeacherMock teacher = teachers.stream()
                                    .filter(t -> t.getId().equals(te.getTeacherId()))
                                    .findFirst().orElse(null);
                            if (teacher != null) {
                                item.setTeacherName(teacher.getName());
                            }
                            
                            // 教室信息
                            ClassroomMock classroom = classrooms.stream()
                                    .filter(cr -> cr.getClassroomId().equals(te.getClassroomId()))
                                    .findFirst().orElse(null);
                            if (classroom != null) {
                                item.setClassroom(classroom.getRoomNumber());
                            }
                            
                            // 时间信息
                            TimeSlotMock timeSlot = timeSlots.stream()
                                    .filter(ts -> ts.getTimeSlotId().equals(te.getTimeSlotId()))
                                    .findFirst().orElse(null);
                            if (timeSlot != null) {
                                item.setDayOfWeek(timeSlot.getDayOfWeek().getValue());
                                item.setStartTime(timeSlot.getStartTime());
                                item.setEndTime(timeSlot.getEndTime());
                                item.setWeekRange(te.getWeekRange());
                            }
                            
                            item.setYear(year);
                            item.setSemester(semester);
                            
                            return item;
                        })
                        .collect(Collectors.toList());
                
                logger.info("成功获取学生 {} 的课程表，共 {} 门课程", studentId, scheduleItems.size());
                return scheduleItems;
                
            } catch (Exception e) {
                logger.error("获取学生 {} 的课程表时发生异常: {}", studentId, e.getMessage(), e);
                return new ArrayList<>();
            }
        });
    }

    @Override
    public CompletableFuture<List<CourseInfoDto>> getStudentCoursesAsync(String studentId, String year, String semester) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("开始获取学生 {} 在 {} 学年 {} 学期的课程列表", studentId, year, semester);
                
                // 模拟数据
                List<TakesMock> takes = mockTakes();
                List<CourseMock> courses = mockCourses();
                List<TeachesMock> teaches = mockTeaches();
                List<TeacherMock> teachers = mockTeachers();
                
                // 获取学生所选课程
                List<String> courseIds = takes.stream()
                        .filter(t -> t.getStudentId().equals(studentId) && 
                                t.getYear().equals(year) && 
                                t.getSemester().equals(semester))
                        .map(TakesMock::getCourseId)
                        .collect(Collectors.toList());
                
                // 按课程分组的授课信息
                Map<String, List<TeachesMock>> courseTeachesMap = teaches.stream()
                        .filter(te -> courseIds.contains(te.getCourseId()) && 
                                te.getYear().equals(year) && 
                                te.getSemester().equals(semester))
                        .collect(Collectors.groupingBy(TeachesMock::getCourseId));
                
                // 转换为CourseInfoDto
                List<CourseInfoDto> courseInfos = courseIds.stream()
                        .map(courseId -> {
                            CourseInfoDto dto = new CourseInfoDto();
                            
                            // 课程信息
                            CourseMock course = courses.stream()
                                    .filter(c -> c.getCourseId().equals(courseId))
                                    .findFirst().orElse(null);
                            if (course != null) {
                                dto.setCourseId(course.getCourseId());
                                dto.setCourseName(course.getName());
                                dto.setCredits(course.getCredits());
                                dto.setCourseCode(course.getCourseCode());
                            }
                            
                            // 教师信息
                            List<TeachesMock> courseTeaches = courseTeachesMap.getOrDefault(courseId, new ArrayList<>());
                            List<String> teacherIds = courseTeaches.stream()
                                    .map(TeachesMock::getTeacherId)
                                    .distinct()
                                    .collect(Collectors.toList());
                            
                            List<String> teacherNames = teachers.stream()
                                    .filter(t -> teacherIds.contains(t.getId()))
                                    .map(TeacherMock::getName)
                                    .collect(Collectors.toList());
                            
                            dto.setTeachers(teacherNames);
                            dto.setYear(year);
                            dto.setSemester(semester);
                            
                            return dto;
                        })
                        .collect(Collectors.toList());
                
                logger.info("成功获取学生 {} 的课程列表，共 {} 门课程", studentId, courseInfos.size());
                return courseInfos;
                
            } catch (Exception e) {
                logger.error("获取学生 {} 的课程列表时发生异常: {}", studentId, e.getMessage(), e);
                return new ArrayList<>();
            }
        });
    }
    
    // 模拟数据方法
    private List<TakesMock> mockTakes() {
        List<TakesMock> takes = new ArrayList<>();
        
        // 为学生2021001创建5门课程
        String[] courseIds = {"CS101", "CS102", "MA101", "PH101", "EN101"};
        for (String courseId : courseIds) {
            TakesMock take = new TakesMock();
            take.setStudentId("2021001");
            take.setCourseId(courseId);
            take.setYear("2024");
            take.setSemester("1");
            takes.add(take);
        }
        
        return takes;
    }
    
    private List<CourseMock> mockCourses() {
        List<CourseMock> courses = new ArrayList<>();
        
        String[] courseIds = {"CS101", "CS102", "MA101", "PH101", "EN101"};
        String[] courseNames = {"计算机基础", "编程入门", "高等数学", "大学物理", "大学英语"};
        String[] courseCodes = {"08090101", "08090102", "07010101", "07020101", "05020101"};
        int[] credits = {2, 4, 4, 3, 2};
        
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
    
    private List<TeachesMock> mockTeaches() {
        List<TeachesMock> teaches = new ArrayList<>();
        
        // 5门课程的授课信息
        String[] courseIds = {"CS101", "CS102", "MA101", "PH101", "EN101"};
        String[] teacherIds = {"T001", "T002", "T003", "T004", "T005"};
        String[] classroomIds = {"CR101", "CR102", "CR201", "CR202", "CR301"};
        String[] timeSlotIds = {"TS1", "TS2", "TS3", "TS4", "TS5"};
        String[] weekRanges = {"1-18", "1-18", "1-18", "1-18", "1-18"};
        
        for (int i = 0; i < courseIds.length; i++) {
            TeachesMock teach = new TeachesMock();
            teach.setCourseId(courseIds[i]);
            teach.setTeacherId(teacherIds[i]);
            teach.setClassroomId(classroomIds[i]);
            teach.setTimeSlotId(timeSlotIds[i]);
            teach.setYear("2024");
            teach.setSemester("1");
            teach.setWeekRange(weekRanges[i]);
            teaches.add(teach);
        }
        
        return teaches;
    }
    
    private List<ClassroomMock> mockClassrooms() {
        List<ClassroomMock> classrooms = new ArrayList<>();
        
        String[] classroomIds = {"CR101", "CR102", "CR201", "CR202", "CR301"};
        String[] roomNumbers = {"101", "102", "201", "202", "301"};
        
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
        
        String[] timeSlotIds = {"TS1", "TS2", "TS3", "TS4", "TS5"};
        DayOfWeek[] daysOfWeek = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY};
        LocalTime[] startTimes = {LocalTime.of(8, 0), LocalTime.of(10, 0), LocalTime.of(14, 0), LocalTime.of(16, 0), LocalTime.of(19, 0)};
        LocalTime[] endTimes = {LocalTime.of(9, 40), LocalTime.of(11, 40), LocalTime.of(15, 40), LocalTime.of(17, 40), LocalTime.of(20, 40)};
        
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
    
    private List<TeacherMock> mockTeachers() {
        List<TeacherMock> teachers = new ArrayList<>();
        
        String[] teacherIds = {"T001", "T002", "T003", "T004", "T005"};
        String[] teacherNames = {"张老师", "李老师", "王老师", "赵老师", "刘老师"};
        
        for (int i = 0; i < teacherIds.length; i++) {
            TeacherMock teacher = new TeacherMock();
            teacher.setId(teacherIds[i]);
            teacher.setName(teacherNames[i]);
            teachers.add(teacher);
        }
        
        return teachers;
    }
    
    // 模拟数据的实体类
    private static class TakesMock {
        private String studentId;
        private String courseId;
        private String year;
        private String semester;
        
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public String getCourseId() { return courseId; }
        public void setCourseId(String courseId) { this.courseId = courseId; }
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
    
    private static class TeachesMock {
        private String courseId;
        private String teacherId;
        private String classroomId;
        private String timeSlotId;
        private String year;
        private String semester;
        private String weekRange;
        
        public String getCourseId() { return courseId; }
        public void setCourseId(String courseId) { this.courseId = courseId; }
        public String getTeacherId() { return teacherId; }
        public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
        public String getClassroomId() { return classroomId; }
        public void setClassroomId(String classroomId) { this.classroomId = classroomId; }
        public String getTimeSlotId() { return timeSlotId; }
        public void setTimeSlotId(String timeSlotId) { this.timeSlotId = timeSlotId; }
        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }
        public String getSemester() { return semester; }
        public void setSemester(String semester) { this.semester = semester; }
        public String getWeekRange() { return weekRange; }
        public void setWeekRange(String weekRange) { this.weekRange = weekRange; }
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
        private DayOfWeek dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;
        
        public String getTimeSlotId() { return timeSlotId; }
        public void setTimeSlotId(String timeSlotId) { this.timeSlotId = timeSlotId; }
        public DayOfWeek getDayOfWeek() { return dayOfWeek; }
        public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
        public LocalTime getStartTime() { return startTime; }
        public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
        public LocalTime getEndTime() { return endTime; }
        public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    }
    
    private static class TeacherMock {
        private String id;
        private String name;
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}