package com.sungshincard.backend.domain.notification.entity;

import com.sungshincard.backend.common.entity.BaseTimeEntity;

import com.sungshincard.backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "notification")
@EntityListeners(AuditingEntityListener.class)
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    public enum NotificationType {
        NEW_LISTING, ORDER_CREATED, PAYMENT_COMPLETED, TRACKING_REGISTERED, 
        SHIPMENT_UPDATED, AUTO_CONFIRMED, COMMENT_ADDED, REVIEW_ADDED, DISPUTE_UPDATED
    }
}
