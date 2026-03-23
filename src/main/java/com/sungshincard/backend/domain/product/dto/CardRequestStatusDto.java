package com.sungshincard.backend.domain.product.dto;

import lombok.Getter;

@Getter
public class CardRequestStatusDto {
    private String status; // APPROVED, REJECTED
    private String rejectReason;
}
