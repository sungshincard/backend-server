package com.sungshincard.backend.domain.member.service;

import com.sungshincard.backend.domain.member.dto.StoreDto;
import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.member.entity.Store;
import com.sungshincard.backend.domain.member.repository.MemberRepository;
import com.sungshincard.backend.domain.member.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    public StoreDto.Response getStoreProfile(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상점입니다."));
        return toResponseDto(store);
    }

    @Transactional
    public StoreDto.Response updateMyStore(String email, StoreDto.Request request) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        
        Store store = storeRepository.findByMember(member)
                .orElseThrow(() -> new IllegalArgumentException("상점을 찾을 수 없습니다."));

        Store updatedStore = Store.builder()
                .id(store.getId())
                .member(store.getMember())
                .storeName(request.getStoreName() != null ? request.getStoreName() : store.getStoreName())
                .intro(request.getIntro() != null ? request.getIntro() : store.getIntro())
                .ratingAvg(store.getRatingAvg())
                .reviewCount(store.getReviewCount())
                .completedSaleCount(store.getCompletedSaleCount())
                .build();
        
        storeRepository.save(updatedStore);
        return toResponseDto(updatedStore);
    }

    private StoreDto.Response toResponseDto(Store store) {
        return new StoreDto.Response(
                store.getId(),
                store.getMember().getId(),
                store.getStoreName(),
                store.getIntro(),
                store.getRatingAvg() != null ? store.getRatingAvg().doubleValue() : 0.0,
                store.getReviewCount(),
                store.getCompletedSaleCount()
        );
    }
}
