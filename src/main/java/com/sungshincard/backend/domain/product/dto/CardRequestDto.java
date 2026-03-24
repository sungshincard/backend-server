package com.sungshincard.backend.domain.product.dto;

import com.sungshincard.backend.domain.product.entity.CardRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequestDto {
    private Long id;
    private Long requesterId;
    private String gameType;
    private Long categoryId;
    private String categoryName;
    private Long elementalTypeId;
    private String elementalTypeName;
    private Long cardSetId;
    private String cardSetName;
    private String cardName;
    private String cardNumber;
    private String referenceImageUrl;
    private String requestNote;
    private String status;
    private Long reviewedById;
    private LocalDateTime reviewedAt;
    private String rejectReason;
    private Long rarityId;
    private String rarity;
    private Integer hp;
    private Long evolutionStageId;
    private String evolutionStage;
    private String illustrator;
    private String expansionCode;
    private String block;
    private String type;
    private String pokemonCardType;
    private String subType;

    public static CardRequestDto from(CardRequest cardRequest) {
        return CardRequestDto.builder()
                .id(cardRequest.getId())
                .requesterId(cardRequest.getRequester() != null ? cardRequest.getRequester().getId() : null)
                .gameType(cardRequest.getGameType().name())
                .cardSetId(cardRequest.getCardSet() != null ? cardRequest.getCardSet().getId() : null)
                .cardSetName(cardRequest.getCardSet() != null ? cardRequest.getCardSet().getName() : null)
                .categoryId(cardRequest.getCategory() != null ? cardRequest.getCategory().getId() : null)
                .categoryName(cardRequest.getCategory() != null ? cardRequest.getCategory().getDisplayName() : null)
                .elementalTypeId(cardRequest.getElementalType() != null ? cardRequest.getElementalType().getId() : null)
                .elementalTypeName(cardRequest.getElementalType() != null ? cardRequest.getElementalType().getDisplayName() : null)
                .cardName(cardRequest.getCardName())
                .cardNumber(cardRequest.getCardNumber())
                .referenceImageUrl(cardRequest.getReferenceImageUrl())
                .requestNote(cardRequest.getRequestNote())
                .status(cardRequest.getStatus().name())
                .reviewedById(cardRequest.getReviewedBy() != null ? cardRequest.getReviewedBy().getId() : null)
                .reviewedAt(cardRequest.getReviewedAt())
                .rejectReason(cardRequest.getRejectReason())
                .rarityId(cardRequest.getCardRarity() != null ? cardRequest.getCardRarity().getId() : null)
                .rarity(cardRequest.getCardRarity() != null ? cardRequest.getCardRarity().getDisplayName() : null)
                .hp(cardRequest.getHp())
                .evolutionStageId(cardRequest.getEvolutionStage() != null ? cardRequest.getEvolutionStage().getId() : null)
                .evolutionStage(cardRequest.getEvolutionStage() != null ? cardRequest.getEvolutionStage().getName() : null)
                .illustrator(cardRequest.getIllustrator())
                .expansionCode(cardRequest.getExpansionCode())
                .block(cardRequest.getBlock())
                .build();
    }
}
