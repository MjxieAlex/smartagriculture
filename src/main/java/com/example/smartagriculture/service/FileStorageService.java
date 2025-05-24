package com.example.smartagriculture.service;

import com.example.smartagriculture.config.FileStorageProperties;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: MjXie
 * @Date: 2025/05/16/13:50
 * @Description:
 */
@Service
public class FileStorageService {

    private final Path uploadDir;

    public FileStorageService(FileStorageProperties props) {
        this.uploadDir = Paths.get(props.getUploadDir()).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() throws IOException {
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("上传文件为空");
        }

        String ext = FilenameUtils.getExtension(
                StringUtils.cleanPath(file.getOriginalFilename()));
        String newName = UUID.randomUUID().toString() + (ext.isEmpty() ? "" : "." + ext);

        Path target = uploadDir.resolve(newName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return newName;
    }

    public Resource loadFileAsResource(String fileName) throws IOException {
        Path filePath = uploadDir.resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("文件不存在或不可读：" + fileName);
        }
    }
}
