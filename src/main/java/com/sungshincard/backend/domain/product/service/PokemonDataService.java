package com.sungshincard.backend.domain.product.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sungshincard.backend.domain.product.entity.Pokemon;
import com.sungshincard.backend.domain.product.repository.PokemonRepository;
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
public class PokemonDataService {

    private final PokemonRepository pokemonRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String POKE_API_BASE_URL = "https://pokeapi.co/api/v2/";

    @Transactional
    public void initializePokemonData() {
        long count = pokemonRepository.count();
        boolean hasIncompleteData = count > 0 && pokemonRepository.existsByRegionIsNull();

        if (count > 0 && !hasIncompleteData) {
            log.info("Pokemon data already initialized and complete. Skipping...");
            return;
        }

        if (hasIncompleteData) {
            log.info("Incomplete Pokemon data detected (missing regions). Re-initializing...");
            pokemonRepository.deleteAll();
        } else {
            log.info("Starting Pokemon data initialization from PokéAPI...");
        }

        try {
            // 1. Get total count and list (Limit to all 1025+ Pokemon)
            String listUrl = POKE_API_BASE_URL + "pokemon?limit=1025"; 
            JsonNode rootNode = restTemplate.getForObject(listUrl, JsonNode.class);
            if (rootNode == null || !rootNode.has("results")) {
                log.error("Failed to fetch Pokemon list from PokéAPI");
                return;
            }
            JsonNode results = rootNode.get("results");

            List<Pokemon> pokemonList = new ArrayList<>();

            for (JsonNode node : results) {
                String name = node.get("name").asText();
                String url = node.get("url").asText();
                String[] parts = url.split("/");
                int id = Integer.parseInt(parts[parts.length - 1]);

                log.info("Fetching data for Pokemon #{}...", id);

                // 2. Fetch Korean Name from Pokemon Species
                String koreanName = getKoreanName(id);
                String finalName = koreanName != null ? koreanName : name;

                // 3. Fetch Image URL from Pokemon Detail & Fetch Types
                String imageUrl = getOfficialImageUrl(id);
                String koreanTypes = getKoreanTypes(id);

                log.info("Found: {} (#{}) - Type: {}, Region: {}", finalName, id, koreanTypes, getRegionById(id));

                Pokemon pokemon = Pokemon.builder()
                        .dexNumber(id)
                        .name(finalName)
                        .imageUrl(imageUrl)
                        .type(koreanTypes)
                        .region(getRegionById(id))
                        .build();

                pokemonList.add(pokemon);

                // Save in batches or at once
                if (pokemonList.size() >= 50) {
                    pokemonRepository.saveAll(pokemonList);
                    pokemonList.clear();
                }
            }

            if (!pokemonList.isEmpty()) {
                pokemonRepository.saveAll(pokemonList);
            }

            log.info("Pokemon data initialization completed successfully!");

        } catch (Exception e) {
            log.error("Error during Pokemon data initialization", e);
        }
    }

    private String getKoreanName(int id) {
        try {
            String speciesUrl = POKE_API_BASE_URL + "pokemon-species/" + id;
            JsonNode speciesNode = restTemplate.getForObject(speciesUrl, JsonNode.class);
            if (speciesNode == null || !speciesNode.has("names")) return null;
            JsonNode names = speciesNode.get("names");

            for (JsonNode nameNode : names) {
                if ("ko".equals(nameNode.get("language").get("name").asText())) {
                    return nameNode.get("name").asText();
                }
            }
        } catch (Exception e) {
            log.warn("Failed to fetch Korean name for Pokemon #{}", id);
        }
        return null;
    }

    private String getKoreanTypes(int id) {
        try {
            String detailUrl = POKE_API_BASE_URL + "pokemon/" + id;
            JsonNode detailNode = restTemplate.getForObject(detailUrl, JsonNode.class);
            if (detailNode == null || !detailNode.has("types")) return null;
            
            List<String> typeList = new ArrayList<>();
            for (JsonNode typeNode : detailNode.get("types")) {
                String typeName = typeNode.get("type").get("name").asText();
                typeList.add(mapTypeToKorean(typeName));
            }
            return String.join(", ", typeList);
        } catch (Exception e) {
            log.warn("Failed to fetch types for Pokemon #{}", id);
        }
        return null;
    }

    private String mapTypeToKorean(String type) {
        return switch (type.toLowerCase()) {
            case "fire" -> "불꽃";
            case "water" -> "물";
            case "grass" -> "풀";
            case "electric" -> "번개";
            case "psychic" -> "초";
            case "ice" -> "얼음";
            case "dragon" -> "드래곤";
            case "dark" -> "악";
            case "fairy" -> "요정";
            case "normal" -> "무색";
            case "fighting" -> "격투";
            case "flying" -> "비행";
            case "poison" -> "독";
            case "ground" -> "땅";
            case "rock" -> "바위";
            case "bug" -> "벌레";
            case "ghost" -> "고스트";
            case "steel" -> "강철";
            default -> type;
        };
    }

    private String getOfficialImageUrl(int id) {
        try {
            String detailUrl = POKE_API_BASE_URL + "pokemon/" + id;
            JsonNode detailNode = restTemplate.getForObject(detailUrl, JsonNode.class);
            if (detailNode == null || !detailNode.has("sprites")) return null;
            return detailNode.get("sprites")
                    .get("other")
                    .get("official-artwork")
                    .get("front_default").asText();
        } catch (Exception e) {
            log.warn("Failed to fetch official artwork for Pokemon #{}", id);
        }
        return null;
    }

    private String getRegionById(int id) {
        if (id <= 151) return "관동";
        if (id <= 251) return "성도";
        if (id <= 386) return "호연";
        if (id <= 493) return "신오";
        if (id <= 649) return "하나";
        if (id <= 721) return "칼로스";
        if (id <= 809) return "알로라";
        if (id <= 898) return "가라르";
        if (id <= 905) return "히스이";
        return "팔데아";
    }
}
