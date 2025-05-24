package com.example.smartagriculture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SmartagricultureApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartagricultureApplication.class, args);
    }

}
