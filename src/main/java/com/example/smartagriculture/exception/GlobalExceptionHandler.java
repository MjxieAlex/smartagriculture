package com.example.smartagriculture.exception;

import com.example.smartagriculture.common.ApiResponse;
import com.example.smartagriculture.common.StatusCode;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: MjXie
 * @Date: 2025/04/07/20:18
 * @Description: 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 处理参数校验异常（@Valid验证失败）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        return ApiResponse.error(StatusCode.VALIDATION_ERROR.getCode(), errorMessage);
    }

    // 处理绑定参数异常
    @ExceptionHandler(BindException.class)
    public ApiResponse<?> handleBindException(BindException e) {
        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        return ApiResponse.error(StatusCode.VALIDATION_ERROR.getCode(), errorMessage);
    }

    // 处理自定义异常
    @ExceptionHandler(CustomException.class)
    public ApiResponse<?> handleCustomException(CustomException e) {
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    // 处理所有其他异常
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e) {
        e.printStackTrace();
        return ApiResponse.error(StatusCode.SERVER_ERROR.getCode(), StatusCode.SERVER_ERROR.getMessage());
    }
}
