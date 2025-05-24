package com.example.smartagriculture.exception;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: MjXie
 * @Date: 2025/04/07/20:27
 * @Description: 自定义异常类，可以在业务层抛出
 */
@Getter
public class CustomException extends RuntimeException{
    private final int code;

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }

}
