package com.sungshincard.backend.domain.settlement.dto;

import com.sungshincard.backend.domain.settlement.entity.Settlement;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SettlementResponseDto {
    private Long id;
    private Long orderId;
    private String tossOrderId; // 주문 번호 표시용
    private String cardName;    // 판매 상품명
    private Long grossAmount;   // 판매 금액
    private Long feeAmount;     // 수수료
    private Long netAmount;     // 정산 금액
    private Settlement.Status status;
    private LocalDateTime createdAt;
    private LocalDateTime settledAt;

    public static SettlementResponseDto from(Settlement settlement) {
        return SettlementResponseDto.builder()
                .id(settlement.getId())
                .orderId(settlement.getOrder().getId())
                .tossOrderId(settlement.getOrder().getTossOrderId())
                .cardName(settlement.getOrder().getSaleCard().getCardMaster().getCardName())
                .grossAmount(settlement.getGrossAmount())
                .feeAmount(settlement.getFeeAmount())
                .netAmount(settlement.getNetAmount())
                .status(settlement.getStatus())
                .createdAt(settlement.getCreatedAt())
                .settledAt(settlement.getSettledAt())
                .build();
    }
}
