package com.sungshincard.backend.domain.member.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ApiResponse<Long> join(@RequestBody @Valid JoinRequest request) {
        Long memberId = memberService.join(
                request.getEmail(),
                request.getPassword(),
                request.getNickname(),
                request.getName(),
                request.getPhoneNumber(),
                request.getBirthDate(),
                request.getGender()
        );
        return ApiResponse.success(memberId, "회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody @Valid LoginRequest request) {
        String token = memberService.login(request.getEmail(), request.getPassword());
        return ApiResponse.success(token, "로그인 성공.");
    }

    @GetMapping("/me")
    public ApiResponse<MemberProfileResponse> getMyProfile(Principal principal) {
        MemberProfileResponse profile = memberService.getMemberProfile(principal.getName());
        return ApiResponse.success(profile, "프로필 조회 성공.");
    }

    /** 내 정보 수정 (닉네임만 변경 가능, 이름/전화번호/생년월일/성별은 본인인증 연동 사항으로 변경 불가) */
    @PatchMapping("/me")
    public ApiResponse<Void> updateMyProfile(
            Principal principal,
            @RequestBody @Valid UpdateProfileRequest request) {
        memberService.updateProfile(principal.getName(), request.getNickname());
        return ApiResponse.success(null, "프로필이 수정되었습니다.");
    }

    /** 프로필 이미지 수정 */
    @PatchMapping("/me/profile-image")
    public ApiResponse<Void> updateProfileImage(
            Principal principal,
            @RequestBody UpdateProfileImageRequest request) {
        memberService.updateProfileImage(principal.getName(), request.getProfileImageUrl());
        return ApiResponse.success(null, "프로필 이미지가 변경되었습니다.");
    }

    // ---- Inner DTO Classes ----

    @Getter
    @Setter
    @AllArgsConstructor
    public static class MemberProfileResponse {
        private Long id;
        private String email;
        private String nickname;
        private String name;
        private String phoneNumber;
        private String birthDate;
        private String gender;
        private String status;
        private String role;
        private String profileImageUrl;
        private StoreResponse store;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class StoreResponse {
        private String storeName;
        private String intro;
        private Double ratingAvg;
        private Integer reviewCount;
        private Integer completedSaleCount;
    }

    @Getter
    @Setter
    public static class LoginRequest {
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;
    }

    @Getter
    @Setter
    public static class JoinRequest {
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        @NotBlank(message = "닉네임은 필수입니다.")
        private String nickname;

        /** 실명 */
        @NotBlank(message = "이름은 필수입니다.")
        private String name;

        /** 휴대폰 번호 (예: 010-1234-5678) */
        @NotBlank(message = "휴대폰 번호는 필수입니다.")
        @Pattern(regexp = "^01[016789]-?\\d{3,4}-?\\d{4}$", message = "유효한 휴대폰 번호 형식이 아닙니다.")
        private String phoneNumber;

        /** 생년월일 (yyyy-MM-dd) */
        @NotBlank(message = "생년월일은 필수입니다.")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일은 yyyy-MM-dd 형식이어야 합니다.")
        private String birthDate;

        /** 성별: MALE / FEMALE / NONE */
        @NotNull(message = "성별은 필수입니다.")
        private String gender;
    }

    @Getter
    @Setter
    public static class UpdateProfileRequest {
        @NotBlank(message = "닉네임은 필수입니다.")
        private String nickname;
    }

    @Getter
    @Setter
    public static class UpdateProfileImageRequest {
        private String profileImageUrl;
    }
}
