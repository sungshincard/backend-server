package com.sungshincard.backend.domain.notification.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.member.service.MemberService;
import com.sungshincard.backend.domain.notification.dto.NotificationDto;
import com.sungshincard.backend.domain.notification.service.NotificationService;
import com.sungshincard.backend.domain.notification.service.NotificationSseService;
import com.sungshincard.backend.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationSseService notificationSseService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@RequestParam String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("Invalid token");
        }
        String email = jwtTokenProvider.getEmail(token);
        Member member = memberService.findByEmail(email);
        return notificationSseService.subscribe(member.getId());
    }

    @GetMapping("/me")
    public ApiResponse<List<NotificationDto>> getMyNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        return ApiResponse.success(notificationService.getMyNotifications(member));
    }

    @GetMapping("/me/unread-count")
    public ApiResponse<Long> getUnreadCount(@AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        return ApiResponse.success(notificationService.getUnreadCount(member));
    }

    @PatchMapping("/{id}/read")
    public ApiResponse<Void> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        notificationService.markAsRead(id, member);
        return ApiResponse.success(null);
    }

    @PatchMapping("/me/read-all")
    public ApiResponse<Void> markAllAsRead(@AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        notificationService.markAllAsRead(member);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNotification(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        notificationService.delete(id, member);
        return ApiResponse.success(null);
    }
}
