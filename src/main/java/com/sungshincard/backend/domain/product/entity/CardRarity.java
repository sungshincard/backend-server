package com.sungshincard.backend.domain.product.entity;

import com.sungshincard.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "card_rarity")
public class CardRarity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g., C, U, R, RR, SAR

    @Column(name = "display_name", nullable = false)
    private String displayName; // e.g., Common, Uncommon, Super Art Rare

    @Column(name = "color_code", length = 20)
    private String colorCode; // Hex color for UI badges

    @Enumerated(EnumType.STRING)
    @Column(name = "game_type", nullable = false)
    private CardMaster.GameType gameType;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
