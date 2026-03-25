package com.sungshincard.backend.domain.product.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.product.dto.CardMasterDto;
import com.sungshincard.backend.domain.product.dto.CardMasterRequestDto;
import com.sungshincard.backend.domain.product.dto.CardMasterSearchDto;
import com.sungshincard.backend.domain.product.service.CardMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/card-masters")
@RequiredArgsConstructor
public class CardMasterController {

  private final CardMasterService cardMasterService;

  @PostMapping
  @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<CardMasterDto>> createCardMaster(@RequestBody CardMasterRequestDto requestDto) {
    CardMasterDto result = cardMasterService.createCardMaster(requestDto);
    return ResponseEntity.ok(ApiResponse.success(result));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<CardMasterDto>> getCardMaster(
      @PathVariable Long id,
      @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
    
    String email = userDetails != null ? userDetails.getUsername() : null;
    CardMasterDto result = cardMasterService.getCardMaster(id, email);
    return ResponseEntity.ok(ApiResponse.success(result));
  }

  @GetMapping("/search")
  public ResponseEntity<ApiResponse<Page<CardMasterDto>>> searchCardMasters(
      @ModelAttribute CardMasterSearchDto searchDto) {
    Page<CardMasterDto> result = cardMasterService.searchCardMasters(searchDto);
    return ResponseEntity.ok(ApiResponse.success(result));
  }

  @GetMapping("/recent")
  public ResponseEntity<ApiResponse<java.util.List<CardMasterDto>>> getRecentCardMasters() {
    java.util.List<CardMasterDto> result = cardMasterService.getRecentCardMasters();
    return ResponseEntity.ok(ApiResponse.success(result));
  }
}
