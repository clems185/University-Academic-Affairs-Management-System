package com.system.schedule.controller;

import com.system.schedule.dto.login.ForgetReq;
import com.system.schedule.dto.login.LoginReq;
import com.system.schedule.dto.login.VerificationReq;
import com.system.schedule.dto.user.UserRes;
import com.system.schedule.service.UserService;
import com.system.schedule.utils.JwtUtil;
import com.system.schedule.utils.ResultHelper;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/all")
    public ResponseEntity<List<UserRes>> getAllUsers() {
        List<UserRes> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginReq req) {
        UserRes user = userService.getUser(req);
        logger.info("Login request: userId={}, password={}", req.getUserId(), req.getPassword());
        if (user == null) {
            return ResponseEntity.badRequest().body(ResultHelper.error("账号不存在,用户名或密码错误"));
        }

        logger.info("登录成功，账号密码完全正确");

        String token = jwtUtil.generateToken(user);

        Claims claims = jwtUtil.getClaimsFromToken(token);
        logger.info("生成的Token包含Claims:");
        claims.forEach((key, value) -> {
            logger.info("Type: {}, Value: {}", key, value);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("Token", token);
        response.put("UserType", user.getUserType() != null ? user.getUserType() : "");
        response.put("Id", user.getId() != null ? user.getId() : "");
        response.put("Name", user.getName() != null ? user.getName() : "");
        response.put("NickName", user.getNickName() != null ? user.getNickName() : "");

        return ResponseEntity.ok(ResultHelper.success(response));
    }

    @PostMapping("/verification")
    public ResponseEntity<Map<String, Object>> getVerificationCode(@RequestBody VerificationReq req) {
        boolean isSent = userService.getVerification(req);
        if (isSent) {
            return ResponseEntity.ok(ResultHelper.success("验证码已经发送，请注意查收"));
        } else {
            return ResponseEntity.badRequest().body(ResultHelper.error("用户名和邮箱有误，请重新检查"));
        }
    }

    @PostMapping("/forgetpassword")
    public ResponseEntity<Map<String, Object>> forgetPassword(@RequestBody ForgetReq req) {
        boolean isSuccess = userService.forgetPassword(req);
        if (isSuccess) {
            return ResponseEntity.ok(ResultHelper.success("密码已经更新，请使用新密码重新登录"));
        } else {
            return ResponseEntity.badRequest().body(ResultHelper.error("密码更新失败，请检查是否存在信息错误"));
        }
    }

}