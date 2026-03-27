package com.sungshincard.backend.domain.settlement.service;

import com.sungshincard.backend.domain.order.entity.Orders;
import com.sungshincard.backend.domain.settlement.entity.Settlement;
import com.sungshincard.backend.domain.settlement.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementService {

    private final SettlementRepository settlementRepository;

    @Transactional
    public void createSettlement(Orders order) {
        // 이미 해당 주문에 대한 정산 데이터가 있는지 확인 (중복 방지)
        if (settlementRepository.existsByOrder(order)) {
            return;
        }

        Settlement settlement = Settlement.builder()
                .order(order)
                .seller(order.getSeller())
                .grossAmount(order.getTotalPrice())
                .feeAmount(order.getServiceFee())
                .netAmount(order.getSettlementAmount())
                .status(Settlement.Status.PENDING)
                .build();
        
        settlementRepository.save(settlement);
    }
}
