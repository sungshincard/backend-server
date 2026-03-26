package com.sungshincard.backend.domain.order.dto;

import com.sungshincard.backend.domain.order.entity.Orders;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private String tossOrderId;
    private Long saleCardId;
    private String saleCardTitle;
    private String sellerNickname;
    private String buyerNickname;
    private Long itemPrice;
    private Long totalPrice;
    private Long shippingFee;
    private Long serviceFee;
    private Long settlementAmount;
    private Orders.SettlementStatus settlementStatus;
    private Orders.OrderStatus status;
    private Orders.TradeType tradeType;
    
    // 배송 정보
    private String receiverName;
    private String receiverPhone;
    private String zipCode;
    private String address;
    private String detailAddress;
    private String shippingMessage;
    
    // 결제/배송 추적
    private String paymentMethod;
    private LocalDateTime paidAt;
    private String carrier;
    private String trackingNumber;
    private LocalDateTime shippedAt;

    public static OrderResponseDto from(Orders order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .tossOrderId(order.getTossOrderId())
                .saleCardId(order.getSaleCard().getId())
                .saleCardTitle(order.getSaleCard().getTitle())
                .sellerNickname(order.getSeller().getNickname())
                .buyerNickname(order.getBuyer().getNickname())
                .itemPrice(order.getItemPrice())
                .totalPrice(order.getTotalPrice())
                .shippingFee(order.getShippingFee())
                .serviceFee(order.getServiceFee())
                .settlementAmount(order.getSettlementAmount())
                .settlementStatus(order.getSettlementStatus())
                .status(order.getStatus())
                .tradeType(order.getTradeType())
                .receiverName(order.getReceiverName())
                .receiverPhone(order.getReceiverPhone())
                .zipCode(order.getZipCode())
                .address(order.getAddress())
                .detailAddress(order.getDetailAddress())
                .shippingMessage(order.getShippingMessage())
                .paymentMethod(order.getPaymentMethod())
                .paidAt(order.getPaidAt())
                .carrier(order.getCarrier())
                .trackingNumber(order.getTrackingNumber())
                .shippedAt(order.getShippedAt())
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListDto {
        private Long id;
        private String saleCardTitle;
        private Long itemPrice;
        private Long totalPrice;
        private Orders.OrderStatus status;
        private Orders.TradeType tradeType;
        private String sellerNickname;
        private String buyerNickname;
        private String thumbnailUrl;
        private LocalDateTime orderedAt;
    }
}
