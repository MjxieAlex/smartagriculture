package com.example.smartagriculture.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: MjXie
 * @Date: 2025/05/16/13:49
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    /**
     * 文件上传目录
     */
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
