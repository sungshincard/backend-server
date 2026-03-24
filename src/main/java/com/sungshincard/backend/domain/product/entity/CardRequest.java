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
@Table(name = "card_request")
@EntityListeners(AuditingEntityListener.class)
public class CardRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_member_id", nullable = false)
    private Member requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_type", nullable = false)
    private CardMaster.GameType gameType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CardCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "elemental_type_id")
    private ElementalType elementalType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_set_id")
    private CardSet cardSet;

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Column(name = "card_number", length = 100)
    private String cardNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_rarity_id")
    private CardRarity cardRarity;

    private Integer hp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evolution_stage_id")
    private EvolutionStage evolutionStage;

    @Column(length = 200)
    private String illustrator;

    @Column(name = "expansion_code", length = 50)
    private String expansionCode;

    @Column(length = 50)
    private String block;

    @Column(name = "reference_image_url", length = 500)
    private String referenceImageUrl;

    @Column(name = "request_note", length = 500)
    private String requestNote;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private Member reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "reject_reason")
    private String rejectReason;

    public enum Status {
        PENDING, APPROVED, REJECTED
    }
}
