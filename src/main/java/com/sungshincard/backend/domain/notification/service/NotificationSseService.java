package com.sungshincard.backend.domain.notification.service;

import com.sungshincard.backend.domain.notification.dto.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class NotificationSseService {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long memberId) {
        // 1시간 타임아웃
        SseEmitter emitter = new SseEmitter(60L * 1000 * 60);
        emitters.put(memberId, emitter);

        emitter.onCompletion(() -> {
            log.info("SSE connection completed for member: {}", memberId);
            emitters.remove(memberId);
        });
        emitter.onTimeout(() -> {
            log.info("SSE connection timeout for member: {}", memberId);
            emitters.remove(memberId);
        });
        emitter.onError((e) -> {
            log.error("SSE connection error for member: {}", memberId);
            emitters.remove(memberId);
        });

        // 초기 연결 시 더미 데이터 전송 (503 에러 방지 및 연결 확인)
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected"));
        } catch (IOException e) {
            emitters.remove(memberId);
        }

        return emitter;
    }

    public void sendToUser(Long memberId, NotificationDto notificationDto) {
        SseEmitter emitter = emitters.get(memberId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(notificationDto));
                log.info("Successfully sent SSE notification to member: {}", memberId);
            } catch (IOException e) {
                log.error("Failed to send SSE to member: {}. Error: {}", memberId, e.getMessage());
                emitters.remove(memberId);
            }
        } else {
            log.warn("No active SSE emitter found for member: {}. Notification saved to DB but not pushed.", memberId);
        }
    }
}
