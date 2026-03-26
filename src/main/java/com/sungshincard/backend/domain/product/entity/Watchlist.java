package com.sungshincard.backend.domain.product.entity;

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
@Table(name = "watchlist", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"member_id", "watch_type", "card_master_id", "sale_card_id"})
})
@EntityListeners(AuditingEntityListener.class)
public class Watchlist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "watch_type", nullable = false)
    private WatchType watchType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_master_id", nullable = true)
    private CardMaster cardMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_card_id", nullable = true)
    private SaleCard saleCard;

    public enum WatchType {
        CARD_MASTER, // 카드 모델 자체에 대한 알림 구독
        SALE_CARD    // 특정 매물에 대한 찜하기
    }
}
