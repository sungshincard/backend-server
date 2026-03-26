package com.sungshincard.backend.domain.product.dto;

import com.sungshincard.backend.domain.product.entity.Watchlist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistResponseDto {
    private Long id;
    private Watchlist.WatchType watchType;
    private CardMasterDto cardMaster;
    private SaleCardResponseDto saleCard;

    public static WatchlistResponseDto from(Watchlist watchlist) {
        return WatchlistResponseDto.builder()
                .id(watchlist.getId())
                .watchType(watchlist.getWatchType())
                .cardMaster(watchlist.getCardMaster() != null ? CardMasterDto.from(watchlist.getCardMaster()) : null)
                .saleCard(watchlist.getSaleCard() != null ? SaleCardResponseDto.from(watchlist.getSaleCard()) : null)
                .build();
    }
}
