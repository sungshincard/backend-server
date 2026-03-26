package com.sungshincard.backend.domain.product.dto;

import com.sungshincard.backend.domain.product.entity.SaleCard;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleCardResponseDto {
    private Long id;
    private String title;
    private String description;
    private SaleCard.ConditionGrade conditionGrade;
    private Long price;
    private SaleCard.Status status;
    private String sellerNickname;
    private LocalDateTime createdAt;
    private List<String> imageUrls;
    private Integer viewCount;
    private Integer favoriteCount;
    private Boolean isWatched;
    private CardMasterDto cardMaster;

    public static SaleCardResponseDto from(SaleCard saleCard) {
        return from(saleCard, true, false);
    }

    public static SaleCardResponseDto from(SaleCard saleCard, boolean includeCardMaster, boolean isWatched) {
        return SaleCardResponseDto.builder()
                .id(saleCard.getId())
                .title(saleCard.getTitle())
                .description(saleCard.getDescription())
                .conditionGrade(saleCard.getConditionGrade())
                .price(saleCard.getPrice())
                .status(saleCard.getStatus())
                .sellerNickname(saleCard.getSeller().getNickname())
                .createdAt(saleCard.getCreatedAt())
                .viewCount(saleCard.getViewCount())
                .favoriteCount(saleCard.getFavoriteCount())
                .isWatched(isWatched)
                .imageUrls(saleCard.getImages().stream()
                        .map(image -> image.getImageUrl())
                        .collect(Collectors.toList()))
                .cardMaster(includeCardMaster ? CardMasterDto.from(saleCard.getCardMaster()) : null)
                .build();
    }
}
