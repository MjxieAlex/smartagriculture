package com.example.smartagriculture.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class CaptchaConfig {

    @Bean
    public DefaultKaptcha captchaProducer(
            @Value("${kaptcha.border}") String border,
            @Value("${kaptcha.textproducer.font.color}") String color,
            @Value("${kaptcha.textproducer.char.length}") int length,
            @Value("${kaptcha.textproducer.font.size}") int fontSize,
            @Value("${kaptcha.textproducer.noise.color}") String noiseColor  // ← 这里改为 textproducer.noise.color
    ) {
        Properties props = new Properties();
        props.put("kaptcha.border", border);
        props.put("kaptcha.textproducer.font.color", color);
        props.put("kaptcha.textproducer.char.length", String.valueOf(length));
        props.put("kaptcha.textproducer.font.size", String.valueOf(fontSize));
        props.put("kaptcha.textproducer.noise.color", noiseColor);
        Config cfg = new Config(props);
        DefaultKaptcha kp = new DefaultKaptcha();
        kp.setConfig(cfg);
        return kp;
    }
}
