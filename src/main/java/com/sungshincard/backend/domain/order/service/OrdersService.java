package com.sungshincard.backend.domain.order.service;

import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.order.dto.OrderRequestDto;
import com.sungshincard.backend.domain.order.entity.Orders;
import com.sungshincard.backend.domain.order.repository.OrdersRepository;
import com.sungshincard.backend.domain.product.entity.SaleCard;
import com.sungshincard.backend.domain.product.repository.SaleCardRepository;
import com.sungshincard.backend.domain.notification.entity.Notification;
import com.sungshincard.backend.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final SaleCardRepository saleCardRepository;
    private final NotificationService notificationService;

    @Transactional
    public Long createOrder(OrderRequestDto requestDto, Member buyer) {
        SaleCard saleCard = saleCardRepository.findById(requestDto.getSaleCardId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 출품 카드입니다."));

        if (saleCard.getStatus() != SaleCard.Status.ACTIVE) {
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
                .status(Orders.OrderStatus.PAID) // 현재는 결제 완료 상태를 바로 생성 (Mock)
                .paidAt(java.time.LocalDateTime.now())
                .build();

        Orders savedOrder = ordersRepository.save(order);

        // 출품 상태를 RESERVED로 변경
        saleCard.updateStatus(SaleCard.Status.RESERVED);

        // 판매자에게 알림 발송
        notificationService.send(
                saleCard.getSeller(),
                Notification.NotificationType.ORDER_STATUS,
                String.format("[%s] 상품이 판매되었습니다. 결제 금액을 확인해 주세요.", saleCard.getCardMaster().getCardName()),
                "/orders/" + savedOrder.getId()
        );

        return savedOrder.getId();
    }

    public Orders getOrder(Long id) {
        return ordersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }
}
