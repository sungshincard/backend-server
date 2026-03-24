package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.product.entity.CardExpansionCode;
import com.sungshincard.backend.domain.product.entity.CardMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardExpansionCodeRepository extends JpaRepository<CardExpansionCode, Long> {
    Optional<CardExpansionCode> findByNameAndGameType(String name, CardMaster.GameType gameType);
    List<CardExpansionCode> findAllByGameTypeAndIsActiveTrue(CardMaster.GameType gameType);
}
