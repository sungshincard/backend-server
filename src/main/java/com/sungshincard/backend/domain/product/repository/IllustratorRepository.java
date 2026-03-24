package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.product.entity.Illustrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IllustratorRepository extends JpaRepository<Illustrator, Long> {
    Optional<Illustrator> findByName(String name);
}
