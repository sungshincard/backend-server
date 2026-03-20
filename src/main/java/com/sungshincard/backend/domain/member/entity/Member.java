package com.sungshincard.backend.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "member")
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 로그인 이메일 */
    @Column(nullable = false, unique = true)
    private String email;

    /** 비밀번호 해시 */
    @Column(name = "password_hash", nullable = false)
    private String password;

    /** 닉네임 (공개 표시용) */
    @Column(nullable = false, unique = true, length = 100)
    private String nickname;

    /** 실명 */
    @Column(nullable = false, length = 100)
    private String name;

    /** 휴대폰 번호 (인증번호 발송, SMS 알림 용도) */
    @Column(name = "phone_number", nullable = false, unique = true, length = 30)
    private String phoneNumber;

    /** 생년월일 (연령 확인, 이벤트 기준) */
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    /** 성별 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Gender gender = Gender.NONE;

    /** 회원 권한 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    /** 계정 상태 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.ACTIVE;

    /** 프로필 이미지 URL */
    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ---- 업데이트 메서드 ----

    public void updateProfile(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    // ---- Enums ----

    public enum Status {
        ACTIVE, SUSPENDED, WITHDRAWN
    }

    public enum Role {
        USER, ADMIN, OPERATOR
    }

    public enum Gender {
        MALE, FEMALE, NONE
    }
}
