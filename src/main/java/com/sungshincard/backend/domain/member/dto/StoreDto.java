package com.sungshincard.backend.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class StoreDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "상점 이름을 입력해주세요.")
        private String storeName;
        private String intro;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long memberId;
        private String storeName;
        private String intro;
        private Double ratingAvg;
        private Integer reviewCount;
        private Integer completedSaleCount;
    }
}
