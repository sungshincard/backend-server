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
    private List<KeyValue> categories;
    private List<KeyValue> elementalTypes;
    private List<KeyValue> cardSets;
    private List<KeyValue> rarities;
    private List<KeyValue> evolutionStages;
    private List<String> sortOptions;
    private List<String> conditionGrades;
    private List<String> gradingCompanies;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyValue {
        private String id;
        private String name;
        private String displayName;
    }
}
