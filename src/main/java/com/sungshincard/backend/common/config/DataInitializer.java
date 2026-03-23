package com.sungshincard.backend.common.config;

import com.sungshincard.backend.domain.product.entity.CardCategory;
import com.sungshincard.backend.domain.product.entity.CardMaster;
import com.sungshincard.backend.domain.product.entity.ElementalType;
import com.sungshincard.backend.domain.product.repository.CardCategoryRepository;
import com.sungshincard.backend.domain.product.repository.ElementalTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ElementalTypeRepository elementalTypeRepository;
    private final CardCategoryRepository cardCategoryRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (elementalTypeRepository.count() == 0) {
            initElementalTypes();
        }
        if (cardCategoryRepository.count() == 0) {
            initCategories();
        }
    }

    private void initElementalTypes() {
        String[][] types = {
            {"FIRE", "불꽃"}, {"WATER", "물"}, {"GRASS", "풀"}, {"LIGHTNING", "번개"},
            {"PSYCHIC", "초"}, {"FIGHTING", "격투"}, {"DARKNESS", "악"}, {"METAL", "강철"},
            {"DRAGON", "드래곤"}, {"COLORLESS", "무색"}, {"FAIRY", "요정"}
        };

        for (String[] t : types) {
            elementalTypeRepository.save(ElementalType.builder()
                .name(t[0]).displayName(t[1])
                .gameType(CardMaster.GameType.POKEMON).build());
        }
    }

    private void initCategories() {
        // Pokemon
        cardCategoryRepository.save(CardCategory.builder()
            .name("POKEMON").displayName("포켓몬").gameType(CardMaster.GameType.POKEMON).sortOrder(1).build());

        // Energy
        CardCategory energy = cardCategoryRepository.save(CardCategory.builder()
            .name("ENERGY").displayName("에너지").gameType(CardMaster.GameType.POKEMON).sortOrder(2).build());
        cardCategoryRepository.save(CardCategory.builder()
            .name("BASIC_ENERGY").displayName("기본 에너지").parent(energy).gameType(CardMaster.GameType.POKEMON).sortOrder(1).build());
        cardCategoryRepository.save(CardCategory.builder()
            .name("SPECIAL_ENERGY").displayName("특수 에너지").parent(energy).gameType(CardMaster.GameType.POKEMON).sortOrder(2).build());

        // Trainer
        CardCategory trainer = cardCategoryRepository.save(CardCategory.builder()
            .name("TRAINER").displayName("트레이너스").gameType(CardMaster.GameType.POKEMON).sortOrder(3).build());
        cardCategoryRepository.save(CardCategory.builder()
            .name("ITEM").displayName("아이템").parent(trainer).gameType(CardMaster.GameType.POKEMON).sortOrder(1).build());
        cardCategoryRepository.save(CardCategory.builder()
            .name("SUPPORT").displayName("서포트").parent(trainer).gameType(CardMaster.GameType.POKEMON).sortOrder(2).build());
        cardCategoryRepository.save(CardCategory.builder()
            .name("STADIUM").displayName("스타디움").parent(trainer).gameType(CardMaster.GameType.POKEMON).sortOrder(3).build());
        cardCategoryRepository.save(CardCategory.builder()
            .name("TOOL").displayName("포켓몬 도구").parent(trainer).gameType(CardMaster.GameType.POKEMON).sortOrder(4).build());
    }
}
