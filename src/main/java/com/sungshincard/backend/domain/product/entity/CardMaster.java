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
@Table(name = "card_master", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"game_type", "set_name", "card_number", "language"})
})
@EntityListeners(AuditingEntityListener.class)
public class CardMaster extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_type", nullable = false)
    private GameType gameType;

    @Column(name = "set_name", nullable = false)
    private String setName;

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Column(name = "card_number", nullable = false, length = 100)
    private String cardNumber;

    @Column(length = 100)
    private String rarity;

    @Column(length = 50)
    private String language;

    @Column(length = 100)
    private String manufacturer;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Member createdBy;

    public enum GameType {
        POKEMON, YUGIOH, ONE_PIECE, DIGIMON, ETC
    }
}
