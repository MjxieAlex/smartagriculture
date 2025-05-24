package com.example.smartagriculture.controller;

import com.example.smartagriculture.common.ApiResponse;
import com.example.smartagriculture.config.RedisConfig;
import com.example.smartagriculture.model.User;
import com.example.smartagriculture.model.UserPermission;
import com.example.smartagriculture.service.MailService;
import com.example.smartagriculture.service.UserPermissionService;
import com.example.smartagriculture.service.UserService;
import com.example.smartagriculture.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: MjXie
 * @Date: 2025/04/07/20:42
 * @Description:
 */

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MailService mailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private UserPermissionService permissionService;

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public ApiResponse<User> register(@Valid @RequestBody User user) {
        User registeredUser = userService.register(user);
        return ApiResponse.success(registeredUser);
    }

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(
            @RequestParam String username,
            @RequestParam String password) {
        User user = userService.login(username, password);
        // 调用 Auth0 Java JWT 的生成方法
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        // 可选：存 Redis 支持单点登出
        redisTemplate.opsForValue()
                .set("token:" + user.getUsername(), token,
                        Duration.ofSeconds(jwtUtil.getExpiration()));
        return ApiResponse.success(Map.of("token", token));
    }


    /**
     * 为用户添加权限
     */
    @PostMapping("/{userId}/permissions")
    public ApiResponse<UserPermission> addPermission(@PathVariable Long userId,
                                                     @RequestParam String permission) {
        UserPermission userPermission = new UserPermission();
        userPermission.setUserId(userId);
        userPermission.setPermission(permission);
        UserPermission result = permissionService.addPermission(userPermission);
        return ApiResponse.success(result);
    }

    /**
     * 根据用户ID获取权限
     */
    @GetMapping("/{userId}/permissions")
    public ApiResponse<UserPermission> getPermission(@PathVariable Long userId) {
        UserPermission permission = permissionService.getPermissionByUserId(userId);
        return ApiResponse.success(permission);
    }

    /**
     * 用户信息查询
     */
    @GetMapping("/{userId}")
    public ApiResponse<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ApiResponse.success(user);
    }

    /**
     * 用户信息更新
     */
    @PutMapping("/{userId}")
    public ApiResponse<User> updateUser(@PathVariable Long userId,
                                        @Valid @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(userId, userDetails);
        return ApiResponse.success(updatedUser);
    }

    /**
     * 用户信息删除
     */
    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ApiResponse.success(null);
    }

    /**
     * 用户权限删除
     */
    @DeleteMapping("/{userId}/permissions")
    public ApiResponse<Void> deletePermission(@PathVariable Long userId) {
        permissionService.deletePermissionByUserId(userId);
        return ApiResponse.success(null);
    }

    /**
     * 用户权限更新
     */
    @PutMapping("/{userId}/permissions")
    public ApiResponse<UserPermission> updatePermission(@PathVariable Long userId,
                                                        @RequestParam String permission) {
        UserPermission updated = permissionService.updatePermission(userId, permission);
        return ApiResponse.success(updated);
    }

    /** 用户登出 */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtil.getUsername(token);
            redisTemplate.delete("token:" + username);
        }
        return ApiResponse.success(null);
    }
}
