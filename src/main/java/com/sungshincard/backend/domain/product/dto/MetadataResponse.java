package com.sungshincard.backend.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataResponse {
    private List<String> categories;
    private List<String> sortOptions;
    private List<String> cardTypes;
    private List<String> cardStages;
    private List<String> conditionGrades;
    private List<String> gradingCompanies;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyValue {
        private String key;
        private String value;
    }
}
