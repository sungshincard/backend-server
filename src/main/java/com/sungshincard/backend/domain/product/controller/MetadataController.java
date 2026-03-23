package com.sungshincard.backend.domain.product.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.product.dto.MetadataResponse;
import com.sungshincard.backend.domain.product.entity.CardMaster;
import com.sungshincard.backend.domain.product.entity.SaleCard;
import com.sungshincard.backend.domain.product.repository.CardCategoryRepository;
import com.sungshincard.backend.domain.product.repository.CardSetRepository;
import com.sungshincard.backend.domain.product.repository.ElementalTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/metadata")
@RequiredArgsConstructor
public class MetadataController {

    private final CardSetRepository cardSetRepository;
    private final CardCategoryRepository cardCategoryRepository;
    private final ElementalTypeRepository elementalTypeRepository;

    @GetMapping("/cards")
    public ApiResponse<MetadataResponse> getCardMetadata(@RequestParam(defaultValue = "POKEMON") String gameType) {
        CardMaster.GameType type = CardMaster.GameType.valueOf(gameType);

        MetadataResponse response = MetadataResponse.builder()
                .categories(cardCategoryRepository.findAll().stream()
                        .filter(c -> c.getGameType() == type)
                        .map(c -> MetadataResponse.KeyValue.builder()
                                .id(c.getId().toString())
                                .name(c.getName())
                                .displayName(c.getDisplayName())
                                .build())
                        .collect(Collectors.toList()))
                .elementalTypes(elementalTypeRepository.findByGameType(type).stream()
                        .map(t -> MetadataResponse.KeyValue.builder()
                                .id(t.getId().toString())
                                .name(t.getName())
                                .displayName(t.getDisplayName())
                                .build())
                        .collect(Collectors.toList()))
                .cardSets(cardSetRepository.findAll().stream()
                        .filter(s -> s.getGameType() == type)
                        .map(s -> MetadataResponse.KeyValue.builder()
                                .id(s.getId().toString())
                                .name(s.getName())
                                .displayName(s.getName())
                                .build())
                        .collect(Collectors.toList()))
                .sortOptions(Arrays.asList("최신순", "인기순", "최저가순", "최근 거래순"))
                .conditionGrades(Arrays.stream(SaleCard.ConditionGrade.values())
                        .map(Enum::name)
                        .collect(Collectors.toList()))
                .gradingCompanies(Arrays.stream(SaleCard.GradingCompany.values())
                        .map(Enum::name)
                        .collect(Collectors.toList()))
                .build();

        return ApiResponse.success(response);
    }
}
