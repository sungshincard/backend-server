package com.sungshincard.backend.domain.notification.service;

import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.notification.dto.NotificationDto;
import com.sungshincard.backend.domain.notification.entity.Notification;
import com.sungshincard.backend.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationSseService notificationSseService;

    @Transactional
    public void send(Member member, Notification.NotificationType type, String content, String relatedUrl) {
        Notification notification = Notification.builder()
                .member(member)
                .type(type)
                .content(content)
                .relatedUrl(relatedUrl)
                .build();
        Notification saved = notificationRepository.save(notification);
        
        // SSE로 실시간 푸시
        notificationSseService.sendToUser(member.getId(), NotificationDto.from(saved));
    }

    public List<NotificationDto> getMyNotifications(Member member) {
        return notificationRepository.findTop10ByMemberOrderByCreatedAtDesc(member)
                .stream()
                .map(NotificationDto::from)
                .collect(Collectors.toList());
    }

    public long getUnreadCount(Member member) {
        return notificationRepository.countByMemberAndIsReadFalse(member);
    }

    @Transactional
    public void markAsRead(Long id, Member member) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        
        if (!notification.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("Not your notification");
        }
        
        notification.markAsRead();
    }

    @Transactional
    public void markAllAsRead(Member member) {
        List<Notification> unread = notificationRepository.findUnreadByMember(member);
        unread.forEach(Notification::markAsRead);
    }

    @Transactional
    public void delete(Long id, Member member) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        
        if (!notification.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("Not your notification");
        }
        
        notificationRepository.delete(notification);
    }
}
