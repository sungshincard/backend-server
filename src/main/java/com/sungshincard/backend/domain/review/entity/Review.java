package com.sungshincard.backend.domain.review.entity;

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
@Table(name = "review")
@EntityListeners(AuditingEntityListener.class)
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_member_id", nullable = false)
    private Member reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_member_id", nullable = false)
    private Member reviewee;

    @Column(nullable = false)
    private Byte rating;

    @Column(length = 1000)
    private String content;
}
