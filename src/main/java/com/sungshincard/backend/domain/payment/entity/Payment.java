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
  private Status status = Status.PENDING;

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
    PENDING,           // 결제 처리 중
    PAYMENT_COMPLETED, // 결제 완료
    FAILED,            // 결제 실패
    CANCELLED,         // 결제 취소
    REFUNDED           // 환불 완료
  }
}
