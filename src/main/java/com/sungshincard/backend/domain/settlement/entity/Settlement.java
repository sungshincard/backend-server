package com.sungshincard.backend.domain.settlement.entity;

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
@Table(name = "settlement")
@EntityListeners(AuditingEntityListener.class)
public class Settlement extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false, unique = true)
  private Orders order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_member_id", nullable = false)
  private Member seller;

  @Column(name = "gross_amount", nullable = false)
  private Long grossAmount;

  @Column(name = "fee_amount", nullable = false)
  private Long feeAmount;

  @Column(name = "net_amount", nullable = false)
  private Long netAmount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private Status status = Status.READY;

  @Column(name = "settled_at")
  private LocalDateTime settledAt;

  @Column(name = "hold_reason")
  private String holdReason;

  public enum Status {
    READY, ON_HOLD, COMPLETED, CANCELED
  }
}
