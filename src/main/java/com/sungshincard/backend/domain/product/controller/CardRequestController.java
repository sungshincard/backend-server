package com.sungshincard.backend.domain.product.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.product.dto.CardRequestCreateDto;
import com.sungshincard.backend.domain.product.dto.CardRequestDto;
import com.sungshincard.backend.domain.product.dto.CardRequestStatusDto;
import com.sungshincard.backend.domain.product.service.CardRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/card-requests")
@RequiredArgsConstructor
public class CardRequestController {

    private final CardRequestService cardRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<CardRequestDto>> createRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CardRequestCreateDto dto) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        CardRequestDto result = cardRequestService.createCardRequest(memberId, dto);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<CardRequestDto>>> getMyRequests(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        List<CardRequestDto> result = cardRequestService.getMyRequests(memberId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // Admin APIs
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<CardRequestDto>>> getPendingRequests() {
        List<CardRequestDto> result = cardRequestService.getPendingRequests();
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PatchMapping("/{id}/review")
    public ResponseEntity<ApiResponse<CardRequestDto>> reviewRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CardRequestStatusDto dto) {
        Long adminId = Long.parseLong(userDetails.getUsername());
        CardRequestDto result = cardRequestService.reviewCardRequest(id, adminId, dto);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
