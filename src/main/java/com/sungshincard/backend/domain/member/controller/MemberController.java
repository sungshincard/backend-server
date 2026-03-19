package com.sungshincard.backend.domain.member.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ApiResponse<Long> join(@RequestBody @Valid JoinRequest request) {
        Long memberId = memberService.join(request.getEmail(), request.getPassword(), request.getNickname());
        return ApiResponse.success(memberId, "Registration successful.");
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody @Valid LoginRequest request) {
        String token = memberService.login(request.getEmail(), request.getPassword());
        return ApiResponse.success(token, "Login successful.");
    }

    @Getter
    @Setter
    public static class LoginRequest {
        @Email
        @NotBlank
        private String email;
        @NotBlank
        private String password;
    }

    @Getter
    @Setter
    public static class JoinRequest {
        @Email
        @NotBlank
        private String email;
        @NotBlank
        private String password;
        @NotBlank
        private String nickname;
    }
}
