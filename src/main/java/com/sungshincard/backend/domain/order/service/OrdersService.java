package com.sungshincard.backend.domain.order.service;

import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.order.dto.OrderRequestDto;
import com.sungshincard.backend.domain.order.dto.OrderResponseDto;
import com.sungshincard.backend.domain.order.entity.Orders;
import com.sungshincard.backend.domain.order.repository.OrdersRepository;
import com.sungshincard.backend.domain.product.entity.SaleCard;
import com.sungshincard.backend.domain.product.repository.SaleCardRepository;
import com.sungshincard.backend.domain.notification.entity.Notification;
import com.sungshincard.backend.domain.notification.service.NotificationService;
import com.sungshincard.backend.domain.payment.entity.Payment;
import com.sungshincard.backend.domain.payment.repository.PaymentRepository;
import com.sungshincard.backend.domain.settlement.entity.Settlement;
import com.sungshincard.backend.domain.settlement.repository.SettlementRepository;
import com.sungshincard.backend.domain.payment.dto.TossShippingInfoRequest;
import com.sungshincard.backend.domain.payment.service.TossPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final SaleCardRepository saleCardRepository;
    private final NotificationService notificationService;
    private final TossPaymentService tossPaymentService;
    private final PaymentRepository paymentRepository;
    private final SettlementRepository settlementRepository;

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto, Member buyer) {
        SaleCard saleCard = saleCardRepository.findByIdWithLock(requestDto.getSaleCardId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 출품 카드입니다."));

        if (saleCard.getStatus() != SaleCard.Status.ACTIVE) {
            // 이미 RESERVED 상태라면, 현재 구매자가 본인인지 확인하여 PENDING 주문이 있으면 해당 ID 반환
            if (saleCard.getStatus() == SaleCard.Status.RESERVED) {
                Orders existingOrder = ordersRepository.findAllBySaleCardAndStatus(saleCard, Orders.OrderStatus.PENDING)
                        .stream()
                        .filter(order -> order.getBuyer().getId().equals(buyer.getId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("이미 다른 사용자가 구매 진행 중인 상품입니다."));
                
                // 기존 주문 재사용 시에도 토스 주문번호는 새로 갱신 (중복 방지)
                String newTossOrderId = existingOrder.getId() + "_" + System.currentTimeMillis();
                existingOrder.updateTossOrderId(newTossOrderId);
                return OrderResponseDto.from(existingOrder);
            }
            throw new IllegalStateException("현재 판매 중인 상품이 아닙니다.");
        }

        if (saleCard.getSeller().getId().equals(buyer.getId())) {
            throw new IllegalStateException("본인의 상품은 구매할 수 없습니다.");
        }

        // 간단한 수수료 계산 (가격의 1.5%)
        long price = saleCard.getPrice();
        long serviceFee = Math.round(price * 0.015);
        long shippingFee = requestDto.getTradeType() == Orders.TradeType.DELIVERY ? 3500 : 0;
        long totalAmount = price + serviceFee + shippingFee;

        Orders order = Orders.builder()
                .buyer(buyer)
                .seller(saleCard.getSeller())
                .saleCard(saleCard)
                .itemPrice(price)
                .totalPrice(totalAmount)
                .serviceFee(serviceFee)
                .shippingFee(shippingFee)
                .settlementAmount(price + shippingFee)
                .tradeType(requestDto.getTradeType())
                .receiverName(requestDto.getReceiverName())
                .receiverPhone(requestDto.getReceiverPhone())
                .zipCode(requestDto.getZipCode())
                .address(requestDto.getAddress())
                .detailAddress(requestDto.getDetailAddress())
                .shippingMessage(requestDto.getShippingMessage())
                .status(Orders.OrderStatus.PENDING) // 초기 상태는 PENDING
                .build();

        Orders savedOrder = ordersRepository.save(order);

        // 토스 결제용 고유 주문번호 생성 및 저장 (DB ID + Timestamp)
        String tossOrderId = savedOrder.getId() + "_" + System.currentTimeMillis();
        savedOrder.updateTossOrderId(tossOrderId);

        // 출품 상태를 RESERVED로 변경
        saleCard.updateStatus(SaleCard.Status.RESERVED);

        // 판매자에게 알림 발송
        notificationService.send(
                saleCard.getSeller(),
                Notification.NotificationType.ORDER_STATUS,
                String.format("[%s] 상품이 판매되었습니다. 결제 금액을 확인해 주세요.", saleCard.getTitle()),
                "/orders/" + savedOrder.getId()
        );

        return OrderResponseDto.from(savedOrder);
    }

    public OrderResponseDto getOrder(Long id) {
        Orders order = ordersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        return OrderResponseDto.from(order);
    }

    /**
     * 결제 완료 처리 (Toss 승인 후 호출)
     */
    @Transactional
    public void completePayment(String tossOrderId, String paymentKey, String paymentMethod) {
        Orders order = ordersRepository.findByTossOrderId(tossOrderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (order.getStatus() != Orders.OrderStatus.PENDING) {
            log.warn("Order is not in PENDING status. current status: {}", order.getStatus());
            return;
        }

        order.updatePaymentInfo(paymentKey, paymentMethod);
        order.getSaleCard().updateStatus(SaleCard.Status.SOLD);

        // 3. Payment 레코드 생성
        Payment payment = Payment.builder()
                .order(order)
                .payer(order.getBuyer())
                .amount(order.getTotalPrice())
                .paymentMethod(mapToPaymentMethod(paymentMethod))
                .provider("TOSS")
                .providerTxId(paymentKey)
                .status(Payment.Status.PAID)
                .paidAt(java.time.LocalDateTime.now())
                .build();
        paymentRepository.save(payment);

        // 4. Settlement 레코드 생성
        Settlement settlement = Settlement.builder()
                .order(order)
                .seller(order.getSeller())
                .grossAmount(order.getTotalPrice())
                .feeAmount(order.getServiceFee())
                .netAmount(order.getSettlementAmount())
                .status(Settlement.Status.READY)
                .build();
        settlementRepository.save(settlement);
        
        // 최종 결제 완료 알림 발송 (판매자)
        notificationService.send(
                order.getSeller(),
                Notification.NotificationType.ORDER_STATUS,
                String.format("[%s] 상품 결제가 완료되었습니다. 배송을 준비해 주세요.", order.getSaleCard().getCardMaster().getCardName()),
                "/orders/" + order.getId()
        );
    }

    private Payment.PaymentMethod mapToPaymentMethod(String method) {
        if (method == null) return Payment.PaymentMethod.CARD;
        if (method.contains("카드")) return Payment.PaymentMethod.CARD;
        if (method.contains("계좌")) return Payment.PaymentMethod.BANK_TRANSFER;
        if (method.contains("포인트")) return Payment.PaymentMethod.POINT;
        return Payment.PaymentMethod.CARD; // 기본값
    }

    @Transactional(readOnly = true)
    public void verifyOrderAmount(String tossOrderId, Long amount) {
        Orders order = ordersRepository.findByTossOrderId(tossOrderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // 멱등성 보장 (이미 결제된 주문은 통과시키지 않음)
        if (order.getStatus() != Orders.OrderStatus.PENDING) {
            log.warn("이미 결제 처리가 진행되었거나 완료된 주문입니다. tossOrderId: {}", tossOrderId);
            throw new IllegalStateException("이미 결제가 완료된 주문입니다.");
        }
        
        if (!order.getTotalPrice().equals(amount)) {
            log.error("Payment Amount Mismatch: DB={} / Request={}", order.getTotalPrice(), amount);
            throw new IllegalArgumentException("결제 요청 금액이 주문 금액과 일치하지 않습니다.");
        }
    }

    /**
     * 배송 정보(송장) 등록 및 에스크로 연동
     */
    @Transactional
    public void updateShippingInfo(Long orderId, String carrier, String trackingNumber) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (order.getStatus() != Orders.OrderStatus.PAID) {
            throw new IllegalStateException("결제 완료 상태에서만 배송 정보를 등록할 수 있습니다.");
        }

        // 1. 주문 데이터 업데이트 (SHIPPED 상태로 변경)
        order.updateTracking(carrier, trackingNumber);

        // 2. 토스 에스크로 배송 정보 등록
        if (order.getPaymentKey() != null) {
            TossShippingInfoRequest tossRequest = TossShippingInfoRequest.builder()
                    .carrier(carrier)
                    .trackingNumber(trackingNumber)
                    .method("DELIVERY")
                    .build();
            tossPaymentService.registerShippingInfo(order.getPaymentKey(), tossRequest);
        }

        // 3. 구매자에게 배송 시작 알림 발송
        notificationService.send(
                order.getBuyer(),
                Notification.NotificationType.ORDER_STATUS,
                String.format("[%s] 상품 배송이 시작되었습니다.", order.getSaleCard().getCardMaster().getCardName()),
                "/orders/" + order.getId()
        );
    }
}
