package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.product.entity.CardMaster;
import com.sungshincard.backend.domain.product.entity.EvolutionStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvolutionStageRepository extends JpaRepository<EvolutionStage, Long> {
    Optional<EvolutionStage> findByNameAndGameType(String name, CardMaster.GameType gameType);
    List<EvolutionStage> findAllByGameTypeOrderBySortOrderAsc(CardMaster.GameType gameType);
}
