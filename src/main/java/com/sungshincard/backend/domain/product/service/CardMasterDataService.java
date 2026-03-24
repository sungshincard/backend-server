package com.sungshincard.backend.domain.product.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sungshincard.backend.domain.product.entity.*;
import com.sungshincard.backend.domain.product.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardMasterDataService {

  private final CardMasterRepository cardMasterRepository;
  private final CardSetRepository cardSetRepository;
  private final ElementalTypeRepository elementalTypeRepository;
  private final CardCategoryRepository cardCategoryRepository;
  private final CardRarityRepository cardRarityRepository;
  private final EvolutionStageRepository evolutionStageRepository;
  private final RestTemplate restTemplate = new RestTemplate();
  private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

  private static final String TCG_DATA_URL = "https://raw.githubusercontent.com/kinbo-ptcg/ptcg-kr-db/main/src/ptcg_kr_re_classify/all_card_data.json";

  @Transactional
  public void initializeCardData() {
    if (cardMasterRepository.count() > 0) {
      log.info("Card data already initialized. Skipping...");
      return;
    }

    log.info("Starting Korean Pokemon Card data initialization from ptcg-kr-db...");

    try {
      String jsonContent = restTemplate.getForObject(TCG_DATA_URL, String.class);
      if (jsonContent == null) {
        log.error("Failed to fetch Card list from community DB (Empty response)");
        return;
      }
      
      JsonNode rootNode = objectMapper.readTree(jsonContent);
      if (rootNode == null || !rootNode.isArray()) {
        log.error("Failed to parse Card list (Invalid JSON structure)");
        return;
      }

      List<CardMaster> cardList = new ArrayList<>();
      int limit = 500; // Adjust as needed
      int count = 0;

      for (JsonNode pokemonNode : rootNode) {
        if (count >= limit) break;

        String pokemonName = pokemonNode.has("name") ? pokemonNode.get("name").asText() : "Unknown";
        JsonNode versions = pokemonNode.get("version_infos");
        
        if (versions == null || !versions.isArray()) continue;

        CardCategory category = determineCategory(pokemonNode);
        ElementalType elementalType = determineElementalType(pokemonNode);
        EvolutionStage stage = getOrCreateEvolutionStage(pokemonNode, CardMaster.GameType.POKEMON);

        for (JsonNode versionNode : versions) {
          String cardName = pokemonName + " " + (versionNode.has("card_name_suffix") ? versionNode.get("card_name_suffix").asText() : "");
          String cardNumber = versionNode.has("number") ? versionNode.get("number").asText() : "N/A";
          
          CardSet cardSet = getOrCreateCardSet(versionNode, CardMaster.GameType.POKEMON);
          CardRarity rarity = getOrCreateRarity(versionNode, CardMaster.GameType.POKEMON);

          String imageUrl = versionNode.has("image_url") ? versionNode.get("image_url").asText() : null;
          String description = pokemonNode.has("flavorText") ? pokemonNode.get("flavorText").asText() : null;

          CardMaster card = CardMaster.builder()
              .gameType(CardMaster.GameType.POKEMON)
              .category(category)
              .elementalType(elementalType)
              .cardSet(cardSet)
              .cardRarity(rarity)
              .evolutionStage(stage)
              .cardName(cardName.trim())
              .cardNumber(cardNumber)
              .language("ko")
              .manufacturer("Pokémon Korea")
              .imageUrl(imageUrl)
              .hp(pokemonNode.has("hp") ? pokemonNode.get("hp").asInt() : null)
              .description(description)
              .isActive(true)
              .build();

          cardList.add(card);
          count++;
        }
      }

      if (!cardList.isEmpty()) {
        cardMasterRepository.saveAll(cardList);
      }

      log.info("Korean Card data initialization completed! Saved {} cards.", cardList.size());

    } catch (Exception e) {
      log.error("Error during Card data initialization", e);
    }
  }

  private CardSet getOrCreateCardSet(JsonNode versionNode, CardMaster.GameType gameType) {
    String setName = versionNode.has("set_name") ? versionNode.get("set_name").asText() : "Unknown Set";
    return cardSetRepository.findByName(setName).orElseGet(() -> {
      CardSet newSet = CardSet.builder()
          .name(setName)
          .gameType(gameType)
          .isActive(true)
          .build();
      return cardSetRepository.save(newSet);
    });
  }

  private CardRarity getOrCreateRarity(JsonNode versionNode, CardMaster.GameType gameType) {
    String rarityName = versionNode.has("rarity") ? versionNode.get("rarity").asText() : "N/A";
    if (rarityName.strip().isEmpty()) rarityName = "N/A";
    
    String finalRarityName = rarityName;
    return cardRarityRepository.findByNameAndGameType(finalRarityName, gameType).orElseGet(() -> {
      CardRarity newRarity = CardRarity.builder()
          .name(finalRarityName)
          .displayName(finalRarityName)
          .gameType(gameType)
          .isActive(true)
          .build();
      return cardRarityRepository.save(newRarity);
    });
  }

  private EvolutionStage getOrCreateEvolutionStage(JsonNode pokemonNode, CardMaster.GameType gameType) {
    String stageName = "기본"; // Default
    if (pokemonNode.has("subtypes") && pokemonNode.get("subtypes").isArray() && pokemonNode.get("subtypes").size() > 0) {
      stageName = pokemonNode.get("subtypes").get(0).asText();
    }

    String finalStageName = stageName;
    return evolutionStageRepository.findByNameAndGameType(finalStageName, gameType).orElseGet(() -> {
      EvolutionStage newStage = EvolutionStage.builder()
          .name(finalStageName)
          .gameType(gameType)
          .sortOrder(0)
          .build();
      return evolutionStageRepository.save(newStage);
    });
  }

  private CardCategory determineCategory(JsonNode pokemonNode) {
    String supertype = pokemonNode.has("supertype") ? pokemonNode.get("supertype").asText() : "포켓몬";
    String categoryName = "POKEMON"; // Default

    if ("에너지".equals(supertype)) categoryName = "ENERGY";
    else if ("트레이너".equals(supertype)) categoryName = "TRAINER";

    String finalCategoryName = categoryName;
    return cardCategoryRepository.findByNameAndGameType(finalCategoryName, CardMaster.GameType.POKEMON)
        .orElseGet(() -> cardCategoryRepository.findByNameAndGameType("POKEMON", CardMaster.GameType.POKEMON).orElse(null));
  }

  private ElementalType determineElementalType(JsonNode pokemonNode) {
    if (!pokemonNode.has("type") || !pokemonNode.get("type").isArray() || pokemonNode.get("type").size() == 0) {
      return null;
    }
    String typeName = pokemonNode.get("type").get(0).asText(); // e.g., "풀", "불꽃"

    return elementalTypeRepository.findByNameAndGameType(typeName, CardMaster.GameType.POKEMON)
        .orElseGet(() -> {
          // If not found, create a basic one for consistency
          ElementalType newType = ElementalType.builder()
              .name(typeName)
              .displayName(typeName)
              .gameType(CardMaster.GameType.POKEMON)
              .build();
          return elementalTypeRepository.save(newType);
        });
  }
}