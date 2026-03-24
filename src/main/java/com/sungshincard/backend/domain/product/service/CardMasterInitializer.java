package com.sungshincard.backend.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(3) // Ensure this runs after DataInitializer(if order applies) to guarantee categories are present
public class CardMasterInitializer implements CommandLineRunner {

    private final CardMasterDataService cardMasterDataService;

    @Override
    public void run(String... args) throws Exception {
        cardMasterDataService.initializeCardData();
    }
}