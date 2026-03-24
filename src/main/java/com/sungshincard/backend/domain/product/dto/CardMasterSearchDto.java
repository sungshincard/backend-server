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
    private Long blockId;
    private Long expansionCodeId;
    private Long illustratorId;

    private Integer page = 0;
    private Integer size = 20;

    public int getOffset() {
        return (page != null ? page : 0) * (size != null ? size : 20);
    }
    
    public int getLimit() {
        return (size != null ? size : 20);
    }
}
