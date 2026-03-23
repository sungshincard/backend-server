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
}
