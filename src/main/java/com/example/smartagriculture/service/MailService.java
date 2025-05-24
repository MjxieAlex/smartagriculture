package com.example.smartagriculture.service;


import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: MjXie
 * @Date: 2025/05/12/10:48
 * @Description:
 */
@Service
public class MailService {
    @Autowired private JavaMailSender mailSender;
    @Autowired private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${spring.mail.username}")
    private String fromEmail;  // ← 注入发件人地址

    public void sendVerificationCode(String toEmail, String uuid) {
        System.out.println("UUID passed in sendVerificationCode: " + uuid);
        String code = RandomStringUtils.randomNumeric(6);
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setFrom(fromEmail);                // ← 指定发件人
        msg.setTo(toEmail);
        msg.setSubject("您的验证码");
        msg.setText("您的验证码是：" + code + "，5 分钟内有效。");

        mailSender.send(msg);

        stringRedisTemplate.opsForValue().set("mailcode:" + uuid, code, Duration.ofMinutes(5));
        System.out.println("Stored in Redis: mailcode:" + uuid + " -> " + code);
    }

    public boolean verifyCode(String uuid, String code) {
        String cached = stringRedisTemplate.opsForValue().get("mailcode:" + uuid);
        System.out.println("UUID: " + uuid);
        System.out.println("Input code: " + code);
        System.out.println("Cached code from Redis: " + cached);
        return cached != null && cached.equals(code);
    }
}