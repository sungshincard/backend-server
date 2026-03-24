package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.product.entity.CardMaster;
import com.sungshincard.backend.domain.product.entity.CardRarity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRarityRepository extends JpaRepository<CardRarity, Long> {
    Optional<CardRarity> findByNameAndGameType(String name, CardMaster.GameType gameType);
    List<CardRarity> findAllByGameTypeAndIsActiveTrue(CardMaster.GameType gameType);
}
