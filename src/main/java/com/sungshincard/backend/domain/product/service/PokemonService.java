package com.sungshincard.backend.domain.product.service;

import com.sungshincard.backend.domain.product.dto.PokemonDto;
import com.sungshincard.backend.domain.product.entity.Pokemon;
import com.sungshincard.backend.domain.product.repository.PokemonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PokemonService {

    private final PokemonRepository pokemonRepository;

    public List<PokemonDto> getAllPokemons(String region, String name) {
        List<Pokemon> pokemons;
        if (region != null && name != null) {
            pokemons = pokemonRepository.findAllByRegionAndNameContaining(region, name);
        } else if (region != null) {
            pokemons = pokemonRepository.findAllByRegion(region);
        } else if (name != null) {
            pokemons = pokemonRepository.findAllByNameContaining(name);
        } else {
            pokemons = pokemonRepository.findAll();
        }

        return pokemons.stream()
                .map(PokemonDto::from)
                .collect(Collectors.toList());
    }
    
    public PokemonDto getPokemonById(Long id) {
        return pokemonRepository.findById(id)
                .map(PokemonDto::from)
                .orElseThrow(() -> new IllegalArgumentException("Pokemon not found"));
    }
}
