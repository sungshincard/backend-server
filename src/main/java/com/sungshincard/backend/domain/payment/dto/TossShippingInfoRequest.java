package com.sungshincard.backend.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TossShippingInfoRequest {
    private String carrier;
    private String trackingNumber;
    private String method; // DELIVERY (기본값)
}
