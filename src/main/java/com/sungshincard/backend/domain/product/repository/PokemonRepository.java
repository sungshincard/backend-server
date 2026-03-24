package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.product.entity.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    
    @Query("SELECT p FROM Pokemon p WHERE " +
           "(:region IS NULL OR p.region = :region) AND " +
           "(:name IS NULL OR p.name LIKE CONCAT('%', :name, '%')) AND " +
           "(:type IS NULL OR p.type LIKE CONCAT('%', :type, '%'))")
    List<Pokemon> searchPokemons(
            @Param("region") String region, 
            @Param("name") String name, 
            @Param("type") String type);

    boolean existsByRegionIsNull();

    java.util.Optional<Pokemon> findByName(String name);
}
