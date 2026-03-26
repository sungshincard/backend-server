package com.sungshincard.backend.domain.product.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.member.service.MemberService;
import com.sungshincard.backend.domain.product.dto.WatchlistResponseDto;
import com.sungshincard.backend.domain.product.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;
    private final MemberService memberService;

    @PostMapping("/toggle/card-master/{cardMasterId}")
    public ApiResponse<Boolean> toggleCardMasterWatchlist(
            @PathVariable Long cardMasterId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Member member = memberService.findByEmail(userDetails.getUsername());
        boolean isWatched = watchlistService.toggleCardMasterWatchlist(member, cardMasterId);
        return ApiResponse.success(isWatched);
    }

    @PostMapping("/toggle/sale-card/{saleCardId}")
    public ApiResponse<Boolean> toggleSaleCardWatchlist(
            @PathVariable Long saleCardId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Member member = memberService.findByEmail(userDetails.getUsername());
        boolean isWatched = watchlistService.toggleSaleCardWatchlist(member, saleCardId);
        return ApiResponse.success(isWatched);
    }

    @GetMapping("/check/card-master/{cardMasterId}")
    public ApiResponse<Boolean> checkCardWatchlist(
            @PathVariable Long cardMasterId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Member member = memberService.findByEmail(userDetails.getUsername());
        return ApiResponse.success(watchlistService.isCardWatched(member, cardMasterId));
    }

    @GetMapping("/check/sale-card/{saleCardId}")
    public ApiResponse<Boolean> checkSaleCardWatchlist(
            @PathVariable Long saleCardId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Member member = memberService.findByEmail(userDetails.getUsername());
        return ApiResponse.success(watchlistService.isSaleCardWatched(member, saleCardId));
    }

    @GetMapping("/me")
    public ApiResponse<List<WatchlistResponseDto>> getMyWatchlist(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Member member = memberService.findByEmail(userDetails.getUsername());
        return ApiResponse.success(watchlistService.getWatchlist(member));
    }
}
