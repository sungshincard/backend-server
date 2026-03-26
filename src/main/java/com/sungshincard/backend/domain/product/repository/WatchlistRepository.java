package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.product.entity.CardMaster;
import com.sungshincard.backend.domain.product.entity.SaleCard;
import com.sungshincard.backend.domain.product.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    
    Optional<Watchlist> findByMemberAndCardMaster(Member member, CardMaster cardMaster);
    
    Optional<Watchlist> findByMemberAndSaleCard(Member member, SaleCard saleCard);
    
    void deleteByMemberAndCardMaster(Member member, CardMaster cardMaster);
    
    void deleteByMemberAndSaleCard(Member member, SaleCard saleCard);
    
    Integer countByCardMasterId(Long cardMasterId);
    
    @Query("SELECT w FROM Watchlist w LEFT JOIN FETCH w.cardMaster LEFT JOIN FETCH w.saleCard WHERE w.member = :member")
    List<Watchlist> findByMember(@Param("member") Member member);
    
    List<Watchlist> findAllByCardMaster(CardMaster cardMaster);
    
    boolean existsByMemberAndCardMaster(Member member, CardMaster cardMaster);
    
    boolean existsByMemberAndSaleCard(Member member, SaleCard saleCard);
}
