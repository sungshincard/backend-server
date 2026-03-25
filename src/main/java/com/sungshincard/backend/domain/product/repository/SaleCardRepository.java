package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.product.entity.SaleCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleCardRepository extends JpaRepository<SaleCard, Long> {
    
    List<SaleCard> findAllByCardMasterIdAndStatusOrderByPriceAsc(Long cardMasterId, SaleCard.Status status);

    List<SaleCard> findAllByCardMasterIdAndStatusAndConditionGradeOrderByPriceAsc(
            Long cardMasterId, SaleCard.Status status, SaleCard.ConditionGrade conditionGrade);
    
    @Query("SELECT s FROM SaleCard s JOIN FETCH s.cardMaster JOIN FETCH s.seller WHERE s.id = :id")
    SaleCard findByIdWithDetails(@Param("id") Long id);
}
