package com.sungshincard.backend.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddressDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "수령인을 입력해주세요.")
        private String recipientName;

        @NotBlank(message = "연락처를 입력해주세요.")
        private String recipientPhone;

        @NotBlank(message = "우편번호를 입력해주세요.")
        private String zipCode;

        @NotBlank(message = "기본 주소를 입력해주세요.")
        private String address1;

        private String address2;

        @NotNull(message = "기본 배송지 여부를 선택해주세요.")
        private Boolean isDefault;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String recipientName;
        private String recipientPhone;
        private String zipCode;
        private String address1;
        private String address2;
        private Boolean isDefault;
    }
}
