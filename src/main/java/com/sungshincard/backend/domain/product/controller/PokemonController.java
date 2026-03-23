package com.sungshincard.backend.domain.product.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.product.dto.PokemonDto;
import com.sungshincard.backend.domain.product.service.PokemonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pokemons")
@RequiredArgsConstructor
public class PokemonController {

    private final PokemonService pokemonService;

    @GetMapping
    public ApiResponse<List<PokemonDto>> getAllPokemons() {
        return ApiResponse.success(pokemonService.getAllPokemons());
    }

    @GetMapping("/{id}")
    public ApiResponse<PokemonDto> getPokemonById(@PathVariable Long id) {
        return ApiResponse.success(pokemonService.getPokemonById(id));
    }
}
