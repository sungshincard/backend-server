package com.sungshincard.backend.domain.settlement.service;

import com.sungshincard.backend.domain.order.entity.Orders;
import com.sungshincard.backend.domain.settlement.entity.Settlement;
import com.sungshincard.backend.domain.settlement.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sungshincard.backend.domain.settlement.dto.SettlementResponseDto;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementService {

    private final SettlementRepository settlementRepository;

    public List<SettlementResponseDto> getMySettlements(Long memberId) {
        return settlementRepository.findBySellerIdOrderByCreatedAtDesc(memberId)
                .stream()
                .map(SettlementResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createSettlement(Orders order) {
        // 이미 해당 주문에 대한 정산 데이터가 있는지 확인 (중복 방지)
        if (settlementRepository.existsByOrder(order)) {
            return;
        }

        // 정산 금액 계산: 상품 가액(itemPrice)에서 플랫폼 수수료(예: 3%)를 제외한 실제 지급 금액
        long itemPrice = order.getItemPrice();
        long sellerFee = (long) (itemPrice * 0.03); // 3% 플랫폼 수수료
        long netAmount = itemPrice - sellerFee;

        Settlement settlement = Settlement.builder()
                .order(order)
                .seller(order.getSeller())
                .grossAmount(itemPrice)
                .feeAmount(sellerFee)
                .netAmount(netAmount)
                .status(Settlement.Status.PENDING)
                .build();
        
        settlementRepository.save(settlement);
    }
}
