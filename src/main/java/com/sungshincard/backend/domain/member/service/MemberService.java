package com.sungshincard.backend.domain.member.service;

import com.sungshincard.backend.config.security.JwtTokenProvider;
import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long join(String email, String password, String nickname) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists.");
        }

        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .role(Member.Role.USER)
                .point(0L)
                .build();

        return memberRepository.save(member).getId();
    }

    public String login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("Invalid password.");
        }

        return jwtTokenProvider.createToken(member.getEmail(), member.getRole().name());
    }
}
