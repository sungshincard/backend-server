package com.sungshincard.backend.domain.shipment.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.shipment.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping("/{orderId}/prepare")
    public ApiResponse<Void> prepareShipment(@PathVariable Long orderId) {
        shipmentService.prepareShipment(orderId);
        return ApiResponse.success(null, "배송 준비 중으로 변경되었습니다.");
    }

    @PostMapping("/{orderId}/deliver")
    public ApiResponse<Void> deliverShipment(@PathVariable Long orderId) {
        shipmentService.updateToDelivered(orderId);
        return ApiResponse.success(null, "배송 완료 처리되었습니다.");
    }
}
