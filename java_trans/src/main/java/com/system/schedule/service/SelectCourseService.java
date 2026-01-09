package com.system.schedule.service;

import com.system.schedule.dto.*;
import com.system.schedule.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SelectCourseService {
    
    private static final Logger logger = LoggerFactory.getLogger(SelectCourseService.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // 获取当前学期
    private Map<String, String> getCurrentSemester() {
        Map<String, String> semester = new HashMap<>();
        semester.put("year", "2025");
        semester.put("semester", "0");
        return semester;
    }
    
    // 获取可用的选课轮次
    @Transactional(readOnly = true)
    public List<SelectionRoundDto> getAvailableSelectionRounds(String studentId) {
        // 获取学生专业
        String majorSql = "SELECT major_id FROM student WHERE id = ?";
        String majorId = jdbcTemplate.queryForObject(majorSql, String.class, studentId);
        
        if (majorId == null || majorId.isEmpty()) {
            return new ArrayList<>();
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        String sql = "SELECT s.selection_id AS selectionId, s.information, s.begin_time AS beginTime, " +
                     "s.end_time AS endTime, s.type, s.semester, s.year, " +
                     "CASE WHEN s.begin_time <= ? AND s.end_time >= ? THEN true ELSE false END AS isActive " +
                     "FROM selection s " +
                     "INNER JOIN selection_major sm ON s.selection_id = sm.selection_id " +
                     "WHERE sm.major_id = ? AND s.begin_time <= ? AND s.end_time >= ?";
        
        return jdbcTemplate.query(sql, 
            new BeanPropertyRowMapper<>(SelectionRoundDto.class),
            now, now, majorId, now, now);
    }
    
    // 获取学生已选课程
    @Transactional(readOnly = true)
    public List<SelectCourseDto> getStudentCourses(String studentId, String selectionId) {
        Map<String, String> currentSemester = getCurrentSemester();
        String currentYear = currentSemester.get("year");
        String currentSemesterStr = currentSemester.get("semester");
        
        // 构建查询学生选课记录的SQL
        StringBuilder sql = new StringBuilder(
            "SELECT sc.course_id AS courseId, sc.class_id AS classId, sc.status, " +
            "c.name AS courseName, " +
            "t.teacher_name AS teacherName, " +
            "CONCAT(campus.name, '-', b.name, '-', cr.room_number) AS location, " +
            "cl.capacity, " +
            "(SELECT COUNT(*) FROM student_course WHERE course_id = sc.course_id AND class_id = sc.class_id " +
            "AND status IN ('P', 'C')) AS selectedCount " +
            "FROM student_course sc " +
            "LEFT JOIN course c ON sc.course_id = c.course_id " +
            "LEFT JOIN class cl ON sc.class_id = cl.class_id AND sc.course_id = cl.course_id " +
            "AND cl.year = ? AND cl.semester = ? " +
            "LEFT JOIN teaches tch ON sc.class_id = tch.class_id AND sc.course_id = tch.course_id " +
            "AND tch.year = ? AND tch.semester = ? " +
            "LEFT JOIN teacher t ON tch.teacher_id = t.teacher_id " +
            "LEFT JOIN class_classroom ccr ON sc.class_id = ccr.class_id AND sc.course_id = ccr.course_id " +
            "AND ccr.year = ? AND ccr.semester = ? " +
            "LEFT JOIN classroom cr ON ccr.classroom_id = cr.classroom_id " +
            "LEFT JOIN building b ON cr.building_id = b.building_id " +
            "LEFT JOIN campus ON b.c_id = campus.c_id " +
            "WHERE sc.student_id = ?"
        );
        
        List<Object> params = new ArrayList<>();
        params.add(currentYear);
        params.add(currentSemesterStr);
        params.add(currentYear);
        params.add(currentSemesterStr);
        params.add(currentYear);
        params.add(currentSemesterStr);
        params.add(studentId);
        
        // 如果指定了选课轮次，则只查询该轮次的课程
        if (selectionId != null && !selectionId.isEmpty()) {
            // 获取该选课轮次对应的课程ID列表
            String selectionCourseSql = "SELECT course_id FROM selection_course WHERE selection_id = ?";
            List<String> courseIds = jdbcTemplate.queryForList(selectionCourseSql, String.class, selectionId);
            
            if (!courseIds.isEmpty()) {
                sql.append(" AND sc.course_id IN (");
                for (int i = 0; i < courseIds.size(); i++) {
                    sql.append("?");
                    if (i < courseIds.size() - 1) {
                        sql.append(", ");
                    }
                }
                sql.append(")");
                params.addAll(courseIds);
            }
        }
        
        List<SelectCourseDto> courses = jdbcTemplate.query(sql.toString(),
            new BeanPropertyRowMapper<>(SelectCourseDto.class),
            params.toArray());
        
        // 为每个课程添加时间段信息
        for (SelectCourseDto course : courses) {
            if (!"0".equals(course.getClassId())) {
                course.setTimeSlotInfo(getClassTimeSlots(course.getCourseId(), 
                    course.getClassId(), currentYear, currentSemesterStr));
            }
        }
        
        return courses;
    }
    
    // 获取可选课程
    @Transactional(readOnly = true)
    public List<SelectCourseDto> getAvailableCourses(String studentId, String selectionId) {
        // 获取学生已选课程ID
        String selectedSql = "SELECT course_id FROM student_course WHERE student_id = ?";
        List<String> selectedCourseIds = jdbcTemplate.queryForList(selectedSql, String.class, studentId);
        
        // 获取选课轮次对应的课程ID
        String selectionSql = "SELECT course_id FROM selection_course WHERE selection_id = ?";
        List<String> selectionCourseIds = jdbcTemplate.queryForList(selectionSql, String.class, selectionId);
        
        // 构建查询条件
        List<String> availableCourseIds = selectionCourseIds.stream()
            .filter(id -> !selectedCourseIds.contains(id))
            .collect(Collectors.toList());
        
        if (availableCourseIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        String inClause = String.join(",", Collections.nCopies(availableCourseIds.size(), "?"));
        String sql = "SELECT course_id AS courseId, name AS courseName, '0' AS classId, " +
                     "'N' AS status, 0 AS selectedCount, 0 AS capacity " +
                     "FROM course WHERE course_id IN (" + inClause + ")";
        
        return jdbcTemplate.query(sql,
            new BeanPropertyRowMapper<>(SelectCourseDto.class),
            availableCourseIds.toArray());
    }
    
    // 根据课程获取班级列表
    @Transactional(readOnly = true)
    public List<ClassInfoDto> getClassesByCourse(String courseId, String studentId) {
        Map<String, String> currentSemester = getCurrentSemester();
        String currentYear = currentSemester.get("year");
        String currentSemesterStr = currentSemester.get("semester");
        
        String sql = "SELECT c.class_id AS classId, c.course_id AS courseId, course.name AS courseName, " +
                     "t.teacher_name AS teacherName, " +
                     "CONCAT(campus.name, '-', b.name, '-', cr.room_number) AS location, " +
                     "c.capacity, " +
                     "(SELECT COUNT(*) FROM student_course WHERE course_id = c.course_id AND class_id = c.class_id " +
                     "AND status IN ('P', 'C')) AS selectedCount " +
                     "FROM class c " +
                     "INNER JOIN course ON c.course_id = course.course_id " +
                     "LEFT JOIN teaches tch ON c.class_id = tch.class_id AND c.course_id = tch.course_id " +
                     "AND tch.year = ? AND tch.semester = ? " +
                     "LEFT JOIN teacher t ON tch.teacher_id = t.teacher_id " +
                     "LEFT JOIN class_classroom ccr ON c.class_id = ccr.class_id AND c.course_id = ccr.course_id " +
                     "AND ccr.year = ? AND ccr.semester = ? " +
                     "LEFT JOIN classroom cr ON ccr.classroom_id = cr.classroom_id " +
                     "LEFT JOIN building b ON cr.building_id = b.building_id " +
                     "LEFT JOIN campus ON b.c_id = campus.c_id " +
                     "WHERE c.course_id = ? AND c.year = ? AND c.semester = ?";
        
        List<ClassInfoDto> classes = jdbcTemplate.query(sql,
            new BeanPropertyRowMapper<>(ClassInfoDto.class),
            currentYear, currentSemesterStr, currentYear, currentSemesterStr, 
            courseId, currentYear, currentSemesterStr);
        
        // 为每个班级添加时间段信息
        for (ClassInfoDto classInfo : classes) {
            classInfo.setTimeSlotInfo(getClassTimeSlots(courseId, classInfo.getClassId(), 
                currentYear, currentSemesterStr));
        }
        
        return classes;
    }
    
    // 添加课程（未选择班级）
    @Transactional
    public boolean addCourse(String studentId, String courseId, String selectionId) {
        // 检查是否已添加
        String checkSql = "SELECT COUNT(*) FROM student_course WHERE student_id = ? AND course_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, studentId, courseId);
        if (count != null && count > 0) {
            return true;
        }
        
        // 插入新记录
        String insertSql = "INSERT INTO student_course (student_id, course_id, class_id, status) VALUES (?, ?, '0', 'N')";
        int result = jdbcTemplate.update(insertSql, studentId, courseId);
        return result > 0;
    }
    
    // 选择班级
    @Transactional
    public boolean selectClass(String studentId, String courseId, String classId) {
        logger.info("SelectClass called with studentId: {}, courseId: {}, classId: {}", studentId, courseId, classId);
        Map<String, String> currentSemester = getCurrentSemester();
        String currentYear = currentSemester.get("year");
        String currentSemesterStr = currentSemester.get("semester");
        
        // 检查时间冲突
        boolean timeConflict = checkTimeConflict(studentId, courseId, classId, currentYear, currentSemesterStr);
        if (timeConflict) {
            logger.warn("时间冲突 detected for student {}, course {}, class {}", studentId, courseId, classId);
            throw new RuntimeException("该班级上课时间与已选课程冲突，请选择其他班级");
        }
        
        // 检查班级容量
        String classSql = "SELECT capacity FROM class WHERE class_id = ? AND course_id = ? AND year = ? AND semester = ?";
        Integer capacity = jdbcTemplate.queryForObject(classSql, Integer.class, 
            classId, courseId, currentYear, currentSemesterStr);
        
        if (capacity == null) {
            logger.warn("Class not found: course {}, class {}", courseId, classId);
            return false;
        }
        
        String countSql = "SELECT COUNT(*) FROM student_course WHERE class_id = ? AND course_id = ? AND status IN ('P', 'C')";
        Integer currentCount = jdbcTemplate.queryForObject(countSql, Integer.class, classId, courseId);
        currentCount = currentCount != null ? currentCount : 0;
        
        if (currentCount >= capacity) {
            logger.warn("Class full: course {}, class {}, capacity {}, current {}", courseId, classId, capacity, currentCount);
            throw new RuntimeException("该班级已满，请选择其他班级");
        }
        
        // 检查是否已存在记录
        String existSql = "SELECT * FROM student_course WHERE student_id = ? AND course_id = ?";
        List<StudentCourse> existingRecords = jdbcTemplate.query(existSql, 
            new BeanPropertyRowMapper<>(StudentCourse.class), studentId, courseId);
        
        if (!existingRecords.isEmpty()) {
            // 更新现有记录
            logger.info("Updating existing record: StudentId={}, CourseId={}, NewClassId={}", 
                studentId, courseId, classId);
            String updateSql = "UPDATE student_course SET class_id = ?, status = 'P' WHERE student_id = ? AND course_id = ?";
            int result = jdbcTemplate.update(updateSql, classId, studentId, courseId);
            
            if (result > 0) {
                logger.info("Successfully updated record for student {}, course {}, class {}", studentId, courseId, classId);
            } else {
                logger.error("Failed to update record for student {}, course {}, class {}", studentId, courseId, classId);
            }
            
            return result > 0;
        } else {
            // 插入新记录
            logger.info("Inserting new record: StudentId={}, CourseId={}, ClassId={}", studentId, courseId, classId);
            String insertSql = "INSERT INTO student_course (student_id, course_id, class_id, status) VALUES (?, ?, ?, 'P')";
            int result = jdbcTemplate.update(insertSql, studentId, courseId, classId);
            
            if (result > 0) {
                logger.info("Successfully inserted record for student {}, course {}, class {}", studentId, courseId, classId);
            } else {
                logger.error("Failed to insert record for student {}, course {}, class {}", studentId, courseId, classId);
            }
            
            return result > 0;
        }
    }
    
    // 检查时间冲突
    private boolean checkTimeConflict(String studentId, String newCourseId, String newClassId, 
                                     String year, String semester) {
        try {
            // 获取新班级的时间段信息
            List<Integer> newClassTimeSlots = getClassTimeSlotIds(newCourseId, newClassId, year, semester);
            if (newClassTimeSlots == null || newClassTimeSlots.isEmpty()) {
                return false;
            }
            
            // 获取学生已选课程（包括预选择和已选择状态）
            String selectedSql = "SELECT course_id, class_id FROM student_course WHERE student_id = ? " +
                                 "AND status IN ('P', 'C') AND course_id != ?";
            List<Map<String, Object>> selectedCourses = jdbcTemplate.queryForList(selectedSql, 
                studentId, newCourseId);
            
            for (Map<String, Object> course : selectedCourses) {
                String existingCourseId = (String) course.get("course_id");
                String existingClassId = (String) course.get("class_id");
                
                // 获取已选课程的时间段信息
                List<Integer> existingTimeSlots = getClassTimeSlotIds(existingCourseId, existingClassId, year, semester);
                
                // 检查是否有重叠的时间段
                if (existingTimeSlots != null && !existingTimeSlots.isEmpty()) {
                    for (Integer newSlot : newClassTimeSlots) {
                        if (existingTimeSlots.contains(newSlot)) {
                            return true; // 存在时间冲突
                        }
                    }
                }
            }
            
            return false; // 没有时间冲突
        } catch (Exception ex) {
            logger.error("检查时间冲突时发生错误", ex);
            return false;
        }
    }
    
    // 获取班级的时间段ID列表
    private List<Integer> getClassTimeSlotIds(String courseId, String classId, String year, String semester) {
        try {
            // 获取第一周的时间段ID字符串
            String sql = "SELECT week1 FROM class_time WHERE course_id = ? AND class_id = ? " +
                         "AND year = ? AND semester = ?";
            String week1TimeSlotsStr = jdbcTemplate.queryForObject(sql, String.class, 
                courseId, classId, year, semester);
            
            if (week1TimeSlotsStr == null || week1TimeSlotsStr.isEmpty()) {
                return null;
            }
            
            // 分割字符串得到TimeSlotId数组
            return Arrays.stream(week1TimeSlotsStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error("获取班级时间段ID列表失败", ex);
            return null;
        }
    }
    
    // 删除课程
    @Transactional
    public boolean removeCourse(String studentId, String courseId) {
        String deleteSql = "DELETE FROM student_course WHERE student_id = ? AND course_id = ?";
        int result = jdbcTemplate.update(deleteSql, studentId, courseId);
        return result > 0;
    }
    
    // 保存选课
    @Transactional
    public boolean saveCourses(String studentId) {
        Map<String, String> currentSemester = getCurrentSemester();
        String currentYear = currentSemester.get("year");
        String currentSemesterStr = currentSemester.get("semester");
        
        // 获取所有预选课程
        String pendingSql = "SELECT course_id, class_id FROM student_course WHERE student_id = ? AND status = 'P'";
        List<Map<String, Object>> pendingCourses = jdbcTemplate.queryForList(pendingSql, studentId);
        
        for (Map<String, Object> course : pendingCourses) {
            String courseId = (String) course.get("course_id");
            String classId = (String) course.get("class_id");
            
            // 再次检查容量
            String capacitySql = "SELECT capacity FROM class WHERE class_id = ? AND course_id = ? " +
                                 "AND year = ? AND semester = ?";
            Integer capacity = jdbcTemplate.queryForObject(capacitySql, Integer.class, 
                classId, courseId, currentYear, currentSemesterStr);
            
            if (capacity == null) continue;
            
            String countSql = "SELECT COUNT(*) FROM student_course WHERE class_id = ? AND course_id = ? " +
                              "AND status IN ('P', 'C')";
            Integer currentCount = jdbcTemplate.queryForObject(countSql, Integer.class, classId, courseId);
            currentCount = currentCount != null ? currentCount : 0;
            
            if (currentCount > capacity) {
                // 容量已满，重置为未选择状态
                String resetSql = "UPDATE student_course SET class_id = '0', status = 'N' " +
                                  "WHERE student_id = ? AND course_id = ?";
                jdbcTemplate.update(resetSql, studentId, courseId);
                
                throw new RuntimeException("课程 " + courseId + " 班级 " + classId + " 已满，请重新选择");
            }
        }
        
        // 更新状态为已选择
        String updateSql = "UPDATE student_course SET status = 'C' WHERE student_id = ? AND status = 'P'";
        int result = jdbcTemplate.update(updateSql, studentId);
        return result > 0;
    }
    
    // 获取班级的时间段信息
    private String getClassTimeSlots(String courseId, String classId, String year, String semester) {
        try {
            // 获取第一周的时间段ID字符串
            String sql = "SELECT week1 FROM class_time WHERE course_id = ? AND class_id = ? " +
                         "AND year = ? AND semester = ?";
            String week1TimeSlotsStr = jdbcTemplate.queryForObject(sql, String.class, 
                courseId, classId, year, semester);
            
            if (week1TimeSlotsStr == null || week1TimeSlotsStr.isEmpty()) {
                return null;
            }
            
            // 分割字符串得到TimeSlotId数组
            List<Integer> timeSlotIds = Arrays.stream(week1TimeSlotsStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
            
            // 根据TimeSlotId获取时间段信息
            if (timeSlotIds.isEmpty()) {
                return null;
            }
            
            String inClause = String.join(",", timeSlotIds.stream().map(String::valueOf).collect(Collectors.toList()));
            String timeSlotSql = "SELECT time_slot_id, day, start_time, end_time FROM time_slot " +
                                 "WHERE time_slot_id IN (" + inClause + ")";
            List<Map<String, Object>> timeSlots = jdbcTemplate.queryForList(timeSlotSql);
            
            // 将时间段按星期几分组
            Map<Integer, List<Integer>> dayMap = new TreeMap<>();
            for (Map<String, Object> ts : timeSlots) {
                Integer day = (Integer) ts.get("day");
                Integer timeSlotId = (Integer) ts.get("time_slot_id");
                
                // 计算节次：TimeSlotId对12取模+1
                int section = (timeSlotId % 12) + 1;
                
                dayMap.computeIfAbsent(day, k -> new ArrayList<>()).add(section);
            }
            
            // 构建时间段字符串
            List<String> dayStrings = new ArrayList<>();
            for (Map.Entry<Integer, List<Integer>> entry : dayMap.entrySet()) {
                String dayName = getDayName(entry.getKey().toString());
                List<Integer> sections = entry.getValue();
                Collections.sort(sections);
                String sectionStr = mergeSections(sections);
                dayStrings.add(dayName + " " + sectionStr);
            }
            
            return String.join(" ", dayStrings);
        } catch (Exception ex) {
            logger.error("获取班级时间段信息失败", ex);
            return null;
        }
    }
    
    // 根据数字获取星期几
    private String getDayName(String dayOfWeek) {
        switch (dayOfWeek) {
            case "0": return "周一";
            case "1": return "周二";
            case "2": return "周三";
            case "3": return "周四";
            case "4": return "周五";
            case "5": return "周六";
            case "6": return "周日";
            default: throw new IllegalArgumentException("Invalid day of week");
        }
    }
    
    // 合并连续节次
    private String mergeSections(List<Integer> sections) {
        if (sections == null || sections.isEmpty()) {
            return "";
        }
        
        Collections.sort(sections);
        List<String> result = new ArrayList<>();
        int start = sections.get(0);
        int end = sections.get(0);
        
        for (int i = 1; i < sections.size(); i++) {
            if (sections.get(i) == end + 1) {
                end = sections.get(i);
            } else {
                if (start == end) {
                    result.add("第" + start + "节");
                } else {
                    result.add("第" + start + "-" + end + "节");
                }
                start = end = sections.get(i);
            }
        }
        
        if (start == end) {
            result.add("第" + start + "节");
        } else {
            result.add("第" + start + "-" + end + "节");
        }
        
        return String.join("、", result);
    }
}