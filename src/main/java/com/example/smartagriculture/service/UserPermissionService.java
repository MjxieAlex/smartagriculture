package com.example.smartagriculture.service;

import com.example.smartagriculture.common.StatusCode;
import com.example.smartagriculture.exception.CustomException;
import com.example.smartagriculture.mapper.UserPermissionMapper;
import com.example.smartagriculture.model.UserPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: MjXie
 * @Date: 2025/04/07/20:41
 * @Description:
 */
@Service
public class UserPermissionService {
    @Autowired
    private UserPermissionMapper userPermissionMapper;

    /**
     * 为用户添加权限
     */
    public UserPermission addPermission(UserPermission permission) {
        int result = userPermissionMapper.insertUserPermission(permission);
        if(result <= 0) {
            throw new CustomException(StatusCode.SERVER_ERROR.getCode(), "添加权限失败");
        }
        return permission;
    }

    /**
     * 根据用户ID查询权限
     */
    public UserPermission getPermissionByUserId(Long userId) {
        return userPermissionMapper.findByUserId(userId);
    }


    /**
     * 根据用户ID删除权限
     */
    @Transactional
    public void deletePermissionByUserId(Long userId) {
        userPermissionMapper.deleteByUserId(userId);
    }

    /**
     * 根据用户ID更新权限
     */
    public UserPermission updatePermission(Long userId, String permission) {
        UserPermission existing = userPermissionMapper.findByUserId(userId);
        if (existing == null) {
            throw new CustomException(404, "该用户没有可更新的权限");
        }
        existing.setPermission(permission);
        userPermissionMapper.update(existing);
        return existing;
    }
}
