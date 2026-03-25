package com.sungshincard.backend.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class LocalFileService implements FileService {

    @Value("${file.upload-dir:./uploads/}")
    private String uploadDir;

    @Override
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        String storedFileName = UUID.randomUUID().toString() + extension;
        File dest = new File(directory.getAbsolutePath() + File.separator + storedFileName);

        try {
            file.transferTo(dest);
            log.info("File uploaded to: {}", dest.getAbsolutePath());
            return storedFileName;
        } catch (IOException e) {
            log.error("Failed to upload file", e);
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files) {
        List<String> storedFileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            String storedFileName = uploadFile(file);
            if (storedFileName != null) {
                storedFileNames.add(storedFileName);
            }
        }
        return storedFileNames;
    }

    @Override
    public void deleteFile(String fileName) {
        File file = new File(uploadDir + fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}
