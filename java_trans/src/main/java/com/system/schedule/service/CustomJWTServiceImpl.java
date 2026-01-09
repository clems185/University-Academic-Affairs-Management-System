package com.system.schedule.service;

import com.system.schedule.dto.user.UserRes;
import com.system.schedule.service.ICustomJWTService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class CustomJWTServiceImpl implements ICustomJWTService {

    // JWT配置参数
    private static final String SECRET_KEY = "your-secret-key-here";
    private static final long EXPIRATION_TIME = 86400000; // 24小时，单位毫秒
    
    @Override
    public CompletableFuture<String> getToken(UserRes user) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 生成JWT Token
                String token = Jwts.builder()
                        .setSubject(user.getId())
                        .claim("name", user.getName())
                        .claim("nickName", user.getNickName())
                        .claim("userType", user.getUserType())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                        .compact();
                
                log.info("生成Token成功，用户ID: {}", user.getId());
                return token;
                
            } catch (Exception e) {
                log.error("生成Token失败: {}", e.getMessage(), e);
                return null;
            }
        });
    }
}