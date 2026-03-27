package com.sungshincard.backend.domain.shipment.service;

import com.sungshincard.backend.domain.member.entity.Address;
import com.sungshincard.backend.domain.member.repository.AddressRepository;
import com.sungshincard.backend.domain.order.entity.Orders;
import com.sungshincard.backend.domain.order.repository.OrdersRepository;
import com.sungshincard.backend.domain.shipment.entity.Shipment;
import com.sungshincard.backend.domain.shipment.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final OrdersRepository ordersRepository;
    private final AddressRepository addressRepository;

    @Transactional
    public void prepareShipment(Long orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (order.getStatus() != Orders.OrderStatus.PAYMENT_COMPLETED) {
            throw new IllegalStateException("결제 완료 상태에서만 배송 준비를 시작할 수 있습니다.");
        }

        // 1. 주문 상태 변경
        order.updateStatus(Orders.OrderStatus.PREPARING);

        // 2. 배송 정보(Shipment) 생성
        // 기존에 Shipment가 있는지 확인 (재진입 시 방지)
        if (shipmentRepository.findByOrder(order).isPresent()) {
            return;
        }

        // 수취인 주소 찾기 혹은 생성 (중복 저장 방지)
        Address receiverAddress = addressRepository.findByMemberId(order.getBuyer().getId()).stream()
                .filter(a -> a.getZipCode().equals(order.getZipCode()) &&
                        a.getAddress1().equals(order.getAddress()) &&
                        a.getAddress2().equals(order.getDetailAddress()) &&
                        a.getRecipientName().equals(order.getReceiverName()) &&
                        a.getRecipientPhone().equals(order.getReceiverPhone()))
                .findFirst()
                .orElseGet(() -> {
                    Address newAddr = Address.builder()
                            .member(order.getBuyer())
                            .recipientName(order.getReceiverName())
                            .recipientPhone(order.getReceiverPhone())
                            .zipCode(order.getZipCode())
                            .address1(order.getAddress())
                            .address2(order.getDetailAddress())
                            .isDefault(false)
                            .build();
                    return addressRepository.save(newAddr);
                });

        Shipment shipment = Shipment.builder()
                .order(order)
                .receiverAddress(receiverAddress)
                .status(Shipment.Status.READY)
                .build();
        shipmentRepository.save(shipment);

        log.info("Shipment prepared for order: {}", orderId);
    }

    @Transactional
    public void updateToShipping(Long orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (order.getStatus() != Orders.OrderStatus.PREPARING && order.getStatus() != Orders.OrderStatus.PAYMENT_COMPLETED) {
            throw new IllegalStateException("배송 준비 중 또는 결제 완료 상태에서만 배송 중 처리가 가능합니다.");
        }

        // 1. 주문 상태 변경
        order.updateStatus(Orders.OrderStatus.SHIPPING);

        // 2. 배송 상태 변경
        Shipment shipment = shipmentRepository.findByOrder(order)
                .orElseThrow(() -> new IllegalStateException("배송 준비 중인 정보가 없습니다."));

        shipment.updateStatus(Shipment.Status.SHIPPING);
        log.info("Shipment status updated to SHIPPING for order: {}", orderId);
    }

    @Transactional
    public void updateToDelivered(Long orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (order.getStatus() != Orders.OrderStatus.SHIPPING) {
            throw new IllegalStateException("배송 중인 주문만 배송 완료 처리가 가능합니다.");
        }

        // 1. 주문 상태 변경
        order.updateStatus(Orders.OrderStatus.DELIVERED);

        // 2. 배송 상태 변경
        Shipment shipment = shipmentRepository.findByOrder(order)
                .orElseThrow(() -> new IllegalStateException("배송 정보가 없습니다."));

        shipment.updateStatus(Shipment.Status.DELIVERED);
        log.info("Shipment status updated to DELIVERED for order: {}", orderId);
    }
}
