package com.sungshincard.backend.domain.payment.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.payment.dto.TossPaymentConfirmRequest;
import com.sungshincard.backend.domain.payment.dto.TossPaymentResponse;
import com.sungshincard.backend.domain.payment.service.TossPaymentService;
import com.sungshincard.backend.domain.order.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class TossPaymentController {

    private final TossPaymentService tossPaymentService;
    private final OrdersService ordersService;

    /**
     * 결제 성공 시 토스에서 전달받은 파라미터로 최종 승인 요청
     */
    @GetMapping("/toss/success")
    public ApiResponse<TossPaymentResponse> successPayment(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount) {
        
        log.info("Toss Payment Success Callback: paymentKey={}, orderId={}, amount={}", paymentKey, orderId, amount);

        // 1. Toss 결제 승인 요청
        TossPaymentConfirmRequest request = TossPaymentConfirmRequest.builder()
                .paymentKey(paymentKey)
                .orderId(orderId)
                .amount(amount)
                .build();
        
        TossPaymentResponse response = tossPaymentService.confirmPayment(request);

        // 2. 주문 상태 업데이트 (PAID)
        // 주문 ID는 "ORDER_123" 형태일 수 있으므로 파싱 필요 여부 확인
        // 여기서는 숫자로 되어있다고 가정하거나 OrdersService에서 처리
        ordersService.completePayment(orderId, paymentKey, response.getMethod());

        return ApiResponse.success(response);
    }

    @GetMapping("/toss/fail")
    public ApiResponse<?> failPayment(
            @RequestParam String code,
            @RequestParam String message,
            @RequestParam String orderId) {
        
        log.error("Toss Payment Failed: code={}, message={}, orderId={}", code, message, orderId);
        return ApiResponse.error(org.springframework.http.HttpStatus.BAD_REQUEST, message);
    }
}
