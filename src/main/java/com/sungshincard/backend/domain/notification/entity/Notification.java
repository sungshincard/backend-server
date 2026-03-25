package com.sungshincard.backend.domain.notification.entity;

import com.sungshincard.backend.common.entity.BaseTimeEntity;
import com.sungshincard.backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "notification")
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
    private String content;

    private String relatedUrl;

    @Column(nullable = false)
    @Builder.Default
    private boolean isRead = false;

    public enum NotificationType {
        NEW_LISTING, ORDER_STATUS, INQUIRY, DISPUTE
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
