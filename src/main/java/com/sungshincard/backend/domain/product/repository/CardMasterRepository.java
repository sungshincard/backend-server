package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.product.entity.CardMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardMasterRepository extends JpaRepository<CardMaster, Long> {
    List<CardMaster> findByPokemonId(Long pokemonId);
}
