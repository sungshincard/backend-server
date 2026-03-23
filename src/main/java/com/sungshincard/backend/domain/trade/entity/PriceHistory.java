package com.sungshincard.backend.domain.trade.entity;

import com.sungshincard.backend.common.entity.BaseTimeEntity;

import com.sungshincard.backend.domain.product.entity.CardMaster;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "price_history", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"card_master_id", "condition_grade", "grading_company", "price_date"})
})
@EntityListeners(AuditingEntityListener.class)
public class PriceHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_master_id", nullable = false)
    private CardMaster cardMaster;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_grade", nullable = false)
    private ConditionGrade conditionGrade;

    @Enumerated(EnumType.STRING)
    @Column(name = "grading_company", nullable = false)
    private GradingCompany gradingCompany;

    @Column(name = "price_date", nullable = false)
    private LocalDate priceDate;

    @Column(name = "min_price")
    private Long minPrice;

    @Column(name = "avg_price")
    private Long avgPrice;

    @Column(name = "max_price")
    private Long maxPrice;

    @Column(name = "trade_count", nullable = false)
    @Builder.Default
    private Integer tradeCount = 0;

    public enum ConditionGrade {
        ALL, S, A, B, C, D
    }

    public enum GradingCompany {
        ALL, NONE, PSA, BGS, CGC
    }
}
