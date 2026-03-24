package com.sungshincard.backend.domain.product.dto;

import lombok.Getter;

@Getter
public class CardMasterRequestDto {
    private String gameType;
    private Long cardSetId;
    private String cardName;
    private String cardNumber;
    private Long rarityId;
    private String language;
    private String manufacturer;
    private String imageUrl;
    private String description;
    private Long pokemonId;
    private Integer hp;
    private Long evolutionStageId;
    private Long illustratorId;
    private Long expansionCodeId;
    private Long blockId;
    private Long categoryId;
    private Long elementalTypeId;
}
