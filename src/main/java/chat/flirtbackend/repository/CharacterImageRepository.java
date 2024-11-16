package chat.flirtbackend.repository;

import chat.flirtbackend.entity.CharacterImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterImageRepository extends JpaRepository<CharacterImage, Long> {

    @Query(value = "select * from character_image ci where ci.character_id = :characterId and ci.type_id = :characterImageTypeId", nativeQuery = true)
    List<CharacterImage> findByCharacterIdAndTypeId(Long characterId, Long characterImageTypeId);

    @Query(value = "select path from character_image where character_id = :characterId and type_id = :characterImageTypeId order by id", nativeQuery = true)
    List<String> getImages(Long characterId, Long characterImageTypeId);

    @Modifying
    @Query(value = "delete from character_image ci where ci.character_id = :characterId and ci.type_id = :typeId", nativeQuery = true)
    void deleteImages(Long characterId, Long typeId);

    @Query(value = "" +
            "select * from character_image ci\n" +
            "where ci.character_id = :characterId and ci.type_id = :characterImageTypeId and ci.id not in (\n" +
            "select message.character_image_id from message\n" +
            "where message.character_image_id is not null and user_id = :userId\n" +
            ")\n" +
            "order by ci.id\n" +
            "limit 1", nativeQuery = true)
    Optional<CharacterImage> findNotSentImage(Long userId, Long characterId, Long characterImageTypeId);

    @Query(value = "" +
            "select ci.path from character_image ci\n" +
            "join character on character.id = ci.character_id and character.id = (select id from character where dt_reference is not null limit 1)\n" +
            "where ci.type_id = 1", nativeQuery = true)
    List<String> getReferenceCharacterImagePath();
}
