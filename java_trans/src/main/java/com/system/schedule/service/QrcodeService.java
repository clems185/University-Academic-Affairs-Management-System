package com.system.schedule.service;

import com.system.schedule.dto.SignReq;
import com.system.schedule.dto.SignRes;
import com.system.schedule.dto.ClassList;
import com.system.schedule.entity.Sign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QrcodeService {
    
    private static final Logger logger = LoggerFactory.getLogger(QrcodeService.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // 获取已签到学生列表
    public List<SignRes> getCheckedInStudents(SignReq req) {
        logger.info("获取签到学生");
        String sql = "SELECT student_id AS studentId, student_name AS studentName, sign_time AS signTime " +
                     "FROM sign WHERE sign_id = ? AND class_id = ? AND course_id = ?";
        return jdbcTemplate.query(sql, 
            new BeanPropertyRowMapper<>(SignRes.class),
            req.getSignId(), req.getClassId(), req.getCourseId());
    }
    
    // 获取教师所授班级列表
    public List<ClassList> getClassList(String teacherId) {
        try {
            // 使用多表联查，获取教师所授班级
            String sql = "SELECT c.class_id AS classId, c.course_id AS courseId, c.name " +
                         "FROM teaches t " +
                         "LEFT JOIN class c ON t.class_id = c.class_id AND t.course_id = c.course_id " +
                         "WHERE t.teacher_id = ? AND c.class_id IS NOT NULL";
            return jdbcTemplate.query(sql, 
                new BeanPropertyRowMapper<>(ClassList.class), 
                teacherId);
        } catch (Exception ex) {
            logger.error("获取班级列表失败", ex);
            return List.of(); // 异常时返回空列表
        }
    }
    
    // 添加签到学生
    public boolean addCheckedInStudent(Sign sign) {
        try {
            if (sign == null) {
                logger.warn("传入的Sign对象为null，添加失败");
                return false;
            }
            
            String sql = "INSERT INTO sign (sign_id, class_id, course_id, student_id, student_name, sign_time) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            int result = jdbcTemplate.update(sql,
                sign.getSignId(),
                sign.getClassId(),
                sign.getCourseId(),
                sign.getStudentId(),
                sign.getStudentName(),
                sign.getSignTime());
            
            boolean success = result > 0;
            logger.info("添加签到记录：{}，SignId: {}, StudentId: {}",
                success ? "成功" : "失败", sign.getSignId(), sign.getStudentId());
            return success;
        } catch (Exception ex) {
            logger.error("添加签到学生时发生异常", ex);
            return false;
        }
    }
}