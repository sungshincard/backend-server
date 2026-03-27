package com.sungshincard.backend.domain.order.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.order.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

/**
 * [테스트 전용 컨트롤러]
 * dev 프로파일에서만 활성화되며, 실제 배송 없이 주문 상태를 변경할 수 있습니다.
 * 운영 환경(prod)에서는 이 컨트롤러의 빈이 생성되지 않습니다.
 */
@RestController
@RequestMapping("/api/v1/test/orders")
@RequiredArgsConstructor
@Profile("dev")
public class OrderTestController {

  private final OrdersService ordersService;

  /**
   * 주문 상태를 PAYMENT_COMPLETED → SHIPPING 으로 강제 변경합니다.
   * 실제 송장 등록 없이 배송 중 상태로 전환하여 테스트합니다.
   */
  @PostMapping("/{orderId}/shipping")
  public ApiResponse<Void> forceShipping(@PathVariable Long orderId) {
    ordersService.forceShippingForTest(orderId);
    return ApiResponse.success(null, "테스트: 주문이 배송 중 상태로 변경되었습니다.");
  }

  /**
   * 주문 상태를 SHIPPING → DELIVERED 로 강제 변경합니다.
   * 택배 배송은 실제로 확인할 수 없으므로, 구매 확정/정산 프로세스를 테스트하기 위해 사용합니다.
   */
  @PostMapping("/{orderId}/delivered")
  public ApiResponse<Void> forceDeliver(@PathVariable Long orderId) {
    ordersService.forceDeliverForTest(orderId);
    return ApiResponse.success(null, "테스트: 주문이 배송 완료 상태로 변경되었습니다.");
  }

  /**
   * 주문 상태를 DELIVERED → PURCHASE_CONFIRMED 로 강제 변경합니다.
   * 실제 구매 확정 버튼 클릭 없이 정산 대기 상태로 전환하여 테스트합니다.
   */
  @PostMapping("/{orderId}/confirm")
  public ApiResponse<Void> forceConfirm(@PathVariable Long orderId) {
    ordersService.forceConfirmForTest(orderId);
    return ApiResponse.success(null, "테스트: 주문이 구매 확정 상태로 변경되었습니다.");
  }
}
