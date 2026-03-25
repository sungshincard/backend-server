package com.sungshincard.backend.domain.product.dto;

import com.sungshincard.backend.domain.product.entity.CardMaster;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
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
    private Long illustratorId;
    private String illustrator;
    private Long expansionCodeId;
    private String expansionCode;
    private Long blockId;
    private String block;
    private Integer favoriteCount;
    private Boolean isWatched;
    private Long lowestPrice;
    private Long highestPrice;
    private Double averagePrice;
    private Long recentTradePrice;
    private Integer activeListingCount;
    
    public static CardMasterDto from(CardMaster cardMaster, Integer favoriteCount, Boolean isWatched, Long lowestPrice, Long highestPrice, Double averagePrice, Long recentTradePrice, Integer activeListingCount) {
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
                .illustratorId(cardMaster.getIllustrator() != null ? cardMaster.getIllustrator().getId() : null)
                .illustrator(cardMaster.getIllustrator() != null ? cardMaster.getIllustrator().getName() : null)
                .expansionCodeId(cardMaster.getExpansionCode() != null ? cardMaster.getExpansionCode().getId() : null)
                .expansionCode(cardMaster.getExpansionCode() != null ? cardMaster.getExpansionCode().getName() : null)
                .blockId(cardMaster.getBlock() != null ? cardMaster.getBlock().getId() : null)
                .block(cardMaster.getBlock() != null ? cardMaster.getBlock().getName() : null)
                .favoriteCount(favoriteCount)
                .isWatched(isWatched)
                .lowestPrice(lowestPrice)
                .highestPrice(highestPrice)
                .averagePrice(averagePrice)
                .recentTradePrice(recentTradePrice)
                .activeListingCount(activeListingCount)
                .build();
    }
    
    public static CardMasterDto from(CardMaster cardMaster) {
        return from(cardMaster, null, null, null, null, null, null, null);
    }
}
