package com.sungshincard.backend.domain.notification.repository;

import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findTop10ByMemberOrderByCreatedAtDesc(Member member);
    
    long countByMemberAndIsReadFalse(Member member);
    
    @Query("SELECT n FROM Notification n WHERE n.member = :member AND n.isRead = false")
    List<Notification> findUnreadByMember(@Param("member") Member member);
}
