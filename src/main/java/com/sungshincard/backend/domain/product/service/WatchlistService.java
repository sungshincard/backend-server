package com.sungshincard.backend.domain.product.service;

import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.product.entity.CardMaster;
import com.sungshincard.backend.domain.product.entity.SaleCard;
import com.sungshincard.backend.domain.product.entity.Watchlist;
import com.sungshincard.backend.domain.product.repository.CardMasterRepository;
import com.sungshincard.backend.domain.product.repository.SaleCardRepository;
import com.sungshincard.backend.domain.product.repository.WatchlistRepository;
import com.sungshincard.backend.domain.product.dto.WatchlistResponseDto;
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
    private final SaleCardRepository saleCardRepository;

    @Transactional
    public boolean toggleCardMasterWatchlist(Member member, Long cardMasterId) {
        CardMaster cardMaster = cardMasterRepository.findById(cardMasterId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드 마스터입니다."));

        if (watchlistRepository.existsByMemberAndCardMaster(member, cardMaster)) {
            watchlistRepository.deleteByMemberAndCardMaster(member, cardMaster);
            return false;
        } else {
            Watchlist watchlist = Watchlist.builder()
                    .member(member)
                    .watchType(Watchlist.WatchType.CARD_MASTER)
                    .cardMaster(cardMaster)
                    .build();
            watchlistRepository.save(watchlist);
            return true;
        }
    }

    @Transactional
    public boolean toggleSaleCardWatchlist(Member member, Long saleCardId) {
        SaleCard saleCard = saleCardRepository.findById(saleCardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 판매 카드입니다."));

        if (watchlistRepository.existsByMemberAndSaleCard(member, saleCard)) {
            watchlistRepository.deleteByMemberAndSaleCard(member, saleCard);
            saleCard.decrementFavoriteCount();
            return false;
        } else {
            Watchlist watchlist = Watchlist.builder()
                    .member(member)
                    .watchType(Watchlist.WatchType.SALE_CARD)
                    .saleCard(saleCard)
                    .build();
            watchlistRepository.save(watchlist);
            saleCard.incrementFavoriteCount();
            return true;
        }
    }

    public Integer getWatchCount(Long cardMasterId) {
        return watchlistRepository.countByCardMasterId(cardMasterId);
    }

    public boolean isCardWatched(Member member, Long cardMasterId) {
        CardMaster cardMaster = cardMasterRepository.findById(cardMasterId).orElse(null);
        if (cardMaster == null) return false;
        return watchlistRepository.existsByMemberAndCardMaster(member, cardMaster);
    }

    public boolean isSaleCardWatched(Member member, Long saleCardId) {
        SaleCard saleCard = saleCardRepository.findById(saleCardId).orElse(null);
        if (saleCard == null) return false;
        return watchlistRepository.existsByMemberAndSaleCard(member, saleCard);
    }

    @Transactional(readOnly = true)
    public List<WatchlistResponseDto> getWatchlist(Member member) {
        return watchlistRepository.findByMember(member).stream()
                .map(WatchlistResponseDto::from)
                .collect(Collectors.toList());
    }
}
