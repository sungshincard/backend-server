package com.sungshincard.backend.domain.product.service;

import com.sungshincard.backend.domain.product.dto.PokemonDto;
import com.sungshincard.backend.domain.product.repository.PokemonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PokemonService {

    private final PokemonRepository pokemonRepository;

    public org.springframework.data.domain.Page<PokemonDto> getAllPokemons(String region, String name, String type, int page, int size) {
        String regionFilter = "전체".equals(region) ? null : region;
        String nameFilter = (name == null || name.isBlank()) ? null : name;
        String typeFilter = (type == null || "전체".equals(type) || type.isBlank()) ? null : type;

        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return pokemonRepository.searchPokemons(regionFilter, nameFilter, typeFilter, pageable)
                .map(PokemonDto::from);
    }
    
    public PokemonDto getPokemonById(Long id) {
        return pokemonRepository.findById(id)
                .map(PokemonDto::from)
                .orElseThrow(() -> new IllegalArgumentException("Pokemon not found"));
    }
}
