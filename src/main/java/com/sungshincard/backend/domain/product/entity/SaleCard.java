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
@Table(name = "sale_card")
@EntityListeners(AuditingEntityListener.class)
public class SaleCard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_member_id", nullable = false)
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_master_id", nullable = false)
    private CardMaster cardMaster;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_grade", nullable = false)
    private ConditionGrade conditionGrade;

    @Enumerated(EnumType.STRING)
    @Column(name = "grading_company", nullable = false)
    private GradingCompany gradingCompany;

    @Column(name = "grading_score", length = 20)
    private String gradingScore;

    @Column(name = "certification_no", length = 100)
    private String certificationNo;

    @Column(nullable = false)
    private Long price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Column(name = "view_count", nullable = false)
    @Builder.Default
    private Integer viewCount = 0;

    @Column(name = "favorite_count", nullable = false)
    @Builder.Default
    private Integer favoriteCount = 0;

    public enum ConditionGrade {
        S, A, B, C, D
    }

    public enum GradingCompany {
        NONE, PSA, BGS, CGC
    }

    public enum Status {
        ACTIVE, RESERVED, SOLD, HIDDEN, DELETED
    }
}
