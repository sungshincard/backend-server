package com.sungshincard.backend.domain.product.dto;

import lombok.Getter;

@Getter
public class CardMasterRequestDto {
    private String gameType;
    private String setName;
    private String cardName;
    private String cardNumber;
    private String rarity;
    private String language;
    private String manufacturer;
    private String imageUrl;
    private String description;
    private Long pokemonId;
    private Integer hp;
    private String evolutionStage;
    private String illustrator;
    private String expansionCode;
    private String block;
}
