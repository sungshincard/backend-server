package com.sungshincard.backend.domain.product.dto;

import lombok.Getter;

@Getter
public class CardRequestCreateDto {
    private String gameType;
    private String setName;
    private String cardName;
    private String cardNumber;
    private String referenceImageUrl;
    private String requestNote;
}
