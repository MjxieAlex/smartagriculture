package com.example.smartagriculture.common;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: MjXie
 * @Date: 2025/04/07/20:17
 * @Description: 状态码枚举类
 */
public enum StatusCode {
    SUCCESS(200, "成功"),
    VALIDATION_ERROR(400, "参数校验错误"),
    SERVER_ERROR(500, "服务器异常");

    private final int code;
    private final String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
