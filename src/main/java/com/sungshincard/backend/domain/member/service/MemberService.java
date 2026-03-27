package com.sungshincard.backend.domain.member.service;

import com.sungshincard.backend.config.security.JwtTokenProvider;
import com.sungshincard.backend.domain.member.controller.MemberController;
import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.member.entity.Store;
import com.sungshincard.backend.domain.member.repository.MemberRepository;
import com.sungshincard.backend.domain.member.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;
  private final StoreRepository storeRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  @Transactional
  public Long join(String email, String password, String nickname,
      String name, String phoneNumber, String birthDateStr, String genderStr) {

    if (memberRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
    }
    if (memberRepository.existsByPhoneNumber(phoneNumber)) {
      throw new IllegalArgumentException("이미 사용 중인 휴대폰 번호입니다.");
    }

    Member.Gender gender;
    try {
      gender = Member.Gender.valueOf(genderStr.toUpperCase());
    } catch (IllegalArgumentException e) {
      gender = Member.Gender.NONE;
    }

    LocalDate birthDate = LocalDate.parse(birthDateStr); // yyyy-MM-dd

    Member member = Member.builder()
        .email(email)
        .password(passwordEncoder.encode(password))
        .nickname(nickname)
        .name(name)
        .phoneNumber(phoneNumber)
        .birthDate(birthDate)
        .gender(gender)
        .role(Member.Role.USER)
        .build();

    Member savedMember = memberRepository.save(member);
    Store store = Store.builder()
        .member(savedMember)
        .storeName(nickname + "님의 상점")
        .intro("환영합니다!")
        .build();
    storeRepository.save(store);

    return savedMember.getId();
  }

  public String login(String email, String password) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

    if (!passwordEncoder.matches(password, member.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
    }

    if (member.getStatus() != Member.Status.ACTIVE) {
      throw new IllegalArgumentException("이용이 제한된 계정입니다: " + member.getStatus());
    }

    return jwtTokenProvider.createToken(member.getEmail(), member.getRole().name());
  }

  @Transactional(readOnly = true)
  public MemberController.MemberProfileResponse getMemberProfile(String email) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + email));

    Store store = storeRepository.findByMember(member).orElse(null);
    MemberController.StoreResponse storeResponse = null;
    if (store != null) {
        storeResponse = new MemberController.StoreResponse(
            store.getStoreName(),
            store.getIntro(),
            store.getRatingAvg() != null ? store.getRatingAvg().doubleValue() : 0.0,
            store.getReviewCount(),
            store.getCompletedSaleCount()
        );
    } else {
        storeResponse = new MemberController.StoreResponse(
            member.getNickname() + "님의 상점",
            "환영합니다!",
            0.0,
            0,
            0
        );
    }

    return new MemberController.MemberProfileResponse(
        member.getId(),
        member.getEmail(),
        member.getNickname(),
        member.getName(),
        member.getPhoneNumber(),
        member.getBirthDate() != null ? member.getBirthDate().toString() : null,
        member.getGender().name(),
        member.getStatus().name(),
        member.getRole().name(),
        member.getProfileImageUrl(),
        storeResponse);
  }

  @Transactional
  public void updateProfile(String email, String nickname) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    member.updateProfile(nickname);
  }

  @Transactional
  public void updateProfileImage(String email, String profileImageUrl) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    member.updateProfileImage(profileImageUrl);
  }

  @Transactional(readOnly = true)
  public Member findByEmail(String email) {
    return memberRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
  }
}
