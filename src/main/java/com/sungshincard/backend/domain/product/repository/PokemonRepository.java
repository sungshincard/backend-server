package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.product.entity.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    List<Pokemon> findAllByRegion(String region);
    List<Pokemon> findAllByNameContaining(String name);
    List<Pokemon> findAllByRegionAndNameContaining(String region, String name);
    boolean existsByRegionIsNull();
}
