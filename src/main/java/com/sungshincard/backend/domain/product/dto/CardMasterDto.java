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
    private Long categoryId;
    private String categoryName;
    private Long elementalTypeId;
    private String elementalTypeName;
    private Long cardSetId;
    private String cardSetName;
    private String cardName;
    private String cardNumber;
    private Long rarityId;
    private String rarity;
    private String language;
    private String manufacturer;
    private String imageUrl;
    private String description;
    private Boolean isActive;
    private Long pokemonId;
    private String pokemonName;
    private Integer hp;
    private Long evolutionStageId;
    private String evolutionStage;
    private String illustrator;
    private String expansionCode;
    private String block;
    
    public static CardMasterDto from(CardMaster cardMaster) {
        return CardMasterDto.builder()
                .id(cardMaster.getId())
                .gameType(cardMaster.getGameType().name())
                .cardName(cardMaster.getCardName())
                .cardNumber(cardMaster.getCardNumber())
                .rarityId(cardMaster.getCardRarity() != null ? cardMaster.getCardRarity().getId() : null)
                .rarity(cardMaster.getCardRarity() != null ? cardMaster.getCardRarity().getDisplayName() : null)
                .language(cardMaster.getLanguage())
                .manufacturer(cardMaster.getManufacturer())
                .imageUrl(cardMaster.getImageUrl())
                .description(cardMaster.getDescription())
                .isActive(cardMaster.getIsActive())
                .pokemonId(cardMaster.getPokemon() != null ? cardMaster.getPokemon().getId() : null)
                .pokemonName(cardMaster.getPokemon() != null ? cardMaster.getPokemon().getName() : null)
                .hp(cardMaster.getHp())
                .evolutionStageId(cardMaster.getEvolutionStage() != null ? cardMaster.getEvolutionStage().getId() : null)
                .evolutionStage(cardMaster.getEvolutionStage() != null ? cardMaster.getEvolutionStage().getName() : null)
                .cardSetId(cardMaster.getCardSet() != null ? cardMaster.getCardSet().getId() : null)
                .cardSetName(cardMaster.getCardSet() != null ? cardMaster.getCardSet().getName() : null)
                .categoryId(cardMaster.getCategory() != null ? cardMaster.getCategory().getId() : null)
                .categoryName(cardMaster.getCategory() != null ? cardMaster.getCategory().getDisplayName() : null)
                .elementalTypeId(cardMaster.getElementalType() != null ? cardMaster.getElementalType().getId() : null)
                .elementalTypeName(cardMaster.getElementalType() != null ? cardMaster.getElementalType().getDisplayName() : null)
                .illustrator(cardMaster.getIllustrator())
                .expansionCode(cardMaster.getExpansionCode())
                .block(cardMaster.getBlock())
                .build();
    }
}
