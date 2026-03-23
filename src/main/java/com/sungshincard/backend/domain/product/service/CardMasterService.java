package com.sungshincard.backend.domain.product.service;

import com.sungshincard.backend.domain.product.dto.CardMasterDto;
import com.sungshincard.backend.domain.product.dto.CardMasterRequestDto;
import com.sungshincard.backend.domain.product.dto.CardMasterSearchDto;
import com.sungshincard.backend.domain.product.entity.CardMaster;
import com.sungshincard.backend.domain.product.entity.Pokemon;
import com.sungshincard.backend.domain.product.repository.CardCategoryRepository;
import com.sungshincard.backend.domain.product.repository.CardMasterMapper;
import com.sungshincard.backend.domain.product.repository.CardMasterRepository;
import com.sungshincard.backend.domain.product.repository.CardSetRepository;
import com.sungshincard.backend.domain.product.repository.ElementalTypeRepository;
import com.sungshincard.backend.domain.product.repository.PokemonRepository;
import lombok.RequiredArgsConstructor;
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
    private final CardMasterMapper cardMasterMapper;

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
                .cardName(requestDto.getCardName())
                .cardNumber(requestDto.getCardNumber())
                .rarity(requestDto.getRarity())
                .language(requestDto.getLanguage())
                .manufacturer(requestDto.getManufacturer())
                .imageUrl(requestDto.getImageUrl())
                .hp(requestDto.getHp())
                .evolutionStage(requestDto.getEvolutionStage())
                .illustrator(requestDto.getIllustrator())
                .expansionCode(requestDto.getExpansionCode())
                .block(requestDto.getBlock())
                .description(requestDto.getDescription())
                .pokemon(pokemon)
                .isActive(true)
                .build();

        CardMaster saved = cardMasterRepository.save(cardMaster);
        return CardMasterDto.from(saved);
    }

    @Transactional(readOnly = true)
    public CardMasterDto getCardMaster(Long id) {
        CardMaster cardMaster = cardMasterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CardMaster not found"));
        return CardMasterDto.from(cardMaster);
    }

    @Transactional(readOnly = true)
    public List<CardMasterDto> searchCardMasters(CardMasterSearchDto searchDto) {
        return cardMasterMapper.searchCardMasters(searchDto);
    }
}
