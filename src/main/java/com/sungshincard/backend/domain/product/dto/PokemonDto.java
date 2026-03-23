package com.sungshincard.backend.domain.product.dto;

import com.sungshincard.backend.domain.product.entity.Pokemon;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PokemonDto {
    private Long id;
    private Integer dexNumber;
    private String name;
    private String type;
    private String region;
    private String imageUrl;

    public static PokemonDto from(Pokemon pokemon) {
        return PokemonDto.builder()
                .id(pokemon.getId())
                .dexNumber(pokemon.getDexNumber())
                .name(pokemon.getName())
                .type(pokemon.getType())
                .region(pokemon.getRegion())
                .imageUrl(pokemon.getImageUrl())
                .build();
    }
}
