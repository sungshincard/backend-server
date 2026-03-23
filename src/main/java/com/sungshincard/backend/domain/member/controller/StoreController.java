package com.sungshincard.backend.domain.member.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.member.dto.StoreDto;
import com.sungshincard.backend.domain.member.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/{storeId}")
    public ApiResponse<StoreDto.Response> getStoreProfile(@PathVariable Long storeId) {
        StoreDto.Response response = storeService.getStoreProfile(storeId);
        return ApiResponse.success(response, "상점 정보 조회 성공");
    }

    @PatchMapping("/me")
    public ApiResponse<StoreDto.Response> updateMyStore(
            Principal principal,
            @RequestBody @Valid StoreDto.Request request) {
        StoreDto.Response response = storeService.updateMyStore(principal.getName(), request);
        return ApiResponse.success(response, "상점 정보 업데이트 성공");
    }
}
