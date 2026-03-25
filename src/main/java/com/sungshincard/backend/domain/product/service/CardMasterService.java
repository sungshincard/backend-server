package com.sungshincard.backend.domain.product.service;

import com.sungshincard.backend.domain.member.service.MemberService;
import com.sungshincard.backend.domain.product.dto.CardMasterDto;
import com.sungshincard.backend.domain.product.dto.CardMasterRequestDto;
import com.sungshincard.backend.domain.product.dto.CardMasterSearchDto;
import com.sungshincard.backend.domain.product.entity.CardMaster;
import com.sungshincard.backend.domain.product.entity.Pokemon;
import com.sungshincard.backend.domain.product.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardMasterService {

    private final CardMasterRepository cardMasterRepository;
    private final PokemonRepository pokemonRepository;
    private final CardSetRepository cardSetRepository;
    private final CardCategoryRepository cardCategoryRepository;
    private final ElementalTypeRepository elementalTypeRepository;
    private final CardRarityRepository cardRarityRepository;
    private final EvolutionStageRepository evolutionStageRepository;
    private final CardBlockRepository cardBlockRepository;
    private final IllustratorRepository illustratorRepository;
    private final CardExpansionCodeRepository cardExpansionCodeRepository;
    private final CardMasterMapper cardMasterMapper;
    private final WatchlistService watchlistService;
    private final MemberService memberService;
    private final com.sungshincard.backend.domain.order.repository.OrdersRepository ordersRepository;

    @Transactional
    public CardMasterDto createCardMaster(CardMasterRequestDto requestDto) {
        Pokemon pokemon = null;
        if (requestDto.getPokemonId() != null) {
            pokemon = pokemonRepository.findById(requestDto.getPokemonId())
                    .orElseThrow(() -> new IllegalArgumentException("Pokemon not found"));
        }

        CardMaster cardMaster = CardMaster.builder()
                .gameType(CardMaster.GameType.valueOf(requestDto.getGameType()))
                .cardSet(requestDto.getCardSetId() != null ? cardSetRepository.findById(requestDto.getCardSetId()).orElse(null) : null)
                .category(requestDto.getCategoryId() != null ? cardCategoryRepository.findById(requestDto.getCategoryId()).orElse(null) : null)
                .elementalType(requestDto.getElementalTypeId() != null ? elementalTypeRepository.findById(requestDto.getElementalTypeId()).orElse(null) : null)
                .cardRarity(requestDto.getRarityId() != null ? cardRarityRepository.findById(requestDto.getRarityId()).orElse(null) : null)
                .evolutionStage(requestDto.getEvolutionStageId() != null ? evolutionStageRepository.findById(requestDto.getEvolutionStageId()).orElse(null) : null)
                .cardName(requestDto.getCardName())
                .cardNumber(requestDto.getCardNumber())
                .language(requestDto.getLanguage())
                .manufacturer(requestDto.getManufacturer())
                .imageUrl(requestDto.getImageUrl())
                .hp(requestDto.getHp())
                .illustrator(requestDto.getIllustratorId() != null ? illustratorRepository.findById(requestDto.getIllustratorId()).orElse(null) : null)
                .expansionCode(requestDto.getExpansionCodeId() != null ? cardExpansionCodeRepository.findById(requestDto.getExpansionCodeId()).orElse(null) : null)
                .block(requestDto.getBlockId() != null ? cardBlockRepository.findById(requestDto.getBlockId()).orElse(null) : null)
                .description(requestDto.getDescription())
                .pokemon(pokemon)
                .isActive(true)
                .build();

        CardMaster saved = cardMasterRepository.save(cardMaster);
        return CardMasterDto.from(saved, null, null, null, null, null, null, null);
    }

    @Transactional(readOnly = true)
    public CardMasterDto getCardMaster(Long id, String userEmail) {
        CardMasterDto result = cardMasterMapper.getCardMasterDetail(id);
        if (result == null) {
            throw new IllegalArgumentException("CardMaster not found");
        }
        
        result.setFavoriteCount(watchlistService.getWatchCount(id));
        if (userEmail != null) {
            try {
                com.sungshincard.backend.domain.member.entity.Member member = memberService.findByEmail(userEmail);
                result.setIsWatched(watchlistService.isWatched(member, id));
            } catch (Exception e) {
                // Ignore if member not found
            }
        }
        
        java.util.List<com.sungshincard.backend.domain.order.entity.Orders.OrderStatus> tradeStatuses = java.util.Arrays.asList(
                com.sungshincard.backend.domain.order.entity.Orders.OrderStatus.PAID,
                com.sungshincard.backend.domain.order.entity.Orders.OrderStatus.SHIPPED,
                com.sungshincard.backend.domain.order.entity.Orders.OrderStatus.DELIVERED,
                com.sungshincard.backend.domain.order.entity.Orders.OrderStatus.PURCHASE_CONFIRMED
        );
        
        Long recentTradePrice = ordersRepository.findTopBySaleCard_CardMaster_IdAndStatusInOrderByCreatedAtDesc(id, tradeStatuses)
                .map(com.sungshincard.backend.domain.order.entity.Orders::getItemPrice)
                .orElse(null);
        
        result.setRecentTradePrice(recentTradePrice);
        
        return result;
    }

    @Transactional(readOnly = true)
    public Page<CardMasterDto> searchCardMasters(CardMasterSearchDto searchDto) {
        List<CardMasterDto> content = cardMasterMapper.searchCardMasters(searchDto);
        long total = cardMasterMapper.countCardMasters(searchDto);
        return new PageImpl<>(content, PageRequest.of(searchDto.getPage(), searchDto.getSize()), total);
    }

    @Transactional(readOnly = true)
    public List<CardMasterDto> getRecentCardMasters() {
        return cardMasterMapper.findRecentCardMasters(8);
    }
}
