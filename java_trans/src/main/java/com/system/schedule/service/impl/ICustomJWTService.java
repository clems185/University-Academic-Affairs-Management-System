package com.system.schedule.service;

import com.system.schedule.dto.user.UserRes;
import java.util.concurrent.CompletableFuture;

public interface ICustomJWTService {
    // 获取token
    CompletableFuture<String> getToken(UserRes user);
}