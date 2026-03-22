package com.sungshincard.backend.domain.trade.entity;

import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.order.entity.Orders;
import com.sungshincard.backend.domain.product.entity.CardMaster;
import com.sungshincard.backend.domain.product.entity.SaleCard;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "trade_history")
public class TradeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_master_id", nullable = false)
    private CardMaster cardMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_card_id", nullable = false)
    private SaleCard saleCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_member_id", nullable = false)
    private Member buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_member_id", nullable = false)
    private Member seller;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_grade", nullable = false)
    private SaleCard.ConditionGrade conditionGrade;

    @Enumerated(EnumType.STRING)
    @Column(name = "grading_company", nullable = false)
    private SaleCard.GradingCompany gradingCompany;

    @Column(name = "grading_score", length = 20)
    private String gradingScore;

    @Column(name = "sold_price", nullable = false)
    private Long soldPrice;

    @Column(name = "traded_at", nullable = false)
    private LocalDateTime tradedAt;
}
