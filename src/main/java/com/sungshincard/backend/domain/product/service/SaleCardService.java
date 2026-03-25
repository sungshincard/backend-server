package com.sungshincard.backend.domain.product.service;

import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.product.dto.SaleCardRequestDto;
import com.sungshincard.backend.domain.product.dto.SaleCardResponseDto;
import com.sungshincard.backend.domain.product.entity.CardMaster;
import com.sungshincard.backend.domain.product.entity.SaleCard;
import com.sungshincard.backend.domain.product.entity.SaleCardImage;
import com.sungshincard.backend.domain.product.entity.Watchlist;
import com.sungshincard.backend.domain.product.repository.CardMasterRepository;
import com.sungshincard.backend.domain.product.repository.SaleCardRepository;
import com.sungshincard.backend.domain.product.repository.WatchlistRepository;
import com.sungshincard.backend.domain.notification.service.NotificationService;
import com.sungshincard.backend.domain.notification.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SaleCardService {

    private final SaleCardRepository saleCardRepository;
    private final CardMasterRepository cardMasterRepository;
    private final WatchlistRepository watchlistRepository;
    private final NotificationService notificationService;

    @Transactional
    public Long createSaleCard(SaleCardRequestDto requestDto, Member seller) {
        CardMaster cardMaster = cardMasterRepository.findById(requestDto.getCardMasterId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드 마스터입니다."));

        SaleCard saleCard = SaleCard.builder()
                .seller(seller)
                .cardMaster(cardMaster)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .conditionGrade(requestDto.getConditionGrade())
                .price(requestDto.getPrice())
                .build();

        if (requestDto.getImageUrls() != null) {
            for (int i = 0; i < requestDto.getImageUrls().size(); i++) {
                String url = requestDto.getImageUrls().get(i);
                SaleCardImage image = SaleCardImage.builder()
                        .imageUrl(url)
                        .originalFileName("uploaded_file") // 실제 원본 이름은 업로드 단계에서 관리 가능하지만 여기서는 간단히 처리
                        .storedFileName(url.replace("/uploads/", ""))
                        .sortOrder(i)
                        .build();
                image.setSaleCard(saleCard);
            }
        }

        SaleCard saved = saleCardRepository.save(saleCard);

        // 관심 등록한 사용자들에게 알림 발송
        List<Watchlist> watchers = watchlistRepository.findAllByCardMaster(cardMaster);
        log.info("Found {} watchers for cardMasterId: {}", watchers.size(), cardMaster.getId());
        for (Watchlist watchlist : watchers) {
            // 본인이 올린 카드는 알림 제외
            if (!watchlist.getMember().getId().equals(seller.getId())) {
                notificationService.send(
                    watchlist.getMember(),
                    Notification.NotificationType.NEW_LISTING,
                    String.format("[%s] 카드가 새로 출품되었습니다: %s원 (%s 등급)", 
                        cardMaster.getCardName(), saved.getPrice(), saved.getConditionGrade()),
                    "/sale-cards/" + saved.getId()
                );
            }
        }

        return saved.getId();
    }

    public SaleCardResponseDto getSaleCard(Long id) {
        SaleCard saleCard = saleCardRepository.findByIdWithDetails(id);
        if (saleCard == null) {
            throw new IllegalArgumentException("존재하지 않는 출품 카드입니다.");
        }
        return SaleCardResponseDto.from(saleCard);
    }

    public List<SaleCardResponseDto> getSaleCardsByCardMaster(Long cardMasterId, SaleCard.ConditionGrade conditionGrade) {
        List<SaleCard> saleCards;
        if (conditionGrade == null) {
            saleCards = saleCardRepository.findAllByCardMasterIdAndStatusOrderByPriceAsc(cardMasterId, SaleCard.Status.ACTIVE);
        } else {
            saleCards = saleCardRepository.findAllByCardMasterIdAndStatusAndConditionGradeOrderByPriceAsc(cardMasterId, SaleCard.Status.ACTIVE, conditionGrade);
        }
        
        return saleCards.stream()
                .map(SaleCardResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateSaleCard(Long id, String title, String description, Long price, Member member) {
        SaleCard saleCard = saleCardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 출품 카드입니다."));

        if (!saleCard.getSeller().getId().equals(member.getId())) {
            throw new IllegalStateException("본인의 출품 상품만 수정할 수 있습니다.");
        }

        saleCard.update(title, description, price);
    }

    @Transactional
    public void updateStatus(Long id, SaleCard.Status status, Member member) {
        SaleCard saleCard = saleCardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 출품 카드입니다."));

        if (!saleCard.getSeller().getId().equals(member.getId())) {
            throw new IllegalStateException("본인의 출품 상품만 수정할 수 있습니다.");
        }

        // JPA Dirty Checking
        // saleCard.updateStatus(status); // Entity에 메서드 추가 필요
    }

    @Transactional(readOnly = true)
    public List<SaleCardResponseDto> getRecentSaleCards() {
        return saleCardRepository.findTop8ByStatusOrderByCreatedAtDesc(SaleCard.Status.ACTIVE)
                .stream()
                .map(SaleCardResponseDto::from)
                .collect(Collectors.toList());
    }
}
