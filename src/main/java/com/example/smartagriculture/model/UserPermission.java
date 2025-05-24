package com.example.smartagriculture.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: MjXie
 * @Date: 2025/04/07/20:37
 * @Description:
 */
@Data
public class UserPermission {
    private Long id;

    private Long userId;

    @NotBlank(message = "权限不能为空")
    private String permission; // 如：查看、编辑、删除
}
