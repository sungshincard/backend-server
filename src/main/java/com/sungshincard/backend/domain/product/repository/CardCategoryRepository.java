package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.product.entity.CardCategory;
import com.sungshincard.backend.domain.product.entity.CardMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardCategoryRepository extends JpaRepository<CardCategory, Long> {
    List<CardCategory> findByParentIsNullAndGameType(CardMaster.GameType gameType);
    Optional<CardCategory> findByNameAndGameType(String name, CardMaster.GameType gameType);
}
