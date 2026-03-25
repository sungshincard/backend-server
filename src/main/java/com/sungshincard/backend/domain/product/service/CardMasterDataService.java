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
  private final PokemonRepository pokemonRepository;
  private final CardBlockRepository cardBlockRepository;
  private final IllustratorRepository illustratorRepository;
  private final CardExpansionCodeRepository cardExpansionCodeRepository;
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

      log.info("가져온 데이터 확인: {}",
          jsonContent != null ? jsonContent.substring(0, Math.min(jsonContent.length(), 200)) : "데이터 없음!");

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
      java.util.Set<String> processedKeys = new java.util.HashSet<>();
      // int limit = 2000;
      // int count = 0;

      for (JsonNode cardNode : rootNode) {
        // if (count >= limit)
        // break;

        String pokemonName = cardNode.has("name") ? cardNode.get("name").asText() : "Unknown";

        CardCategory category = determineCategory(cardNode);
        ElementalType elementalType = determineElementalType(cardNode);
        EvolutionStage stage = getOrCreateEvolutionStage(cardNode, CardMaster.GameType.POKEMON);
        Pokemon pokemon = getOrCreatePokemon(cardNode);

        // In flat structure, the node itself is the version
        String cardName = pokemonName;
        String cardNumber = cardNode.has("id") ? cardNode.get("id").asText() : "N/A";

        CardSet cardSet = getOrCreateCardSet(cardNode, CardMaster.GameType.POKEMON);
        CardRarity rarity = getOrCreateRarity(cardNode, CardMaster.GameType.POKEMON);
        CardBlock block = getOrCreateCardBlock(cardNode, CardMaster.GameType.POKEMON);
        CardExpansionCode expansionCode = getOrCreateExpansionCode(cardNode, cardSet, CardMaster.GameType.POKEMON);
        Illustrator illustrator = getOrCreateIllustrator(cardNode);

        // 🚀 수정된 코드: cardImgURL을 가장 먼저 찾도록 변경
        String imageUrl = cardNode.has("cardImgURL") ? cardNode.get("cardImgURL").asText() : null;

        // (혹시 다른 형식의 데이터가 섞여 있을 경우를 대비한 안전빵 코드)
        if (imageUrl == null && cardNode.has("image_url")) {
          imageUrl = cardNode.get("image_url").asText();
        } else if (imageUrl == null && cardNode.has("imageUrl")) {
          imageUrl = cardNode.get("imageUrl").asText();
        }

        String description = cardNode.has("flavorText") ? cardNode.get("flavorText").asText() : null;

        String compositeKey = "POKEMON-" + cardSet.getId() + "-" + cardNumber + "-ko";
        if (processedKeys.contains(compositeKey)) {
          log.warn("Skipping duplicate card: {}", compositeKey);
          continue;
        }
        processedKeys.add(compositeKey);

        CardMaster card = CardMaster.builder()
            .gameType(CardMaster.GameType.POKEMON)
            .category(category)
            .elementalType(elementalType)
            .cardSet(cardSet)
            .cardRarity(rarity)
            .evolutionStage(stage)
            .pokemon(pokemon)
            .cardName(cardName.trim())
            .cardNumber(cardNumber)
            .language("ko")
            .manufacturer("Pokémon Korea")
            .imageUrl(imageUrl)
            .hp(cardNode.has("hp") ? cardNode.get("hp").asInt() : null)
            .block(block)
            .expansionCode(expansionCode)
            .illustrator(illustrator)
            .description(description)
            .isActive(true)
            .build();

        cardList.add(card);
        // count++;
      }

      if (!cardList.isEmpty()) {
        cardMasterRepository.saveAll(cardList);
      }

      log.info("Korean Card data initialization completed! Saved {} cards.", cardList.size());

    } catch (Exception e) {
      log.error("Error during Card data initialization", e);
    }
  }

  private CardSet getOrCreateCardSet(JsonNode cardNode, CardMaster.GameType gameType) {
    // 🚀 수정: JSON 데이터의 "prodName" 키를 찾도록 변경
    String setName = cardNode.has("prodName") ? cardNode.get("prodName").asText()
        : (cardNode.has("set_name") ? cardNode.get("set_name").asText() : "Unknown Set");

    // 🚀 수정: 확장팩 코드도 "prodCode" 키를 찾도록 변경
    String setCode = cardNode.has("prodCode") ? cardNode.get("prodCode").asText()
        : (cardNode.has("expansion_code") ? cardNode.get("expansion_code").asText() : null);

    String series = cardNode.has("regulationMark") ? cardNode.get("regulationMark").asText() : null;

    return cardSetRepository.findByName(setName).orElseGet(() -> {
      CardSet newSet = CardSet.builder()
          .name(setName)
          .expansionCode(setCode)
          .series(series)
          .gameType(gameType)
          .isActive(true)
          .build();
      return cardSetRepository.save(newSet);
    });
  }

  private CardBlock getOrCreateCardBlock(JsonNode cardNode, CardMaster.GameType gameType) {
    String blockName = cardNode.has("regulationMark") ? cardNode.get("regulationMark").asText() : "N/A";
    if (blockName == null || blockName.strip().isEmpty())
      blockName = "N/A";

    String finalBlockName = blockName;
    return cardBlockRepository.findByNameAndGameType(finalBlockName, gameType).orElseGet(() -> {
      CardBlock newBlock = CardBlock.builder()
          .name(finalBlockName)
          .gameType(gameType)
          .isActive(true)
          .build();
      return cardBlockRepository.save(newBlock);
    });
  }

  // 👇 확장팩 코드 엔티티 생성 부분도 같이 "prodCode"로 수정해 주시면 좋습니다!
  private CardExpansionCode getOrCreateExpansionCode(JsonNode cardNode, CardSet cardSet, CardMaster.GameType gameType) {
    String codeName = cardNode.has("prodCode") ? cardNode.get("prodCode").asText() : cardSet.getExpansionCode();

    if (codeName == null || codeName.strip().isEmpty())
      return null;

    String finalCodeName = codeName;
    return cardExpansionCodeRepository.findByNameAndGameType(finalCodeName, gameType).orElseGet(() -> {
      CardExpansionCode newCode = CardExpansionCode.builder()
          .name(finalCodeName)
          .gameType(gameType)
          .isActive(true)
          .build();
      return cardExpansionCodeRepository.save(newCode);
    });
  }

  private Illustrator getOrCreateIllustrator(JsonNode cardNode) {
    // 🚀 수정: JSON 데이터의 "artist" 키를 가장 먼저 찾도록 변경!
    String illustratorName = "Unknown";

    if (cardNode.has("artist")) {
      illustratorName = cardNode.get("artist").asText();
    } else if (cardNode.has("illustrator")) { // 혹시 모를 다른 형태의 데이터를 위한 방어 코드
      illustratorName = cardNode.get("illustrator").asText();
    }

    // 이름이 비어있거나 이상한 값일 경우 기본값 처리
    if (illustratorName == null || illustratorName.strip().isEmpty() || "Unknown".equalsIgnoreCase(illustratorName)) {
      illustratorName = "Unknown";
    }

    String finalIllustratorName = illustratorName;
    return illustratorRepository.findByName(finalIllustratorName).orElseGet(() -> {
      Illustrator newIllustrator = Illustrator.builder()
          .name(finalIllustratorName)
          .isActive(true)
          .build();
      return illustratorRepository.save(newIllustrator);
    });
  }

  private Pokemon getOrCreatePokemon(JsonNode cardNode) {
    String rawName = cardNode.has("name") ? cardNode.get("name").asText() : "Unknown";
    if ("Unknown".equals(rawName))
      return null;

    // Remove TCG suffixes (V, VMAX, ex, etc.) to match base Pokemon names from
    // PokéAPI
    String cleanedName = cleanPokemonName(rawName);

    return pokemonRepository.findByName(cleanedName)
        .or(() -> pokemonRepository.findByName(rawName))
        .orElse(null);
  }

  private String cleanPokemonName(String name) {
    if (name == null)
      return null;
    // Regex to remove common TCG suffixes at the end of the name
    return name.replaceAll("\\s+(VMAX|VSTAR|V-UNION|V|ex|GX|BREAK|LV\\.X|\\(Prism Star\\)|\\(Radiant\\))$", "").trim();
  }

  private CardRarity getOrCreateRarity(JsonNode cardNode, CardMaster.GameType gameType) {
    String rarityName = cardNode.has("rarity") ? cardNode.get("rarity").asText() : "N/A";
    if (rarityName.strip().isEmpty())
      rarityName = "N/A";

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
    if (pokemonNode.has("subtypes") && pokemonNode.get("subtypes").isArray()
        && pokemonNode.get("subtypes").size() > 0) {
      String rawStage = pokemonNode.get("subtypes").get(0).asText();
      stageName = normalizeEvolutionStage(rawStage);
    }

    String finalStageName = stageName;
    return evolutionStageRepository.findByNameAndGameType(finalStageName, gameType).orElseGet(() -> {
      EvolutionStage newStage = EvolutionStage.builder()
          .name(finalStageName)
          .gameType(gameType)
          .sortOrder(determineSortOrder(finalStageName))
          .build();
      return evolutionStageRepository.save(newStage);
    });
  }

  private String normalizeEvolutionStage(String raw) {
    if (raw == null)
      return "기본";
    return switch (raw) {
      case "Basic" -> "기본";
      case "Stage 1" -> "1진화";
      case "Stage 2" -> "2진화";
      case "VMAX" -> "VMAX";
      case "VSTAR" -> "VSTAR";
      case "ex" -> "ex";
      case "GX" -> "GX";
      case "BREAK" -> "BREAK";
      case "Restored" -> "복원";
      default -> raw;
    };
  }

  private int determineSortOrder(String stageName) {
    return switch (stageName) {
      case "기본" -> 0;
      case "1진화" -> 1;
      case "2진화" -> 2;
      case "VMAX", "VSTAR", "ex", "GX" -> 3;
      default -> 99;
    };
  }

  private CardCategory determineCategory(JsonNode pokemonNode) {
    String supertype = pokemonNode.has("supertype") ? pokemonNode.get("supertype").asText() : "포켓몬";
    String categoryName = "POKEMON"; // Default

    if ("에너지".equals(supertype)) {
      categoryName = "ENERGY";

      // Check for more specific subtypes for Energy (Basic, Special)
      if (pokemonNode.has("subtypes") && pokemonNode.get("subtypes").isArray()
          && pokemonNode.get("subtypes").size() > 0) {
        String subtype = pokemonNode.get("subtypes").get(0).asText();
        categoryName = switch (subtype) {
          case "기본 에너지" -> "BASIC_ENERGY";
          case "특수 에너지" -> "SPECIAL_ENERGY";
          default -> "ENERGY";
        };
      }
    } else if ("트레이너스".equals(supertype)) {
      categoryName = "TRAINER";

      // Check for more specific subtypes for Trainers (Item, Support, Stadium, Tool)
      if (pokemonNode.has("subtypes") && pokemonNode.get("subtypes").isArray()
          && pokemonNode.get("subtypes").size() > 0) {
        String subtype = pokemonNode.get("subtypes").get(0).asText();
        categoryName = switch (subtype) {
          case "아이템" -> "ITEM";
          case "서포트" -> "SUPPORT";
          case "스타디움" -> "STADIUM";
          case "포켓몬 도구" -> "TOOL";
          default -> "TRAINER";
        };
      }
    }

    String finalCategoryName = categoryName;
    return cardCategoryRepository.findByNameAndGameType(finalCategoryName, CardMaster.GameType.POKEMON)
        .orElseGet(
            () -> cardCategoryRepository.findByNameAndGameType("POKEMON", CardMaster.GameType.POKEMON).orElse(null));
  }

  private ElementalType determineElementalType(JsonNode cardNode) {
    // 1. 키가 없거나 비어있으면 null 반환 (배열 검사 제거)
    if (!cardNode.has("type") || cardNode.get("type").isNull() || cardNode.get("type").asText().isEmpty()) {
      return null;
    }

    // 2. 문자열 값 가져오기 (예: "(물)")
    String rawType = cardNode.get("type").asText();

    // 3. 양옆 괄호 () 제거하고 공백 없애기 -> "물"
    String typeName = rawType.replace("(", "").replace(")", "").trim();

    return elementalTypeRepository.findByDisplayNameAndGameType(typeName, CardMaster.GameType.POKEMON)
        .orElseGet(() -> {
          // DB에 없다면 새로 생성해서 저장
          ElementalType newType = ElementalType.builder()
              .name(typeName) // 시스템 내부용 이름 (필요하면 영문 매핑)
              .displayName(typeName) // 화면에 보여줄 이름 ("물")
              .gameType(CardMaster.GameType.POKEMON)
              .build();
          return elementalTypeRepository.save(newType);
        });
  }
}