package com.sungshincard.backend.domain.payment.entity;

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
@Table(name = "payment")
@EntityListeners(AuditingEntityListener.class)
public class Payment extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Orders order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payer_member_id", nullable = false)
  private Member payer;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method", nullable = false)
  private PaymentMethod paymentMethod;

  @Column(nullable = false)
  private Long amount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private Status status = Status.READY;

  @Column(length = 100)
  private String provider;

  @Column(name = "provider_tx_id", length = 150)
  private String providerTxId;

  @Column(name = "paid_at")
  private LocalDateTime paidAt;

  @Column(name = "canceled_at")
  private LocalDateTime canceledAt;

  public enum PaymentMethod {
    CARD, BANK_TRANSFER, POINT
  }

  public enum Status {
    READY, PAID, FAILED, CANCELED, REFUNDED
  }
}
