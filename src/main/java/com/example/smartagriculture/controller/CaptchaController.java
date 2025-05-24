package com.example.smartagriculture.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.example.smartagriculture.common.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: MjXie
 * @Date: 2025/05/12/10:47
 * @Description:
 */
@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {
    @Autowired private DefaultKaptcha captchaProducer;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/{uuid}")
    public void getCaptcha(@PathVariable String uuid, HttpServletResponse resp) throws IOException {
        String text = captchaProducer.createText();
        BufferedImage img = captchaProducer.createImage(text);
        redisTemplate.opsForValue().set("captcha:" + uuid, text, Duration.ofMinutes(5));
        resp.setContentType("image/jpeg");
        ImageIO.write(img, "jpg", resp.getOutputStream());
    }

    @GetMapping("/{uuid}/verify")
    public ApiResponse<Boolean> verify(
            @PathVariable String uuid,
            @RequestParam String code) {
        String real = (String)redisTemplate.opsForValue().get("captcha:" + uuid);
        boolean ok = real != null && real.equalsIgnoreCase(code);
        return ApiResponse.success(ok);
    }
}
