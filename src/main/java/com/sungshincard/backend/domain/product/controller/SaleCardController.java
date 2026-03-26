package com.sungshincard.backend.domain.product.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.member.service.MemberService;
import com.sungshincard.backend.domain.product.dto.SaleCardRequestDto;
import com.sungshincard.backend.domain.product.dto.SaleCardResponseDto;
import com.sungshincard.backend.domain.product.entity.SaleCard;
import com.sungshincard.backend.domain.product.service.SaleCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sale-cards")
@RequiredArgsConstructor
public class SaleCardController {

    private final SaleCardService saleCardService;
    private final MemberService memberService;

    @PostMapping
    public ApiResponse<Long> createSaleCard(
            @RequestBody SaleCardRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Member member = memberService.findByEmail(userDetails.getUsername());
        Long id = saleCardService.createSaleCard(requestDto, member);
        return ApiResponse.success(id);
    }

    @GetMapping("/{id}")
    public ApiResponse<SaleCardResponseDto> getSaleCard(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Member member = (userDetails != null) ? memberService.findByEmail(userDetails.getUsername()) : null;
        return ApiResponse.success(saleCardService.getSaleCard(id, member));
    }

    @GetMapping("/card-master/{cardMasterId}")
    public ApiResponse<List<SaleCardResponseDto>> getSaleCardsByCardMaster(
            @PathVariable Long cardMasterId,
            @RequestParam(required = false) SaleCard.ConditionGrade conditionGrade,
            @AuthenticationPrincipal UserDetails userDetails) {
        Member member = (userDetails != null) ? memberService.findByEmail(userDetails.getUsername()) : null;
        return ApiResponse.success(saleCardService.getSaleCardsByCardMaster(cardMasterId, conditionGrade, member));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> updateSaleCard(
            @PathVariable Long id,
            @RequestBody SaleCardRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Member member = memberService.findByEmail(userDetails.getUsername());
        saleCardService.updateSaleCard(id, requestDto.getTitle(), requestDto.getDescription(), requestDto.getPrice(), member);
        return ApiResponse.success(null);
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam SaleCard.Status status,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Member member = memberService.findByEmail(userDetails.getUsername());
        saleCardService.updateStatus(id, status, member);
        return ApiResponse.success(null);
    }

    @GetMapping("/me")
    public ApiResponse<List<SaleCardResponseDto>> getMySaleCards(
            @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        return ApiResponse.success(saleCardService.getMySaleCards(member));
    }

    @GetMapping("/recent")
    public ApiResponse<List<SaleCardResponseDto>> getRecentSaleCards(
            @AuthenticationPrincipal UserDetails userDetails) {
        Member member = (userDetails != null) ? memberService.findByEmail(userDetails.getUsername()) : null;
        return ApiResponse.success(saleCardService.getRecentSaleCards(member));
    }
}
