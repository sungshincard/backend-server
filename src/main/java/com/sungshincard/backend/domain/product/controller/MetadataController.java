package com.sungshincard.backend.domain.product.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.product.dto.MetadataResponse;
import com.sungshincard.backend.domain.product.entity.SaleCard;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/metadata")
public class MetadataController {

    @GetMapping("/cards")
    public ApiResponse<MetadataResponse> getCardMetadata() {
        MetadataResponse response = MetadataResponse.builder()
                .categories(Arrays.asList("포켓몬", "트레이너스", "에너지"))
                .sortOptions(Arrays.asList("최신순", "인기순", "최저가순", "최근 거래순"))
                .cardTypes(Arrays.asList("불꽃", "물", "풀", "전기", "에스퍼", "악", "드래곤"))
                .cardStages(Arrays.asList("기본", "1진화", "2진화", "포켓몬 ex", "아이템", "서포트"))
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
