package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.product.entity.CardRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRequestRepository extends JpaRepository<CardRequest, Long> {
    List<CardRequest> findByRequesterId(Long requesterId);
    List<CardRequest> findByStatus(CardRequest.Status status);
}
