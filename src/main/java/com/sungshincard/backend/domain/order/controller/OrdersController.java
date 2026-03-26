package com.sungshincard.backend.domain.order.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.member.service.MemberService;
import com.sungshincard.backend.domain.order.dto.OrderRequestDto;
import com.sungshincard.backend.domain.order.dto.OrderResponseDto;
import com.sungshincard.backend.domain.order.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;
    private final MemberService memberService;

    @PostMapping
    public ApiResponse<OrderResponseDto> createOrder(
            @RequestBody OrderRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Member buyer = memberService.findByEmail(userDetails.getUsername());
        OrderResponseDto responseDto = ordersService.createOrder(requestDto, buyer);
        return ApiResponse.success(responseDto);
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponseDto> getOrder(@PathVariable Long id) {
        return ApiResponse.success(ordersService.getOrder(id));
    }

    @GetMapping("/buy")
    public ApiResponse<List<OrderResponseDto>> getPurchaseHistory(
            @AuthenticationPrincipal UserDetails userDetails) {
        Member buyer = memberService.findByEmail(userDetails.getUsername());
        return ApiResponse.success(ordersService.getPurchaseHistory(buyer));
    }

    @GetMapping("/sell")
    public ApiResponse<List<OrderResponseDto>> getSalesHistory(
            @AuthenticationPrincipal UserDetails userDetails) {
        Member seller = memberService.findByEmail(userDetails.getUsername());
        return ApiResponse.success(ordersService.getSalesHistory(seller));
    }

    @PutMapping("/{id}/shipping")
    public ApiResponse<Void> updateShipping(
            @PathVariable Long id,
            @RequestParam String carrier,
            @RequestParam String trackingNumber) {
        
        ordersService.updateShippingInfo(id, carrier, trackingNumber);
        return ApiResponse.success(null, "배송 정보가 등록되었습니다.");
    }
}
