package com.system.schedule.service;

import com.system.schedule.dto.ProfileDto;
import com.system.schedule.dto.UpdateInformationApplyDto;
import com.system.schedule.entity.*;
import com.system.schedule.mapper.PhotoMapper;
import com.system.schedule.mapper.StudentMapper;
import com.system.schedule.mapper.TeacherMapper;
import com.system.schedule.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;

@Service
public class ProfileService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private TeacherMapper teacherMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PhotoMapper photoMapper;
    
    // 获取用户个人信息
    @Transactional(readOnly = true)
    public ProfileDto getUserProfile(String userId, String userType) {
        try {
            // 统一转换为数字类型判断
            int type;
            try {
                type = Integer.parseInt(userType);
            } catch (NumberFormatException e) {
                throw new Exception("用户类型格式不正确");
            }
            
            // 根据用户类型查询不同信息（2=教师，3=学生）
            if (type == 3) { // 学生
                return getStudentProfile(userId);
            } else if (type == 2) { // 教师
                return getTeacherProfile(userId);
            } else { // 管理员或其他类型
                return getUserBasicProfile(userId);
            }
        } catch (Exception ex) {
            logger.error("获取用户个人信息失败，用户ID：{}，用户类型：{}", userId, userType, ex);
            throw new RuntimeException("获取用户个人信息失败", ex);
        }
    }
    
    private ProfileDto getStudentProfile(String studentId) throws Exception {
        // 查询学生头像
        Photos studentPhoto = photoMapper.findByIdAndType(studentId, "STUDENT");
        byte[] avatarBytes = studentPhoto != null ? studentPhoto.getPhoto() : null;
        
        // 查询学生信息（这里简化处理，实际需要多表联查）
        Student student = studentMapper.findById(studentId);
        if (student == null) {
            throw new Exception("未找到学生信息");
        }
        
        // 查询专业信息
        Major major = studentMapper.findMajorById(student.getMajorId());
        
        // 查询用户信息
        Users user = userMapper.findById(studentId);
        
        ProfileDto profile = new ProfileDto();
        profile.setId(student.getId());
        profile.setName(student.getName());
        profile.setNickName(user != null ? user.getNickName() : null);
        profile.setUserType(3);
        profile.setEmail(student.getEmail());
        profile.setTelephone(student.getTelephone());
        profile.setMajorName(major != null ? major.getName() : null);
        profile.setStartYear(student.getStartYear());
        profile.setEndYear(student.getEndYear());
        profile.setGpa(student.getGpa());
        profile.setAttemptedCredit(student.getAttemptedCredit());
        profile.setEarnedCredit(student.getEarnedCredit());
        profile.setInformation(student.getInformation());
        profile.setAvatar(avatarBytes != null ? 
            Base64.getEncoder().encodeToString(avatarBytes) : null);
        
        return profile;
    }
    
    private ProfileDto getTeacherProfile(String teacherId) throws Exception {
        // 1. 统计当前授课数
        int courseCount = teacherMapper.countTeachesByTeacherId(teacherId);
        
        // 2. 统计指导学生数
        int studentCount = teacherMapper.countIsStudentByInstructorId(teacherId);
        
        // 3. 查询教师头像
        Photos teacherPhoto = photoMapper.findByIdAndType(teacherId, "TEACHER");
        byte[] avatarBytes = teacherPhoto != null ? teacherPhoto.getPhoto() : null;
        
        // 查询教师信息
        Teacher teacher = teacherMapper.findById(teacherId);
        if (teacher == null) {
            throw new Exception("未找到教师信息");
        }
        
        // 查询专业信息
        Major major = teacherMapper.findMajorById(teacher.getMajorId());
        
        // 查询用户信息
        Users user = userMapper.findById(teacherId);
        
        ProfileDto profile = new ProfileDto();
        profile.setId(teacher.getTeacherId());
        profile.setName(teacher.getTeacherName());
        profile.setNickName(user != null ? user.getNickName() : null);
        profile.setUserType(2);
        profile.setEmail(teacher.getEmail());
        profile.setTelephone(teacher.getTelephone());
        profile.setMajorName(major != null ? major.getName() : null);
        profile.setStartYear(teacher.getStartYear());
        profile.setWorkYear(teacher.getWorkYear());
        profile.setInformation(teacher.getInformation());
        profile.setCourseCount(courseCount);
        profile.setStudentCount(studentCount);
        profile.setTitle(teacher.getLevel());
        profile.setAvatar(avatarBytes != null ? 
            Base64.getEncoder().encodeToString(avatarBytes) : null);
        
        return profile;
    }
    
    private ProfileDto getUserBasicProfile(String userId) throws Exception {
        Users user = userMapper.findById(userId);
        if (user == null) {
            throw new Exception("未找到用户信息");
        }
        
        ProfileDto profile = new ProfileDto();
        profile.setId(user.getId());
        profile.setName(user.getName());
        profile.setNickName(user.getNickName());
        profile.setUserType(user.getUserType());
        profile.setEmail(user.getEmail());
        
        return profile;
    }
    
    // 生成10位随机字母数字组合的唯一ID
    private String generate10DigitApplyId() {
        // 字符集：包含数字和大小写字母
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(10);
        
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return sb.toString();
    }
    
    // 提交个人信息修改申请
    @Transactional
    public boolean submitUpdateApply(UpdateInformationApplyDto dto) {
        try {
            // 生成唯一的申请ID
            String applyId = generate10DigitApplyId();
            
            String types = "";
            if ("2".equals(dto.getUserType())) {
                types = "教师";
            } else if ("3".equals(dto.getUserType())) {
                types = "学生";
            } else {
                types = "其他";
            }
            
            // 转换Base64图片为字节数组（如果有）
            byte[] newPhotoBytes = null;
            if (dto.getNewPhoto() != null && !dto.getNewPhoto().isEmpty()) {
                newPhotoBytes = Base64.getDecoder().decode(dto.getNewPhoto());
            }
            
            // 创建申请实体
            UpdateInformationApply apply = new UpdateInformationApply();
            apply.setApplyId(applyId);
            apply.setApplicantId(dto.getUserId());
            apply.setApplicantType(types);
            apply.setApplyTime(LocalDateTime.now());
            apply.setInformation(dto.getInformation());
            apply.setNewProfile(dto.getNewProfile());
            apply.setNewPhoto(newPhotoBytes);
            apply.setFinalDecision("P"); // 默认处理中
            
            // 插入数据库
            userMapper.insertUpdateInformationApply(apply);
            return true;
        } catch (Exception ex) {
            logger.error("提交个人信息修改申请失败，用户ID：{}", dto.getUserId(), ex);
            throw new RuntimeException("提交个人信息修改申请失败", ex);
        }
    }
}