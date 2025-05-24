package com.example.smartagriculture.mapper;

import com.example.smartagriculture.model.User;
import com.example.smartagriculture.model.UserPermission;
import org.apache.ibatis.annotations.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: MjXie
 * @Date: 2025/04/07/20:39
 * @Description:
 */
@Mapper
public interface UserPermissionMapper {
    @Insert("INSERT INTO user_permissions(user_id, permission) VALUES(#{userId}, #{permission})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertUserPermission(UserPermission userPermission);

    @Select("SELECT * FROM user_permissions WHERE user_id = #{userId}")
    UserPermission findByUserId(Long userId);

    @Update("UPDATE user_permissions SET permission = #{permission} WHERE user_id = #{userId}")
    int update(UserPermission userPermission);

    @Delete("DELETE FROM user_permissions WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);
}
