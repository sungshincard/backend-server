package com.sungshincard.backend.common.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.common.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageUploadController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ApiResponse<List<String>> uploadImages(@RequestParam("files") List<MultipartFile> files) {
        List<String> storedFileNames = fileService.uploadFiles(files);
        
        // 프론트엔드에서 바로 접근 가능한 URL 형태로 반환
        List<String> imageUrls = storedFileNames.stream()
                .map(name -> "/uploads/" + name)
                .collect(Collectors.toList());
        
        return ApiResponse.success(imageUrls);
    }
}
