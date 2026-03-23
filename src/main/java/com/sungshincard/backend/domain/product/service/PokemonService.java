package com.sungshincard.backend.domain.product.service;

import com.sungshincard.backend.domain.product.dto.PokemonDto;
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

    public List<PokemonDto> getAllPokemons() {
        return pokemonRepository.findAll().stream()
                .map(PokemonDto::from)
                .collect(Collectors.toList());
    }
    
    public PokemonDto getPokemonById(Long id) {
        return pokemonRepository.findById(id)
                .map(PokemonDto::from)
                .orElseThrow(() -> new IllegalArgumentException("Pokemon not found"));
    }
}
