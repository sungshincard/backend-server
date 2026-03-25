package com.sungshincard.backend.domain.product.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.member.service.MemberService;
import com.sungshincard.backend.domain.product.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;
    private final MemberService memberService;

    @PostMapping("/toggle/{cardMasterId}")
    public ApiResponse<Boolean> toggleWatchlist(
            @PathVariable Long cardMasterId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Member member = memberService.findByEmail(userDetails.getUsername());
        boolean isWatched = watchlistService.toggleWatchlist(member, cardMasterId);
        return ApiResponse.success(isWatched);
    }

    @GetMapping("/check/{cardMasterId}")
    public ApiResponse<Boolean> checkWatchlist(
            @PathVariable Long cardMasterId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Member member = memberService.findByEmail(userDetails.getUsername());
        return ApiResponse.success(watchlistService.isWatched(member, cardMasterId));
    }

    @GetMapping("/me")
    public ApiResponse<java.util.List<com.sungshincard.backend.domain.product.dto.CardMasterDto>> getMyWatchlist(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        com.sungshincard.backend.domain.member.entity.Member member = memberService.findByEmail(userDetails.getUsername());
        java.util.List<com.sungshincard.backend.domain.product.entity.Watchlist> watchlist = watchlistService.getWatchlist(member);
        
        java.util.List<com.sungshincard.backend.domain.product.dto.CardMasterDto> dtos = watchlist.stream()
                .map(w -> com.sungshincard.backend.domain.product.dto.CardMasterDto.from(w.getCardMaster()))
                .toList();
                
        return ApiResponse.success(dtos);
    }
}
