package com.sungshincard.backend.domain.product.entity;

import com.sungshincard.backend.common.entity.BaseTimeEntity;

import com.sungshincard.backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false)
    private Long price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "saleCard", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SaleCardImage> images = new ArrayList<>();

    @Column(name = "view_count", nullable = false)
    @Builder.Default
    private Integer viewCount = 0;

    @Column(name = "favorite_count", nullable = false)
    @Builder.Default
    private Integer favoriteCount = 0;

    public enum ConditionGrade {
        S, A, B, C, D
    }


    public enum Status {
        ACTIVE,   // 판매 중
        PENDING,  // 결제 진행 중 (예약)
        SOLD,     // 판매 완료
        HIDDEN,   // 숨김 처리
        DELETED   // 삭제 처리
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void update(String title, String description, Long price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public void incrementFavoriteCount() {
        this.favoriteCount++;
    }

    public void decrementFavoriteCount() {
        if (this.favoriteCount > 0) {
            this.favoriteCount--;
        }
    }
}
