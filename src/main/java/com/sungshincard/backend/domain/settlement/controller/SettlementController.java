package com.sungshincard.backend.domain.settlement.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.member.service.MemberService;
import com.sungshincard.backend.domain.settlement.dto.SettlementResponseDto;
import com.sungshincard.backend.domain.settlement.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;
    private final MemberService memberService;

    @GetMapping("/me")
    public ApiResponse<List<SettlementResponseDto>> getMySettlements(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Member member = memberService.findByEmail(userDetails.getUsername());
        List<SettlementResponseDto> settlements = settlementService.getMySettlements(member.getId());
        return ApiResponse.success(settlements);
    }
}
