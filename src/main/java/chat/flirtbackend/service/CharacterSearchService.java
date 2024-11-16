package chat.flirtbackend.service;

import chat.flirtbackend.dto.*;
import chat.flirtbackend.entity.Character;
import chat.flirtbackend.entity.Occupation;
import chat.flirtbackend.mapper.CharacterChatItemMapper;
import chat.flirtbackend.util.UtCharacterImageType;
import chat.flirtbackend.util.UtCharacterType;
import chat.flirtbackend.util.UtPagination;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CharacterSearchService {

    private final EntityManager entityManager;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final CharacterImageService characterImageService;
    private final CharacterService characterService;
    private final OccupationService occupationService;
    private final MinioService minioService;
    private final ObjectMapper objectMapper;

    public CharacterSearchService(EntityManager entityManager, NamedParameterJdbcTemplate namedParameterJdbcTemplate, CharacterImageService characterImageService, CharacterService characterService, OccupationService occupationService, MinioService minioService, ObjectMapper objectMapper) {
        this.entityManager = entityManager;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.characterImageService = characterImageService;
        this.characterService = characterService;
        this.occupationService = occupationService;
        this.minioService = minioService;
        this.objectMapper = objectMapper;
    }

    public SearchResponse<CharacterPublicDTO> search(CharacterPublicSearchDTO filter) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        builder.append("select c.*\n");
        builder.append("from character c where c.user_id is null\n");
        builder.append("and c.user_id is null and c.dt_archive is null\n");
        Long typeId = filter.getTypeId();
        if (typeId != null && typeId != 0) {
            List<Long> typeIds = new ArrayList<>();
            typeIds.add(typeId);
            if(typeId.equals(UtCharacterType.ANIME)) {
                typeIds.add(UtCharacterType.ANIMEv2);
            }
            params.put("typeIds", typeIds);
            builder.append("and c.type_id in (:typeIds)\n");
        }
        builder.append("order by " +
                "CASE c.type_id\n" +
                "when 1 then 1\n" +
                "when 2 then 2\n" +
                "when 4 then 3\n" +
                "when 3 then 4\n" +
                "else 5\n" +
                "end, c.id desc\n");

        List<Character> characters = getItemsQuery(builder.toString(), params)
                .setFirstResult(UtPagination.getPagination(filter.getPageNum(), filter.getPageSize()))
                .setMaxResults(filter.getPageSize())
                .getResultList();
        List<CharacterPublicDTO> items = characters
                .stream()
                .map(character -> {
                    CharacterPublicDTO item = objectMapper.convertValue(character, CharacterPublicDTO.class);
                    Occupation occupation = occupationService.findById(character.getOccupationId());
                    item.setOccupation(occupation);
                    List<String> avatarPaths = characterImageService.getImages(character.getId(), UtCharacterImageType.AVATAR);
                    List<String> avatarUrls = minioService.getObjectFromCharacterBucket(avatarPaths);
                    item.setAvatarUrls(avatarUrls);
                    return item;
                }).collect(Collectors.toList());

        Long total = namedParameterJdbcTemplate.queryForObject("select count(*) from (" + builder + ") t", params, Long.class);
        return new SearchResponse<>(items, total);
    }

    public SearchResponse<Character> search(CharacterAdminSearchDTO filter) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder("select * from character c where 1=1\n");

        Boolean systemOnly = filter.getSystemOnly();
        builder.append(systemOnly ? "and c.user_id is null\n" : "and c.user_id is not null\n");

        String text = filter.getText();
        if(text != null && !text.isBlank()) {
            params.put("text", text);
            builder.append("and (lower(c.name) like concat('%', lower(:text), '%') or lower(c.description) like concat('%', lower(:text), '%') or lower(c.text_prompt) like concat('%', lower(:text), '%'))\n");
        }
        Boolean archived = filter.getArchived();
        if(archived != null) {
            builder.append(archived ? "and c.dt_archive is not null\n" : "and c.dt_archive is null\n");
        }
        Boolean imageOnly = filter.getImageOnly();
        if(imageOnly != null) {
            builder.append(imageOnly ?
                    "and (select count(*) from character_image where character_id = c.id) > 0\n" :
                    "and (select count(*) from character_image where character_id = c.id) = 0\n");
        }
        builder.append("order by c.id desc\n");
        List<Character> characters = getItemsQuery(builder.toString(), params)
                .setFirstResult(UtPagination.getPagination(filter.getPageNum(), filter.getPageSize()))
                .setMaxResults(filter.getPageSize())
                .getResultList();
        Long total = ((BigInteger) getCountQuery(builder.toString(), params).getSingleResult()).longValue();
        return new SearchResponse<>(characters, total);
    }

    public SearchResponse<CharacterChatItemDTO> getCharacterChatItems(Long userId, CharacterChatItemsSearchDTO filter) {
        Map<String, Object> params = new HashMap<>();
        List<Long> unlockedCharacterIds = characterService.getUnlockedCharacterIds(userId);
        unlockedCharacterIds.add(0L);
        params.put("userId", userId);
        params.put("unlockedCharacterIds", unlockedCharacterIds);
        params.put("characterAvatarImageTypeId", UtCharacterImageType.AVATAR);
        StringBuilder builder = new StringBuilder();
        builder.append("" +
                "select c.id, c.name, m.text, ci.path avatarPath, m.id messageId, m.dt_create messageDtCreate,\n" +
                "m.character_image_id characterImageId\n" +
                "from character c\n" +
                "left join character_image ci on ci.id = (\n" +
                "select ci.id from character_image ci\n" +
                "where ci.character_id = c.id and ci.type_id = :characterAvatarImageTypeId\n" +
                "order by ci.id\n" +
                "limit 1" +
                "\n)\n" +
                "left join message m on m.id = (select m.id from message m where m.character_id = c.id and m.user_id = :userId order by id desc limit 1)\n" +
                "where (c.user_id is null or c.user_id = :userId)" +
                "and c.dt_archive is null and (c.cost = 0 or c.id in (:unlockedCharacterIds))\n");

        String text = filter.getText();
        if (text != null && !text.isBlank()) {
            params.put("text", text);
            builder.append("and (lower(c.name) like concat('%', lower(:text), '%') or lower(m.text) like concat('%', lower(:text), '%'))\n");
        }

        // берем тотал до того, как сетим limit и offset
        Long total = ((BigInteger) getCountQuery(builder.toString(), params).getSingleResult()).longValue();

        params.put("limit", filter.getPageSize());
        params.put("offset", UtPagination.getPagination(filter.getPageNum(), filter.getPageSize()));
        builder.append("order by m.id is not null desc, messageDtCreate desc, c.type_id, c.id desc\n");
        builder.append("limit :limit offset :offset\n");

        List<CharacterChatItemDTO> items = namedParameterJdbcTemplate.query(builder.toString(), params, new CharacterChatItemMapper());
        items.forEach(item -> {
            if(item.getAvatarPath() != null) {
                String avatarUrl = minioService.getObjectFromCharacterBucket(item.getAvatarPath());
                item.setAvatarUrl(avatarUrl);
            }
        });
        return new SearchResponse<>(items, total);
    }

    public SearchResponse<CharacterPublicDTO> search(Long userId, CharacterOwnSearchDTO filter) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        StringBuilder builder = new StringBuilder();
        builder.append("select * from character\n");
        builder.append("where user_id = :userId and dt_archive is null\n");
        builder.append("order by id desc\n");

        List<Character> characters = getItemsQuery(builder.toString(), params)
                .setFirstResult(UtPagination.getPagination(filter.getPageNum(), filter.getPageSize()))
                .setMaxResults(filter.getPageSize())
                .getResultList();
        List<CharacterPublicDTO> items = characters
                .stream()
                .map(character -> {
                    CharacterPublicDTO item = objectMapper.convertValue(character, CharacterPublicDTO.class);
                    Occupation occupation = occupationService.findById(character.getOccupationId());
                    item.setOccupation(occupation);
                    List<String> avatarPaths = characterImageService.getImages(character.getId(), UtCharacterImageType.AVATAR);
                    List<String> avatarUrls = minioService.getObjectFromCharacterBucket(avatarPaths);
                    item.setAvatarUrls(avatarUrls);
                    return item;
                }).collect(Collectors.toList());

        Long total = namedParameterJdbcTemplate.queryForObject("select count(*) from (" + builder + ") t", params, Long.class);
        return new SearchResponse<>(items, total);
    }

    private Query getItemsQuery(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(sql, Character.class);
        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));
        return query;
    }

    private Query getCountQuery(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery("select count(*) from (" + sql + ") t");
        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));
        return query;
    }
}
