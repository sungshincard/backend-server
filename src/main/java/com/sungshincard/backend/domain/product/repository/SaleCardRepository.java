package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.product.entity.SaleCard;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleCardRepository extends JpaRepository<SaleCard, Long> {
    
    @Query("SELECT DISTINCT s FROM SaleCard s " +
           "JOIN FETCH s.seller " +
           "LEFT JOIN FETCH s.images " +
           "WHERE s.seller.id = :sellerId")
    List<SaleCard> findAllBySellerId(@Param("sellerId") Long sellerId);
    
    List<SaleCard> findAllByCardMasterIdAndStatusOrderByPriceAsc(Long cardMasterId, SaleCard.Status status);

    List<SaleCard> findAllByCardMasterIdAndStatusAndConditionGradeOrderByPriceAsc(
            Long cardMasterId, SaleCard.Status status, SaleCard.ConditionGrade conditionGrade);
    
    @EntityGraph(attributePaths = {"cardMaster", "seller", "images"})
    List<SaleCard> findTop8ByStatusOrderByCreatedAtDesc(SaleCard.Status status);
    
    @Query("SELECT s FROM SaleCard s JOIN FETCH s.cardMaster JOIN FETCH s.seller WHERE s.id = :id")
    SaleCard findByIdWithDetails(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SaleCard s WHERE s.id = :id")
    Optional<SaleCard> findByIdWithLock(@Param("id") Long id);
}
