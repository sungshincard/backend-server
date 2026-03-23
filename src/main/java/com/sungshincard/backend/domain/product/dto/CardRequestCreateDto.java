package com.sungshincard.backend.domain.product.dto;

import lombok.Getter;

@Getter
public class CardRequestCreateDto {
    private String gameType;
    private Long cardSetId;
    private String cardName;
    private String cardNumber;
    private String referenceImageUrl;
    private String requestNote;
    private String rarity;
    private Integer hp;
    private String evolutionStage;
    private String illustrator;
    private String expansionCode;
    private String block;
    private Long categoryId;
    private Long elementalTypeId;
}
