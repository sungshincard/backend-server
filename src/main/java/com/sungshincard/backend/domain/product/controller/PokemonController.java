package com.sungshincard.backend.domain.product.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.product.dto.PokemonDto;
import com.sungshincard.backend.domain.product.service.PokemonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/pokemons")
@RequiredArgsConstructor
public class PokemonController {

    private final PokemonService pokemonService;

    @GetMapping
    public ApiResponse<org.springframework.data.domain.Page<PokemonDto>> getAllPokemons(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(pokemonService.getAllPokemons(region, name, type, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<PokemonDto> getPokemonById(@PathVariable Long id) {
        return ApiResponse.success(pokemonService.getPokemonById(id));
    }
}
