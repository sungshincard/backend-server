package com.sungshincard.backend.domain.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardMasterSearchDto {
    private String gameType;
    private String cardName;
    private String setName;
    private String cardNumber;
    private String rarity;
    private String pokemonCardType;
    private String subType;
    private String type;
    private String evolutionStage;
}
