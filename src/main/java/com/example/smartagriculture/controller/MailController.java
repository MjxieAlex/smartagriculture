package com.example.smartagriculture.controller;

import com.example.smartagriculture.common.ApiResponse;
import com.example.smartagriculture.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: MjXie
 * @Date: 2025/05/12/10:49
 * @Description:
 */
@RestController
@RequestMapping("/api/mail")
public class MailController {
    @Autowired private MailService mailService;


    @PostMapping("/send")
    public ApiResponse<Void> sendMailCode(
            @RequestParam String email,
            @RequestParam String uuid) {
        mailService.sendVerificationCode(email, uuid);
        return ApiResponse.success(null);
    }

    @PostMapping("/verify")
    public ApiResponse<Boolean> verify(
            @RequestParam String uuid,
            @RequestParam String code) {
        boolean ok = mailService.verifyCode(uuid, code);
        return ApiResponse.success(ok);
    }
}
