package com.sungshincard.backend.domain.order.repository;

import com.sungshincard.backend.domain.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByBuyerId(Long buyerId);
    List<Orders> findAllBySellerId(Long sellerId);

    java.util.Optional<Orders> findTopBySaleCard_CardMaster_IdAndStatusInOrderByCreatedAtDesc(
            Long cardMasterId, java.util.Collection<Orders.OrderStatus> statuses);
}
