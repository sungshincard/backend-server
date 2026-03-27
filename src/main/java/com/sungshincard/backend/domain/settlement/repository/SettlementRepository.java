package com.sungshincard.backend.domain.settlement.repository;

import com.sungshincard.backend.domain.order.entity.Orders;
import com.sungshincard.backend.domain.settlement.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {
  Optional<Settlement> findByOrderId(Long orderId);

  boolean existsByOrder(Orders order);

  List<Settlement> findBySellerIdOrderByCreatedAtDesc(Long sellerId);
}
