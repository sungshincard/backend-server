package com.sungshincard.backend.domain.order.repository;

import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.order.entity.Orders;
import com.sungshincard.backend.domain.product.entity.SaleCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
  @Query("SELECT DISTINCT o FROM Orders o " +
      "JOIN FETCH o.saleCard s " +
      "JOIN FETCH o.seller " +
      "JOIN FETCH o.buyer " +
      "LEFT JOIN FETCH s.images " +
      "WHERE o.buyer.id = :buyerId " +
      "ORDER BY o.createdAt DESC")
  List<Orders> findBuyOrdersWithSaleCardAndImages(@Param("buyerId") Long buyerId);

  @Query("SELECT DISTINCT o FROM Orders o " +
      "JOIN FETCH o.saleCard s " +
      "JOIN FETCH o.seller " +
      "JOIN FETCH o.buyer " +
      "LEFT JOIN FETCH s.images " +
      "WHERE o.seller.id = :sellerId " +
      "ORDER BY o.createdAt DESC")
  List<Orders> findSellOrdersWithSaleCardAndImages(@Param("sellerId") Long sellerId);

  List<Orders> findAllByBuyerId(Long buyerId);

  List<Orders> findAllBySellerId(Long sellerId);

  java.util.Optional<Orders> findTopBySaleCard_CardMaster_IdAndStatusInOrderByCreatedAtDesc(
      Long cardMasterId, java.util.Collection<Orders.OrderStatus> statuses);

  List<Orders> findAllByStatusAndCreatedAtBefore(Orders.OrderStatus status, java.time.LocalDateTime dateTime);

  java.util.List<Orders> findAllBySaleCardAndStatus(SaleCard saleCard, Orders.OrderStatus status);

  java.util.Optional<Orders> findBySaleCardAndBuyerAndStatus(SaleCard saleCard, Member buyer,
      Orders.OrderStatus status);

  java.util.Optional<Orders> findBySaleCard(SaleCard saleCard);

  java.util.Optional<Orders> findByTossOrderId(String tossOrderId);

  @Query("SELECT o FROM Orders o " +
      "JOIN FETCH o.seller " +
      "JOIN FETCH o.saleCard " +
      "WHERE o.tradeType = :tradeType " +
      "AND o.status = :status " +
      "AND o.updatedAt < :threshold")
  List<Orders> findOrdersForAutoConfirm(@Param("tradeType") Orders.TradeType tradeType, 
                                        @Param("status") Orders.OrderStatus status, 
                                        @Param("threshold") java.time.LocalDateTime threshold);

}