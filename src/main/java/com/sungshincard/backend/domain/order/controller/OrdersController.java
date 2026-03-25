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

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;
    private final MemberService memberService;

    @PostMapping
    public ApiResponse<Long> createOrder(
            @RequestBody OrderRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Member buyer = memberService.findByEmail(userDetails.getUsername());
        Long orderId = ordersService.createOrder(requestDto, buyer);
        return ApiResponse.success(orderId);
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponseDto> getOrder(@PathVariable Long id) {
        return ApiResponse.success(OrderResponseDto.from(ordersService.getOrder(id)));
    }
}
