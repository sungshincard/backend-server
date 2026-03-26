package com.sungshincard.backend.domain.product.service;

import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.product.entity.CardMaster;
import com.sungshincard.backend.domain.product.entity.Watchlist;
import com.sungshincard.backend.domain.product.repository.CardMasterRepository;
import com.sungshincard.backend.domain.product.repository.WatchlistRepository;
import com.sungshincard.backend.domain.product.dto.CardMasterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final CardMasterRepository cardMasterRepository;

    @Transactional
    public boolean toggleWatchlist(Member member, Long cardMasterId) {
        CardMaster cardMaster = cardMasterRepository.findById(cardMasterId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드 마스터입니다."));

        if (watchlistRepository.existsByMemberAndCardMaster(member, cardMaster)) {
            watchlistRepository.deleteByMemberAndCardMaster(member, cardMaster);
            return false; // 제거됨
        } else {
            Watchlist watchlist = Watchlist.builder()
                    .member(member)
                    .cardMaster(cardMaster)
                    .build();
            watchlistRepository.save(watchlist);
            return true; // 등록됨
        }
    }

    public Integer getWatchCount(Long cardMasterId) {
        return watchlistRepository.countByCardMasterId(cardMasterId);
    }

    public boolean isWatched(Member member, Long cardMasterId) {
        CardMaster cardMaster = cardMasterRepository.findById(cardMasterId).orElse(null);
        if (cardMaster == null) return false;
        return watchlistRepository.existsByMemberAndCardMaster(member, cardMaster);
    }

    @Transactional(readOnly = true)
    public List<CardMasterDto> getWatchlist(Member member) {
        return watchlistRepository.findByMember(member).stream()
                .map(w -> CardMasterDto.from(w.getCardMaster()))
                .collect(Collectors.toList());
    }
}
