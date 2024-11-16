package chat.flirtbackend.service;

import chat.flirtbackend.dto.*;
import chat.flirtbackend.entity.CharacterImage;
import chat.flirtbackend.entity.Faq;
import chat.flirtbackend.entity.UserPlan;
import chat.flirtbackend.util.UtCharacterImageType;
import chat.flirtbackend.util.UtPagination;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CharacterImageSearchService {
    private final EntityManager entityManager;
    private final UserPlanService userPlanService;
    private final MinioService minioService;

    public CharacterImageSearchService(EntityManager entityManager, UserPlanService userPlanService, MinioService minioService) {
        this.entityManager = entityManager;
        this.userPlanService = userPlanService;
        this.minioService = minioService;
    }

    public SearchResponse<CharacterImageDTO> search(Long userId, CharacterImagePublicSearchDTO filter) {
        boolean isPremium = userPlanService.isPremium(userId);

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        StringBuilder builder = new StringBuilder("" +
                "select ci.* from character_image ci\n" +
                "join character on character.id = ci.character_id\n" +
                "join message m on ci.id = m.character_image_id\n" +
                "where m.character_image_id is not null and m.user_id = :userId and character.dt_archive is null\n" +
                "order by m.dt_create desc\n");

        Long characterImageTypeId = filter.getCharacterImageTypeId();
        if (characterImageTypeId != null) {
            params.put("characterImageTypeId", characterImageTypeId);
            builder.append("and ci.type_id = :characterImageTypeId");
        }
        List<CharacterImage> characterImages = getItemsQuery(builder.toString(), params)
                .setFirstResult(UtPagination.getPagination(filter.getPageNum(), filter.getPageSize()))
                .setMaxResults(filter.getPageSize())
                .getResultList();
        List<CharacterImageDTO> items = characterImages.stream().map(image -> {
            CharacterImageDTO.CharacterImageDTOBuilder imageDTOBuilder = CharacterImageDTO.builder()
                    .id(image.getId())
                    .characterId(image.getCharacterId())
                    .typeId(image.getTypeId());

            if (!isPremium && image.getTypeId().equals(UtCharacterImageType.NAKED)) {
                String imageBluredUrl = minioService.getObjectFromCharacterBucket(image.getBluredPath());
                imageDTOBuilder.imageBluredUrl(imageBluredUrl);
            } else {
                String imageUrl = minioService.getObjectFromCharacterBucket(image.getPath());
                imageDTOBuilder.imageUrl(imageUrl);
            }
            return imageDTOBuilder.build();
        }).collect(Collectors.toList());

        Long total = ((BigInteger) getCountQuery(builder.toString(), params).getSingleResult()).longValue();
        return new SearchResponse<>(items, total);
    }

    private Query getItemsQuery(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(sql, CharacterImage.class);
        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));
        return query;
    }

    private Query getCountQuery(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery("select count(*) from (" + sql + ") t");
        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));
        return query;
    }
}
