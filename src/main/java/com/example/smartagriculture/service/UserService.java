package com.example.smartagriculture.service;

import com.example.smartagriculture.common.StatusCode;
import com.example.smartagriculture.exception.CustomException;
import com.example.smartagriculture.mapper.UserMapper;
import com.example.smartagriculture.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: MjXie
 * @Date: 2025/04/07/20:40
 * @Description:
 */
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired  // 添加这行注入
    private UserPermissionService permissionService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 用户注册
     */
    public User register(User user) {
        // 检查用户名是否已存在
        if(userMapper.findByUsername(user.getUsername()) != null){
            throw new CustomException(400, "用户名已存在");
        }
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        int result = userMapper.insertUser(user);
        if(result <= 0) {
            throw new CustomException(StatusCode.SERVER_ERROR.getCode(), "注册失败");
        }
        return user;
    }

    /**
     * 用户登录
     */
    public User login(String username, String rawPassword) {
        User user = userMapper.findByUsername(username);
        if(user == null || !passwordEncoder.matches(rawPassword, user.getPassword())){
            throw new CustomException(400, "用户名或密码错误");
        }
        return user;
    }

    public User getUserById(Long id) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new CustomException(404, "用户不存在");
        }
        return user;
    }

    /**
     * 用户更新
     */
    @Transactional
    public User updateUser(Long userId, User userDetails) {
        User existingUser = getUserById(userId); // 自带不存在检查

        if (userDetails.getEmail() != null) {
            existingUser.setEmail(userDetails.getEmail());
        }
        if (userDetails.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        if (userDetails.getRole() != null) {
            existingUser.setRole(userDetails.getRole());
        }

        userMapper.updateUser(existingUser);
        return existingUser;
    }

    /**
     * 用户删除
     */
    @Transactional
    public void deleteUser(Long userId) {
        // 先删除关联权限
        permissionService.deletePermissionByUserId(userId);
        // 再删除用户
        userMapper.deleteUser(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        List<GrantedAuthority> auths = Arrays.stream(user.getRole().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), auths);
    }

}
