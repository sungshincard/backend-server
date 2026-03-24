package com.sungshincard.backend.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@RequiredArgsConstructor
public class PokemonInitializer implements CommandLineRunner {

    private final PokemonDataService pokemonDataService;

    @Override
    public void run(String... args) throws Exception {
        pokemonDataService.initializePokemonData();
    }
}
