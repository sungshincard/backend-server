package com.sungshincard.backend.domain.product.dto;

import com.sungshincard.backend.domain.product.entity.CardMaster;
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
    private String setName;
    private String cardName;
    private String cardNumber;
    private String referenceImageUrl;
    private String requestNote;
    private String status;
    private Long reviewedById;
    private LocalDateTime reviewedAt;
    private String rejectReason;
    private String rarity;
    private Integer hp;
    private String evolutionStage;
    private String illustrator;
    private String expansionCode;
    private String block;
    private String pokemonCardType;
    private String subType;

    public static CardRequestDto from(CardRequest cardRequest) {
        return CardRequestDto.builder()
                .id(cardRequest.getId())
                .requesterId(cardRequest.getRequester() != null ? cardRequest.getRequester().getId() : null)
                .gameType(cardRequest.getGameType().name())
                .setName(cardRequest.getSetName())
                .cardName(cardRequest.getCardName())
                .cardNumber(cardRequest.getCardNumber())
                .referenceImageUrl(cardRequest.getReferenceImageUrl())
                .requestNote(cardRequest.getRequestNote())
                .status(cardRequest.getStatus().name())
                .reviewedById(cardRequest.getReviewedBy() != null ? cardRequest.getReviewedBy().getId() : null)
                .reviewedAt(cardRequest.getReviewedAt())
                .rejectReason(cardRequest.getRejectReason())
                .rarity(cardRequest.getRarity())
                .hp(cardRequest.getHp())
                .evolutionStage(cardRequest.getEvolutionStage())
                .illustrator(cardRequest.getIllustrator())
                .expansionCode(cardRequest.getExpansionCode())
                .block(cardRequest.getBlock())
                .pokemonCardType(cardRequest.getPokemonCardType() != null ? cardRequest.getPokemonCardType().name() : null)
                .subType(cardRequest.getSubType())
                .build();
    }
}
