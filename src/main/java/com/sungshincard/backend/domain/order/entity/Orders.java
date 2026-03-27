package com.sungshincard.backend.domain.order.entity;

import com.sungshincard.backend.common.entity.BaseTimeEntity;
import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.product.entity.SaleCard;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Orders extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "toss_order_id")
    private String tossOrderId; // 토스 결제용 고유 주문 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_member_id", nullable = false)
    private Member buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_member_id", nullable = false)
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_card_id", nullable = false)
    private SaleCard saleCard;

    @Column(nullable = false)
    private Long itemPrice; // 순수 상품 가액

    @Column(nullable = false)
    private Long totalPrice; // 결제 총액 (itemPrice + shippingFee + serviceFee)

    @Column(nullable = false)
    private Long shippingFee;

    @Column(nullable = false)
    private Long serviceFee;

    @Column(nullable = false)
    private Long settlementAmount; // 정산 예정액 (itemPrice + shippingFee)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SettlementStatus settlementStatus = SettlementStatus.WAITING;

    // --- 배송지 정보 (주문 시점 스냅샷) ---
    @Column(nullable = false)
    private String receiverName;

    @Column(nullable = false)
    private String receiverPhone;

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String detailAddress;

    @Column
    private String shippingMessage;

    // --- 결제 정보 ---
    @Column
    private String paymentKey; // PG사 승인 번호

    @Column
    private String paymentMethod; // CARD, TRANSFER 등

    @Column
    private java.time.LocalDateTime paidAt;

    // --- 배송 추적 정보 ---
    @Column
    private String carrier; // 택배사

    @Column
    private String trackingNumber; // 송장 번호

    @Column
    private java.time.LocalDateTime shippedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeType tradeType;

    public enum OrderStatus {
        PENDING, // 결제 처리 중
        PAYMENT_COMPLETED, // 결제 완료 (돈이 플랫폼에 묶인 상태)
        SHIPPING, // 배송 중 (택배 거래 전용)
        DELIVERED, // 배송 완료 (택배 거래 전용)
        PURCHASE_CONFIRMED, // 구매 확정 (정산 대기 상태)
        SETTLEMENT_DONE,   // 정산 완료 (실제 판매자에게 입금됨)
        CANCELLED, // 주문 취소
        DISPUTED // 분쟁 중 (신고 접수 시 정산 보류)
    }

    public enum TradeType {
        DELIVERY, FACE_TO_FACE
    }

    public enum SettlementStatus {
        WAITING, COMPLETED, HELD, CANCELLED
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public void updateSettlementStatus(SettlementStatus status) {
        this.settlementStatus = status;
    }

    public void updateTracking(String carrier, String trackingNumber) {
        this.carrier = carrier;
        this.trackingNumber = trackingNumber;
        this.shippedAt = java.time.LocalDateTime.now();
        this.status = OrderStatus.SHIPPING;
    }

    public void updatePaymentInfo(String paymentKey, String paymentMethod) {
        this.paymentKey = paymentKey;
        this.paymentMethod = paymentMethod;
        this.paidAt = java.time.LocalDateTime.now();
        this.status = OrderStatus.PAYMENT_COMPLETED;
    }

    public void updateTossOrderId(String tossOrderId) {
        this.tossOrderId = tossOrderId;
    }
}
