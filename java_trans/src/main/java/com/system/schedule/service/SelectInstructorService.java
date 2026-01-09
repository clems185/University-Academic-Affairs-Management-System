package com.system.schedule.service;

import com.system.schedule.dto.*;
import com.system.schedule.entity.IsStudent;
import com.system.schedule.entity.IsTeacher;
import com.system.schedule.entity.InstructorSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SelectInstructorService {
    
    private static final Logger logger = LoggerFactory.getLogger(SelectInstructorService.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // 获取可用的导师选择轮次
    @Transactional(readOnly = true)
    public List<InstructorSelectionRoundDto> getAvailableInstructorSelections(String studentId) {
        String sql = "SELECT selection_id AS selectionId, information, begin_time AS beginTime, " +
                     "end_time AS endTime, true AS isActive " +
                     "FROM instructor_selection";
        
        return jdbcTemplate.query(sql, 
            new BeanPropertyRowMapper<>(InstructorSelectionRoundDto.class));
    }
    
    // 获取所有导师信息
    @Transactional(readOnly = true)
    public List<InstructorInfoDto> getAllInstructors(String studentId) {
        // 获取当前学生已选择的导师信息
        String currentSql = "SELECT instructor_id, status FROM is_student WHERE student_id = ?";
        List<Map<String, Object>> currentSelections = jdbcTemplate.queryForList(currentSql, studentId);
        
        String currentInstructorId = "";
        String currentStatus = "";
        if (!currentSelections.isEmpty()) {
            Map<String, Object> selection = currentSelections.get(0);
            currentInstructorId = (String) selection.get("instructor_id");
            currentStatus = (String) selection.get("status");
        }
        
        String sql = "SELECT it.selection_id AS selectionId, t.teacher_id AS teacherId, " +
                     "t.teacher_name AS teacherName, m.name AS majorName, t.email, t.telephone, " +
                     "it.capacity, " +
                     "(SELECT COUNT(*) FROM is_student WHERE instructor_id = t.teacher_id AND status = 'C') AS selectedCount, " +
                     "CASE WHEN ? = t.teacher_id THEN ? ELSE 'N' END AS status " +
                     "FROM is_teacher it " +
                     "INNER JOIN teacher t ON it.teacher_id = t.teacher_id " +
                     "LEFT JOIN major m ON t.major_id = m.major_id";
        
        return jdbcTemplate.query(sql, 
            new BeanPropertyRowMapper<>(InstructorInfoDto.class),
            currentInstructorId, currentStatus);
    }
    
    // 选择导师
    @Transactional
    public boolean selectInstructor(String studentId, String teacherId) {
        // 获取导师容量信息
        String teacherSql = "SELECT capacity FROM is_teacher WHERE teacher_id = ?";
        Integer capacity = jdbcTemplate.queryForObject(teacherSql, Integer.class, teacherId);
        
        if (capacity == null) {
            return false;
        }
        
        // 获取当前已选人数
        String countSql = "SELECT COUNT(*) FROM is_student WHERE instructor_id = ? AND status = 'C'";
        Integer currentCount = jdbcTemplate.queryForObject(countSql, Integer.class, teacherId);
        currentCount = currentCount != null ? currentCount : 0;
        
        if (currentCount >= capacity) {
            throw new RuntimeException("该导师已满，请选择其他导师");
        }
        
        // 检查是否已存在记录
        String existSql = "SELECT COUNT(*) FROM is_student WHERE student_id = ?";
        Integer exists = jdbcTemplate.queryForObject(existSql, Integer.class, studentId);
        
        if (exists != null && exists > 0) {
            // 更新现有记录
            String updateSql = "UPDATE is_student SET instructor_id = ?, status = 'P' WHERE student_id = ?";
            int result = jdbcTemplate.update(updateSql, teacherId, studentId);
            return result > 0;
        } else {
            // 插入新记录
            String insertSql = "INSERT INTO is_student (student_id, instructor_id, status) VALUES (?, ?, 'P')";
            int result = jdbcTemplate.update(insertSql, studentId, teacherId);
            return result > 0;
        }
    }
    
    // 保存导师选择
    @Transactional
    public boolean saveInstructorSelection(String studentId) {
        // 获取学生的选择记录
        String selectionSql = "SELECT instructor_id, status FROM is_student WHERE student_id = ? AND status = 'P'";
        List<Map<String, Object>> selections = jdbcTemplate.queryForList(selectionSql, studentId);
        
        if (selections.isEmpty()) {
            return false;
        }
        
        Map<String, Object> selection = selections.get(0);
        String teacherId = (String) selection.get("instructor_id");
        
        // 获取导师容量信息
        String teacherSql = "SELECT capacity FROM is_teacher WHERE teacher_id = ?";
        Integer capacity = jdbcTemplate.queryForObject(teacherSql, Integer.class, teacherId);
        
        if (capacity == null) {
            return false;
        }
        
        // 获取当前已选人数
        String countSql = "SELECT COUNT(*) FROM is_student WHERE instructor_id = ? AND status = 'C'";
        Integer currentCount = jdbcTemplate.queryForObject(countSql, Integer.class, teacherId);
        currentCount = currentCount != null ? currentCount : 0;
        
        if (currentCount >= capacity) {
            // 删除学生的选择记录
            String deleteSql = "DELETE FROM is_student WHERE student_id = ?";
            jdbcTemplate.update(deleteSql, studentId);
            throw new RuntimeException("导师 " + teacherId + " 已满，请重新选择");
        }
        
        // 更新状态为已选择
        String updateSql = "UPDATE is_student SET status = 'C' WHERE student_id = ?";
        int result = jdbcTemplate.update(updateSql, studentId);
        return result > 0;
    }
    
    // 撤回导师选择
    @Transactional
    public boolean withdrawInstructor(String studentId) {
        String deleteSql = "DELETE FROM is_student WHERE student_id = ?";
        int result = jdbcTemplate.update(deleteSql, studentId);
        return result > 0;
    }
    
    // 根据ID获取导师选择信息
    @Transactional(readOnly = true)
    public InformationDto getInstructorSelectionById(String selectionId) {
        String sql = "SELECT selection_id AS selectionId, information AS selectionName " +
                     "FROM instructor_selection WHERE selection_id = ?";
        
        return jdbcTemplate.queryForObject(sql, 
            new BeanPropertyRowMapper<>(InformationDto.class),
            selectionId);
    }
}