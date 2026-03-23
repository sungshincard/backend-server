package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.product.entity.CardSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardSetRepository extends JpaRepository<CardSet, Long> {
    Optional<CardSet> findByName(String name);
}
