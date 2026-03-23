package com.sungshincard.backend.domain.dispute.entity;

import com.sungshincard.backend.common.entity.BaseTimeEntity;

import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.order.entity.Orders;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "dispute")
@EntityListeners(AuditingEntityListener.class)
public class Dispute extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_member_id", nullable = false)
    private Member requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "dispute_type", nullable = false)
    private DisputeType disputeType;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by")
    private Member resolvedBy;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolution_note", length = 500)
    private String resolutionNote;

    @Column(name = "unboxing_video_url", length = 500)
    private String unboxingVideoUrl;

    @Column(name = "evidence_image_url", length = 500)
    private String evidenceImageUrl;

    public enum DisputeType {
        FAKE_SUSPECTED, DAMAGED, NOT_AS_DESCRIBED, DELIVERY_ISSUE, NO_SHIPMENT, REFUND_REQUEST, ETC
    }

    public enum Status {
        OPEN, IN_REVIEW, RESOLVED, REJECTED
    }
}
