package com.example.smartagriculture.mapper;

import com.example.smartagriculture.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: MjXie
 * @Date: 2025/04/07/20:39
 * @Description:
 */
@Mapper
public interface UserMapper {
    //
    @Insert("INSERT INTO users(username, password, email, role, created_at) VALUES(#{username}, #{password}, #{email}, #{role}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertUser(User user);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);

    @Update("UPDATE users SET " +
            "email = #{email}, " +
            "password = #{password}, " +
            "role = #{role} " +
            "WHERE id = #{id}")
    int updateUser(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteUser(Long id);
}
