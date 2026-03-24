package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.product.entity.CardBlock;
import com.sungshincard.backend.domain.product.entity.CardMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardBlockRepository extends JpaRepository<CardBlock, Long> {
    Optional<CardBlock> findByNameAndGameType(String name, CardMaster.GameType gameType);
    List<CardBlock> findAllByGameTypeAndIsActiveTrue(CardMaster.GameType gameType);
}
