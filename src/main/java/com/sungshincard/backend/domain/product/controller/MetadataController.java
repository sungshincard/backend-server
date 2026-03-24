package com.sungshincard.backend.domain.product.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.product.dto.MetadataResponse;
import com.sungshincard.backend.domain.product.entity.CardMaster;
import com.sungshincard.backend.domain.product.entity.SaleCard;
import com.sungshincard.backend.domain.product.repository.*;
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
    private final CardRarityRepository cardRarityRepository;
    private final EvolutionStageRepository evolutionStageRepository;
    private final CardBlockRepository cardBlockRepository;
    private final IllustratorRepository illustratorRepository;
    private final CardExpansionCodeRepository cardExpansionCodeRepository;

    @GetMapping("/cards")
    public ApiResponse<MetadataResponse> getCardMetadata(@RequestParam(defaultValue = "POKEMON") String gameType) {
        CardMaster.GameType type = CardMaster.GameType.valueOf(gameType);

        MetadataResponse response = MetadataResponse.builder()
                .categories(cardCategoryRepository.findAllByGameTypeAndIsActiveTrue(type).stream()
                        .map(c -> MetadataResponse.KeyValue.builder()
                                .id(c.getId().toString())
                                .name(c.getName())
                                .displayName(c.getDisplayName())
                                .build())
                        .collect(Collectors.toList()))
                .elementalTypes(elementalTypeRepository.findAllByGameTypeAndIsActiveTrue(type).stream()
                        .map(t -> MetadataResponse.KeyValue.builder()
                                .id(t.getId().toString())
                                .name(t.getName())
                                .displayName(t.getDisplayName())
                                .build())
                        .collect(Collectors.toList()))
                .cardSets(cardSetRepository.findAllByGameTypeAndIsActiveTrue(type).stream()
                        .map(s -> MetadataResponse.KeyValue.builder()
                                .id(s.getId().toString())
                                .name(s.getName())
                                .displayName(s.getName())
                                .build())
                        .collect(Collectors.toList()))
                .rarities(cardRarityRepository.findAllByGameTypeAndIsActiveTrue(type).stream()
                        .map(r -> MetadataResponse.KeyValue.builder()
                                .id(r.getId().toString())
                                .name(r.getName())
                                .displayName(r.getDisplayName())
                                .build())
                        .collect(Collectors.toList()))
                .evolutionStages(evolutionStageRepository.findAllByGameTypeOrderBySortOrderAsc(type).stream()
                        .map(e -> MetadataResponse.KeyValue.builder()
                                .id(e.getId().toString())
                                .name(e.getName())
                                .displayName(e.getName())
                                .build())
                        .collect(Collectors.toList()))
                .blocks(cardBlockRepository.findAllByGameTypeAndIsActiveTrue(type).stream()
                        .map(b -> MetadataResponse.KeyValue.builder()
                                .id(b.getId().toString())
                                .name(b.getName())
                                .displayName(b.getName())
                                .build())
                        .collect(Collectors.toList()))
                .illustrators(illustratorRepository.findAll().stream()
                        .map(i -> MetadataResponse.KeyValue.builder()
                                .id(i.getId().toString())
                                .name(i.getName())
                                .displayName(i.getName())
                                .build())
                        .collect(Collectors.toList()))
                .expansionCodes(cardExpansionCodeRepository.findAllByGameTypeAndIsActiveTrue(type).stream()
                        .map(ec -> MetadataResponse.KeyValue.builder()
                                .id(ec.getId().toString())
                                .name(ec.getName())
                                .displayName(ec.getName())
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
