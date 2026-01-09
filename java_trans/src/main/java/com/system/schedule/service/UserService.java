// UserService.java
package com.system.schedule.service;

import com.system.schedule.dto.Login.LoginReq;
import com.system.schedule.dto.Login.UserRes;
import com.system.schedule.dto.User.*;
import com.system.schedule.entity.Users;
import com.system.schedule.mapper.UsersMapper;
import com.system.schedule.util.PasswordHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.security.SecureRandom;
import java.util.List;
import java.time.Instant;
import java.util.Random;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UsersMapper usersMapper;
    
    @Autowired
    private JavaMailSender mailSender;

    public UserRes getUser(LoginReq req) {
        // 从数据库获取用户信息
        Users user = usersMapper.selectById(req.getName());

        if (user != null && PasswordHelper.verifyPassword(req.getPassword(), user.getPasswordHash(), user.getSalt())) {
            // 密码验证成功，映射到响应DTO
            UserRes userRes = new UserRes();
            userRes.setId(user.getId());
            userRes.setName(user.getName());
            userRes.setNickName(user.getNickName());
            userRes.setEmail(user.getEmail());
            userRes.setType(user.getType());
            return userRes;
        }

        return null; // 验证失败返回null
    }

    public List<Users> getAllUsers() {
        return usersMapper.selectList(null);
    }

    public boolean editNickeNameOrPassword(String userId, PersonEdit req) {
        Users info = usersMapper.selectById(userId);
        if (info != null) {
            boolean updated = false;
            
            if (StringUtils.hasText(req.getNickName())) {
                info.setNickName(req.getNickName());
                updated = true;
            }

            if (StringUtils.hasText(req.getPassword())) {
                // 更新密码时生成新的盐值和哈希
                String salt = PasswordHelper.generateSalt();
                info.setSalt(salt);
                info.setPasswordHash(PasswordHelper.hashPassword(req.getPassword(), salt));
                updated = true;
            }

            if (updated) {
                return usersMapper.updateById(info) > 0;
            }
        }

        return false;
    }

    public boolean forgetPassword(Forgetreq req) {
        Users info = usersMapper.selectById(req.getId());
        if (info != null && info.getVerification() != null) {
            long timestamp = Instant.now().getEpochSecond();
            if (timestamp - info.getCreateTime() >= 600 || !req.getVerificationCode().equals(info.getVerification())) {
                return false;
            } else {
                String salt = PasswordHelper.generateSalt();
                String hash = PasswordHelper.hashPassword(req.getNewPassWord(), salt);
                
                Users updateUser = new Users();
                updateUser.setId(req.getId());
                updateUser.setPasswordHash(hash);
                updateUser.setSalt(salt);
                updateUser.setVerification(null);
                updateUser.setCreateTime(0L);
                
                return usersMapper.updateById(updateUser) > 0;
            }
        } else {
            return false;
        }
    }

    public boolean getVerification(VerificationReq req) {
        Users info = usersMapper.selectById(req.getUserId());
        if (info != null) {
            String userEmail = info.getEmail();
            if (!userEmail.equals(req.getEmail())) {
                return false;
            } else {
                int length = 6;
                String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                SecureRandom random = new SecureRandom();
                StringBuilder res = new StringBuilder(length);
                
                for (int i = 0; i < length; i++) {
                    int index = random.nextInt(allowedChars.length());
                    res.append(allowedChars.charAt(index));
                }
                
                long timestamp = Instant.now().getEpochSecond();
                Users updateUser = new Users();
                updateUser.setId(req.getUserId());
                updateUser.setVerification(res.toString());
                updateUser.setCreateTime(timestamp);
                
                boolean isSuccess = usersMapper.updateById(updateUser) > 0;
                
                if (isSuccess) {
                    try {
                        SimpleMailMessage message = new SimpleMailMessage();
                        message.setFrom("382819364@qq.com");
                        message.setTo(req.getEmail());
                        message.setSubject("密码找回验证");
                        message.setText("验证码为" + res.toString() + ",十分钟之内有效，请勿泄露");
                        
                        mailSender.send(message);
                        return true;
                    } catch (Exception ex) {
                        log.error("邮件发送失败: {}", ex.getMessage());
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}