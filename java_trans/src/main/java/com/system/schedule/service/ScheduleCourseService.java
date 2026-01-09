package com.system.schedule.service;

import com.system.schedule.dto.ScheduleCourseDto;
import com.system.schedule.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ScheduleCourseService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // 获取所有课程
    @Transactional(readOnly = true)
    public List<ScheduleCourseDto> getAllCourses() {
        String sql = "SELECT c.class_id AS classId, c.course_id AS courseId, course.name AS courseName, " +
                     "teacher.teacher_name AS teacherName, '待安排' AS location, c.year, c.semester, " +
                     "(SELECT COUNT(*) FROM takes WHERE takes.course_id = c.course_id AND takes.class_id = c.class_id " +
                     "AND CAST(takes.year AS CHAR) = c.year AND CAST(takes.semester AS CHAR) = c.semester) AS selectedCount, " +
                     "c.capacity " +
                     "FROM class c " +
                     "LEFT JOIN course ON c.course_id = course.course_id " +
                     "LEFT JOIN teaches ON c.class_id = teaches.class_id AND c.course_id = teaches.course_id " +
                     "LEFT JOIN teacher ON teaches.teacher_id = teacher.teacher_id";
        
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ScheduleCourseDto.class));
    }
    
    // 排课
    @Transactional
    public List<ScheduleCourseDto> scheduleCourses(String year, String semester, List<String> constraints) {
        // 获取本学期所有课程
        String classSql = "SELECT * FROM class WHERE year = ? AND semester = ?";
        List<Class> classes = jdbcTemplate.query(classSql, 
            new BeanPropertyRowMapper<>(Class.class), year, semester);
        
        // 获取所有教室信息
        List<Classroom> classrooms = jdbcTemplate.query("SELECT * FROM classroom", 
            new BeanPropertyRowMapper<>(Classroom.class));
        
        // 获取所有教师信息
        List<Teacher> teachers = jdbcTemplate.query("SELECT * FROM teacher", 
            new BeanPropertyRowMapper<>(Teacher.class));
        
        // 获取所有课程信息
        List<Course> courses = jdbcTemplate.query("SELECT * FROM course", 
            new BeanPropertyRowMapper<>(Course.class));
        
        // 获取所有授课关系
        String teachesSql = "SELECT * FROM teaches WHERE year = ? AND semester = ?";
        List<Teaches> teaches = jdbcTemplate.query(teachesSql, 
            new BeanPropertyRowMapper<>(Teaches.class), year, semester);
        
        // 获取所有选课关系
        String takesSql = "SELECT * FROM takes WHERE CAST(year AS CHAR) = ? AND CAST(semester AS CHAR) = ?";
        List<Takes> takes = jdbcTemplate.query(takesSql, 
            new BeanPropertyRowMapper<>(Takes.class), year, semester);
        
        // 清空相关表（根据DeleteAll标志）
        boolean deleteAll = true;
        if (deleteAll) {
            jdbcTemplate.update("DELETE FROM takes");
            jdbcTemplate.update("DELETE FROM class_classroom");
            jdbcTemplate.update("DELETE FROM class_time");
        }
        
        // 初始化排课结果
        List<ScheduleCourseDto> result = new ArrayList<>();
        
        // 创建时间槽占用表 (7天×12节课=84个时间槽)
        boolean[][] timeSlotOccupancy = new boolean[7][12];
        
        // 创建教室占用表
        Map<String, boolean[][]> classroomOccupancy = new HashMap<>();
        for (Classroom classroom : classrooms) {
            classroomOccupancy.put(classroom.getClassroomId(), new boolean[7][12]);
        }
        
        // 创建教师占用表
        Map<String, boolean[][]> teacherOccupancy = new HashMap<>();
        for (Teacher teacher : teachers) {
            teacherOccupancy.put(teacher.getTeacherId(), new boolean[7][12]);
        }
        
        // 按课程人数降序排序，优先安排人数多的课程
        classes.sort((c1, c2) -> {
            long count1 = takes.stream()
                .filter(t -> t.getCourseId().equals(c1.getCourseId()) && t.getClassId().equals(c1.getClassId()))
                .count();
            long count2 = takes.stream()
                .filter(t -> t.getCourseId().equals(c2.getCourseId()) && t.getClassId().equals(c2.getClassId()))
                .count();
            return Long.compare(count2, count1); // 降序
        });
        
        // 检查约束条件
        boolean requireConsecutiveClasses = constraints.contains("consecutiveClasses");
        boolean allowSaturday = constraints.contains("allowSaturday");
        boolean allowSunday = constraints.contains("allowSunday");
        
        // 记录课程已安排天数
        Map<String, Set<Integer>> courseScheduledDays = new HashMap<>();
        
        for (Class classItem : classes) {
            Course course = courses.stream()
                .filter(c -> c.getCourseId().equals(classItem.getCourseId()))
                .findFirst()
                .orElse(null);
            if (course == null) continue;
            
            // 获取该班级的教师列表
            List<Teaches> classTeachers = teaches.stream()
                .filter(t -> t.getCourseId().equals(classItem.getCourseId()) && 
                            t.getClassId().equals(classItem.getClassId()))
                .toList();
            
            // 获取选课人数
            long selectedCount = takes.stream()
                .filter(t -> t.getCourseId().equals(classItem.getCourseId()) && 
                            t.getClassId().equals(classItem.getClassId()))
                .count();
            
            // 计算每周上课次数
            int timesPerWeek = course.getTimesPerWeek() / 2;
            
            // 初始化课程已安排天数记录
            String courseKey = classItem.getCourseId() + "-" + classItem.getClassId();
            courseScheduledDays.putIfAbsent(courseKey, new HashSet<>());
            
            // 寻找合适的教室（容量足够）
            List<Classroom> suitableClassrooms = classrooms.stream()
                .filter(c -> c.getCapacity() >= selectedCount)
                .sorted(Comparator.comparingInt(Classroom::getCapacity)) // 容量小的优先
                .toList();
            
            if (suitableClassrooms.isEmpty()) {
                // 没有合适教室
                ScheduleCourseDto dto = new ScheduleCourseDto();
                dto.setClassId(classItem.getClassId());
                dto.setCourseId(classItem.getCourseId());
                dto.setCourseName(course.getName());
                dto.setTeacherName(classTeachers.stream()
                    .map(t -> teachers.stream()
                        .filter(teacher -> teacher.getTeacherId().equals(t.getTeacherId()))
                        .findFirst()
                        .map(Teacher::getTeacherName)
                        .orElse(""))
                    .filter(name -> !name.isEmpty())
                    .reduce((a, b) -> a + "," + b)
                    .orElse(""));
                dto.setLocation("无合适教室");
                dto.setYear(classItem.getYear());
                dto.setSemester(classItem.getSemester());
                dto.setSelectedCount((int) selectedCount);
                dto.setCapacity(classItem.getCapacity());
                dto.setTimeSlots(List.of());
                result.add(dto);
                continue;
            }
            
            // 寻找合适的时间段
            List<String> timeSlots = new ArrayList<>();
            String assignedClassroom = "";
            boolean success = false;
            
            // 尝试不同的时间段组合
            for (int attempt = 0; attempt < 50 && !success; attempt++) {
                timeSlots.clear();
                assignedClassroom = "";
                courseScheduledDays.put(courseKey, new HashSet<>());
                
                for (Classroom classroom : suitableClassrooms) {
                    for (int day = 0; day < 7 && timeSlots.size() < timesPerWeek; day++) {
                        // 检查周六周日约束
                        if (day == 5 && !allowSaturday) continue;
                        if (day == 6 && !allowSunday) continue;
                        
                        // 如果未启用"同一课程连续安排"约束，且该课程已在该天安排过，则跳过
                        if (!requireConsecutiveClasses && courseScheduledDays.get(courseKey).contains(day)) {
                            continue;
                        }
                        
                        // 获取可用时间段
                        List<Integer> availableSlots = getAvailableSlots(day, constraints);
                        
                        for (int slot : availableSlots) {
                            if (timeSlots.size() >= timesPerWeek) break;
                            
                            int nextSlot = slot + 1;
                            
                            // 检查教室是否可用
                            boolean classroomAvailable = !classroomOccupancy.get(classroom.getClassroomId())[day][slot] &&
                                                        !classroomOccupancy.get(classroom.getClassroomId())[day][nextSlot];
                            
                            // 检查教师是否可用
                            boolean teachersAvailable = true;
                            for (Teaches teacher : classTeachers) {
                                if (teacherOccupancy.get(teacher.getTeacherId())[day][slot] ||
                                    teacherOccupancy.get(teacher.getTeacherId())[day][nextSlot]) {
                                    teachersAvailable = false;
                                    break;
                                }
                            }
                            
                            if (classroomAvailable && teachersAvailable) {
                                // 占用资源
                                classroomOccupancy.get(classroom.getClassroomId())[day][slot] = true;
                                classroomOccupancy.get(classroom.getClassroomId())[day][nextSlot] = true;
                                
                                for (Teaches teacher : classTeachers) {
                                    teacherOccupancy.get(teacher.getTeacherId())[day][slot] = true;
                                    teacherOccupancy.get(teacher.getTeacherId())[day][nextSlot] = true;
                                }
                                
                                // 记录时间槽
                                int timeSlotId1 = day * 12 + slot;
                                int timeSlotId2 = day * 12 + nextSlot;
                                timeSlots.add(timeSlotId1 + "," + timeSlotId2);
                                
                                assignedClassroom = classroom.getClassroomId();
                                courseScheduledDays.get(courseKey).add(day);
                                
                                // 如果未启用"同一课程连续安排"约束，则跳过该天剩余的时间段
                                if (!requireConsecutiveClasses) {
                                    break;
                                }
                            }
                        }
                    }
                    
                    if (timeSlots.size() >= timesPerWeek) {
                        success = true;
                        break;
                    }
                }
            }
            
            // 创建课程DTO
            ScheduleCourseDto courseDto = new ScheduleCourseDto();
            courseDto.setClassId(classItem.getClassId());
            courseDto.setCourseId(classItem.getCourseId());
            courseDto.setCourseName(course.getName());
            courseDto.setTeacherName(classTeachers.stream()
                .map(t -> teachers.stream()
                    .filter(teacher -> teacher.getTeacherId().equals(t.getTeacherId()))
                    .findFirst()
                    .map(Teacher::getTeacherName)
                    .orElse(""))
                .filter(name -> !name.isEmpty())
                .reduce((a, b) -> a + "," + b)
                .orElse(""));
            courseDto.setLocation(success ? assignedClassroom : "未安排");
            courseDto.setYear(classItem.getYear());
            courseDto.setSemester(classItem.getSemester());
            courseDto.setSelectedCount((int) selectedCount);
            courseDto.setCapacity(classItem.getCapacity());
            courseDto.setTimeSlots(timeSlots);
            
            // 保存到数据库
            if (success && !timeSlots.isEmpty()) {
                saveScheduleToDatabase(classItem, assignedClassroom, timeSlots, 
                    course.getCourseBeginWeek(), course.getCourseEndWeek());
            }
            
            result.add(courseDto);
        }
        
        return result;
    }
    
    // 获取可用时间段
    private List<Integer> getAvailableSlots(int day, List<String> constraints) {
        List<Integer> availableSlots = new ArrayList<>();
        int[] defaultSlots = {0, 2, 4, 6, 8, 10};
        
        for (int slot : defaultSlots) {
            boolean valid = true;
            
            // 晚上尽可能少排课
            if (constraints.contains("avoidEvening") && slot >= 8) {
                valid = false;
            }
            
            // 周二下午不排课
            if (constraints.contains("tuesdayAfternoonOff") && day == 1 && slot >= 4) {
                valid = false;
            }
            
            // 避免午餐时间排课
            if (constraints.contains("avoidLunchBreak") && (slot == 4 || slot == 5)) {
                valid = false;
            }
            
            if (valid) {
                availableSlots.add(slot);
            }
        }
        
        return availableSlots;
    }
    
    // 保存排课结果到数据库
    private void saveScheduleToDatabase(Class classItem, String classroomId, 
                                       List<String> timeSlots, int startWeek, int endWeek) {
        // 保存到CLASS_CLASSROOM表
        String insertClassroomSql = "INSERT INTO class_classroom (course_id, class_id, semester, year, classroom_id) " +
                                    "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertClassroomSql,
            classItem.getCourseId(),
            classItem.getClassId(),
            classItem.getSemester(),
            classItem.getYear(),
            classroomId);
        
        // 保存到CLASS_TIME表
        // 构建周次数据
        StringBuilder weekValues = new StringBuilder();
        for (int i = 1; i <= 22; i++) {
            weekValues.append("week").append(i).append(", ");
        }
        weekValues.setLength(weekValues.length() - 2); // 移除最后的逗号和空格
        
        StringBuilder placeholders = new StringBuilder();
        for (int i = 1; i <= 22; i++) {
            placeholders.append("?, ");
        }
        placeholders.setLength(placeholders.length() - 2);
        
        String insertTimeSql = "INSERT INTO class_time (course_id, class_id, semester, year, " + weekValues + ") " +
                               "VALUES (?, ?, ?, ?, " + placeholders + ")";
        
        List<Object> params = new ArrayList<>();
        params.add(classItem.getCourseId());
        params.add(classItem.getClassId());
        params.add(classItem.getSemester());
        params.add(classItem.getYear());
        
        // 设置每周的时间槽
        for (int week = 1; week <= 22; week++) {
            if (week >= startWeek && week <= endWeek) {
                // 将所有时间槽合并为一个字符串，用逗号分隔
                params.add(String.join(",", timeSlots));
            } else {
                params.add("");
            }
        }
        
        jdbcTemplate.update(insertTimeSql, params.toArray());
    }
}