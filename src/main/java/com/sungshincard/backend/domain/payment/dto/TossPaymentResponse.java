package com.sungshincard.backend.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TossPaymentResponse {
    private String paymentKey;
    private String orderId;
    private String orderName;
    private String method;
    private Long totalAmount;
    private String status;
    private String requestedAt;
    private String approvedAt;
    private String useEscrow;
    private String type;
    
    private CardInfo card;
    
    @Getter
    @NoArgsConstructor
    public static class CardInfo {
        private String company;
        private String number;
        private Integer installmentPlanMonths;
        private String approveNo;
        private String useCardPoint;
        private String cardType;
        private String ownerType;
        private String acquireStatus;
        private Boolean isInterestFree;
    }
}
