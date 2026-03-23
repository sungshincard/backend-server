package com.sungshincard.backend.domain.product.dto;

import com.sungshincard.backend.domain.product.entity.CardMaster;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardMasterDto {
    private Long id;
    private String gameType;
    private String setName;
    private String cardName;
    private String cardNumber;
    private String rarity;
    private String language;
    private String manufacturer;
    private String imageUrl;
    private String description;
    private Boolean isActive;
    private Long pokemonId;
    
    public static CardMasterDto from(CardMaster cardMaster) {
        return CardMasterDto.builder()
                .id(cardMaster.getId())
                .gameType(cardMaster.getGameType().name())
                .setName(cardMaster.getSetName())
                .cardName(cardMaster.getCardName())
                .cardNumber(cardMaster.getCardNumber())
                .rarity(cardMaster.getRarity())
                .language(cardMaster.getLanguage())
                .manufacturer(cardMaster.getManufacturer())
                .imageUrl(cardMaster.getImageUrl())
                .description(cardMaster.getDescription())
                .isActive(cardMaster.getIsActive())
                .pokemonId(cardMaster.getPokemon() != null ? cardMaster.getPokemon().getId() : null)
                .build();
    }
}
