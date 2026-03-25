package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.product.entity.CardMaster;
import com.sungshincard.backend.domain.product.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    
    Optional<Watchlist> findByMemberAndCardMaster(Member member, CardMaster cardMaster);
    
    void deleteByMemberAndCardMaster(Member member, CardMaster cardMaster);
    
    Integer countByCardMasterId(Long cardMasterId);
    
    List<Watchlist> findByMember(Member member);
    
    List<Watchlist> findAllByCardMaster(CardMaster cardMaster);
    
    boolean existsByMemberAndCardMaster(Member member, CardMaster cardMaster);
}
