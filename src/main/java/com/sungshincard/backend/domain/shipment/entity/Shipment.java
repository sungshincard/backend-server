package com.sungshincard.backend.domain.shipment.entity;

import com.sungshincard.backend.common.entity.BaseTimeEntity;

import com.sungshincard.backend.domain.member.entity.Address;
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
@Table(name = "shipment")
@EntityListeners(AuditingEntityListener.class)
public class Shipment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @Column(name = "courier_name", length = 100)
    private String courierName;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_address_id")
    private Address senderAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_address_id")
    private Address receiverAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.READY;

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    public enum Status {
        READY,     // 배송 준비 중
        SHIPPING,  // 배송 중
        DELIVERED, // 배송 완료
        LOST,      // 분실
        RETURNED   // 반송
    }

    public void updateStatus(Status status) {
        this.status = status;
        if (status == Status.SHIPPING) {
            this.shippedAt = LocalDateTime.now();
        } else if (status == Status.DELIVERED) {
            this.deliveredAt = LocalDateTime.now();
        }
    }
}
