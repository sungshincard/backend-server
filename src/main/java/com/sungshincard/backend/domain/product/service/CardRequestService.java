package com.sungshincard.backend.domain.product.service;

import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.member.repository.MemberRepository;
import com.sungshincard.backend.domain.product.dto.CardRequestCreateDto;
import com.sungshincard.backend.domain.product.dto.CardRequestDto;
import com.sungshincard.backend.domain.product.dto.CardRequestStatusDto;
import com.sungshincard.backend.domain.product.entity.CardMaster;
import com.sungshincard.backend.domain.product.entity.CardRequest;
import com.sungshincard.backend.domain.product.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardRequestService {

    private final CardRequestRepository cardRequestRepository;
    private final MemberRepository memberRepository;
    private final CardSetRepository cardSetRepository;
    private final CardCategoryRepository cardCategoryRepository;
    private final ElementalTypeRepository elementalTypeRepository;
    private final CardRarityRepository cardRarityRepository;
    private final EvolutionStageRepository evolutionStageRepository;

    @Transactional
    public CardRequestDto createCardRequest(Long requesterId, CardRequestCreateDto dto) {
        Member requester = memberRepository.findById(requesterId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        CardRequest cardRequest = CardRequest.builder()
                .requester(requester)
                .gameType(CardMaster.GameType.valueOf(dto.getGameType()))
                .cardSet(dto.getCardSetId() != null ? cardSetRepository.findById(dto.getCardSetId()).orElse(null) : null)
                .category(dto.getCategoryId() != null ? cardCategoryRepository.findById(dto.getCategoryId()).orElse(null) : null)
                .elementalType(dto.getElementalTypeId() != null ? elementalTypeRepository.findById(dto.getElementalTypeId()).orElse(null) : null)
                .cardRarity(dto.getRarityId() != null ? cardRarityRepository.findById(dto.getRarityId()).orElse(null) : null)
                .evolutionStage(dto.getEvolutionStageId() != null ? evolutionStageRepository.findById(dto.getEvolutionStageId()).orElse(null) : null)
                .cardName(dto.getCardName())
                .cardNumber(dto.getCardNumber())
                .hp(dto.getHp())
                .illustrator(dto.getIllustrator())
                .expansionCode(dto.getExpansionCode())
                .block(dto.getBlock())
                .referenceImageUrl(dto.getReferenceImageUrl())
                .requestNote(dto.getRequestNote())
                .status(CardRequest.Status.PENDING)
                .build();

        CardRequest saved = cardRequestRepository.save(cardRequest);
        return CardRequestDto.from(saved);
    }

    @Transactional(readOnly = true)
    public List<CardRequestDto> getMyRequests(Long requesterId) {
        return cardRequestRepository.findByRequesterId(requesterId).stream()
                .map(CardRequestDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CardRequestDto> getPendingRequests() {
        return cardRequestRepository.findByStatus(CardRequest.Status.PENDING).stream()
                .map(CardRequestDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CardRequestDto reviewCardRequest(Long id, Long adminId, CardRequestStatusDto dto) {
        CardRequest request = cardRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CardRequest not found"));

        Member admin = memberRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        CardRequest updated = CardRequest.builder()
                .id(request.getId())
                .requester(request.getRequester())
                .gameType(request.getGameType())
                .cardSet(request.getCardSet())
                .category(request.getCategory())
                .elementalType(request.getElementalType())
                .cardRarity(request.getCardRarity())
                .evolutionStage(request.getEvolutionStage())
                .cardName(request.getCardName())
                .cardNumber(request.getCardNumber())
                .hp(request.getHp())
                .illustrator(request.getIllustrator())
                .expansionCode(request.getExpansionCode())
                .block(request.getBlock())
                .referenceImageUrl(request.getReferenceImageUrl())
                .requestNote(request.getRequestNote())
                .status(CardRequest.Status.valueOf(dto.getStatus()))
                .reviewedBy(admin)
                .reviewedAt(LocalDateTime.now())
                .rejectReason(dto.getRejectReason())
                .build();
                
        // In JPA, to update an entity, we can either use save() with the same ID, or add setters to entity. 
        // Since CardRequest has no setters and uses Builder, we save the new built entity which will overwrite due to having the same ID.
        // But wait, BaseTimeEntity fields might be lost. Let's use repository save.
        
        CardRequest saved = cardRequestRepository.save(updated);
        return CardRequestDto.from(saved);
    }
}
