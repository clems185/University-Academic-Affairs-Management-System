package com.system.schedule.service;

import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class PasswordHelper {
    
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 16;
    
    // 生成盐值
    public String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    // 生成哈希密码
    public String hashPassword(String password, String salt) {
        try {
            // 注意：Java中没有直接的Rfc2898DeriveBytes实现
            // 这里使用PBKDF2WithHmacSHA256替代
            javax.crypto.SecretKeyFactory factory = 
                javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            javax.crypto.spec.PBEKeySpec spec = new javax.crypto.spec.PBEKeySpec(
                password.toCharArray(),
                Base64.getDecoder().decode(salt),
                ITERATIONS,
                KEY_LENGTH * 8
            );
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("密码哈希生成失败", e);
        }
    }
    
    // 验证密码
    public boolean verifyPassword(String inputPassword, String storedHash, String storedSalt) {
        String hashOfInput = hashPassword(inputPassword, storedSalt);
        return hashOfInput.equals(storedHash);
    }
}