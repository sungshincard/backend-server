package com.sungshincard.backend.domain.order.scheduler;

import com.sungshincard.backend.domain.order.entity.Orders;
import com.sungshincard.backend.domain.order.repository.OrdersRepository;
import com.sungshincard.backend.domain.product.entity.SaleCard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrdersRepository ordersRepository;

    /**
     * 30분 동안 결제가 완료되지 않은 PENDING 주문을 자동 취소 처리
     * 매 10분마다 실행
     */
    @Scheduled(fixedDelay = 600000) // 10분
    @Transactional
    public void cancelExpiredOrders() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(30);
        
        List<Orders> expiredOrders = ordersRepository.findAllByStatusAndCreatedAtBefore(
                Orders.OrderStatus.PENDING, expirationTime);

        if (expiredOrders.isEmpty()) {
            return;
        }

        log.info("Cancelling {} expired PENDING orders", expiredOrders.size());

        for (Orders order : expiredOrders) {
            order.updateStatus(Orders.OrderStatus.CANCELLED);
            // 연관된 SaleCard를 다시 ACTIVE로 변경
            order.getSaleCard().updateStatus(SaleCard.Status.ACTIVE);
        }
    }
}
