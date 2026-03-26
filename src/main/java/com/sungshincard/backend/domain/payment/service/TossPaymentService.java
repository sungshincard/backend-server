package com.sungshincard.backend.domain.payment.service;

import com.sungshincard.backend.domain.payment.config.TossPaymentConfig;
import com.sungshincard.backend.domain.payment.dto.TossPaymentConfirmRequest;
import com.sungshincard.backend.domain.payment.dto.TossPaymentResponse;
import com.sungshincard.backend.domain.payment.dto.TossShippingInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossPaymentService {

    private final TossPaymentConfig tossPaymentConfig;
    private final RestTemplate restTemplate;

    private static final String CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";
    private static final String ESCROW_SHIPPING_URL = "https://api.tosspayments.com/v1/escrow/shipping-info/";

    /**
     * 토스 결제 승인 요청
     */
    public TossPaymentResponse confirmPayment(TossPaymentConfirmRequest request) {
        HttpHeaders headers = createHeaders();
        HttpEntity<TossPaymentConfirmRequest> entity = new HttpEntity<>(request, headers);

        try {
            return restTemplate.postForObject(CONFIRM_URL, entity, TossPaymentResponse.class);
        } catch (Exception e) {
            log.error("Toss Payment Confirmation Failed: {}", e.getMessage());
            throw new RuntimeException("결제 승인 과정에서 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 토스 결제 취소 요청 (보상 트랜잭션용)
     */
    public void cancelPayment(String paymentKey, String cancelReason) {
        HttpHeaders headers = createHeaders();
        java.util.Map<String, String> body = new java.util.HashMap<>();
        body.put("cancelReason", cancelReason);
        
        HttpEntity<java.util.Map<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForObject(
                    "https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel",
                    entity,
                    String.class
            );
            log.info("Toss Payment Cancelled for paymentKey: {}, Reason: {}", paymentKey, cancelReason);
        } catch (Exception e) {
            log.error("Toss Payment Cancellation Failed: {}", e.getMessage());
            // 취소 실패 시엔 수동 처리를 위한 로그를 비중 있게 남김
            log.error("CRITICAL: Payment cancellation failed for key {}. Manual intervention required!", paymentKey);
        }
    }

    /**
     * 토스 에스크로 배송 정보 등록
     */
    public void registerShippingInfo(String paymentKey, TossShippingInfoRequest request) {
        HttpHeaders headers = createHeaders();
        HttpEntity<TossShippingInfoRequest> entity = new HttpEntity<>(request, headers);

        try {
            restTemplate.postForObject(ESCROW_SHIPPING_URL + paymentKey, entity, String.class);
            log.info("Toss Escrow Shipping Info Registered for paymentKey: {}", paymentKey);
        } catch (Exception e) {
            log.error("Toss Escrow Shipping Registration Failed: {}", e.getMessage());
            throw new RuntimeException("에스크로 배송 정보 등록 과정에서 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String secretKey = tossPaymentConfig.getSecretKey();
        String authBase64 = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        
        headers.setBasicAuth(authBase64); // 이미 base64 인코딩 되어 있으나 setBasicAuth는 raw string을 기대할 수 있으므로 주의
        // Spring HttpHeaders.setBasicAuth(String encodedAuth)는 인코딩된 문자열을 받기도 함.
        // 하지만 더 확실하게:
        headers.set("Authorization", "Basic " + authBase64);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
