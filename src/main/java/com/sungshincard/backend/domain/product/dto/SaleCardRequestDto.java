package com.sungshincard.backend.domain.product.dto;

import com.sungshincard.backend.domain.product.entity.SaleCard;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SaleCardRequestDto {
    private Long cardMasterId;
    private String title;
    private String description;
    private SaleCard.ConditionGrade conditionGrade;
    private Long price;
    private List<String> imageUrls;
}
