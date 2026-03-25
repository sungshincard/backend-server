package com.sungshincard.backend.domain.order.dto;

import com.sungshincard.backend.domain.order.entity.Orders;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {
    private Long saleCardId;
    private Orders.TradeType tradeType;
    
    // 배송 정보 추가
    private String receiverName;
    private String receiverPhone;
    private String zipCode;
    private String address;
    private String detailAddress;
    private String shippingMessage;
}
