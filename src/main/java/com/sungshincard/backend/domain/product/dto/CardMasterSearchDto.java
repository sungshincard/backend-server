package com.sungshincard.backend.domain.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardMasterSearchDto {
    private String gameType;
    private String cardName;
    private Long cardSetId;
    private String cardNumber;
    private String rarity;
    private Long categoryId;
    private Long elementalTypeId;
    private String evolutionStage;
    private Long pokemonId;
}
