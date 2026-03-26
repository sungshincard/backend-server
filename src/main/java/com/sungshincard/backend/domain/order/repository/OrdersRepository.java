package com.sungshincard.backend.domain.order.repository;

import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.order.entity.Orders;
import com.sungshincard.backend.domain.product.entity.SaleCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByBuyerId(Long buyerId);
    List<Orders> findAllBySellerId(Long sellerId);

    java.util.Optional<Orders> findTopBySaleCard_CardMaster_IdAndStatusInOrderByCreatedAtDesc(
            Long cardMasterId, java.util.Collection<Orders.OrderStatus> statuses);

    List<Orders> findAllByStatusAndCreatedAtBefore(Orders.OrderStatus status, java.time.LocalDateTime dateTime);

    java.util.List<Orders> findAllBySaleCardAndStatus(SaleCard saleCard, Orders.OrderStatus status);
    java.util.Optional<Orders> findBySaleCardAndBuyerAndStatus(SaleCard saleCard, Member buyer, Orders.OrderStatus status);
    java.util.Optional<Orders> findBySaleCard(SaleCard saleCard);
    java.util.Optional<Orders> findByTossOrderId(String tossOrderId);
}
