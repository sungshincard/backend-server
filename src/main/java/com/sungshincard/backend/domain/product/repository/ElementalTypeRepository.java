package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.product.entity.CardMaster;
import com.sungshincard.backend.domain.product.entity.ElementalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElementalTypeRepository extends JpaRepository<ElementalType, Long> {
    List<ElementalType> findByGameType(CardMaster.GameType gameType);
    Optional<ElementalType> findByNameAndGameType(String name, CardMaster.GameType gameType);
}
