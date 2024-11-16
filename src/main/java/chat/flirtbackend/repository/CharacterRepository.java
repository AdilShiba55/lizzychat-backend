package chat.flirtbackend.repository;

import chat.flirtbackend.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    @Query(value = "" +
            "select character_id from usr_purchase up\n" +
            "where up.user_id = :userId and up.type_id = 1", nativeQuery = true)
    List<Long> getUnlockedCharacterIds(Long userId);

    @Query(value = "select cost from character where id = :characterId", nativeQuery = true)
    Long getCharacterCost(Long characterId);

    @Query(value = "select count(*) from character where id = :characterId and user_id = :userId", nativeQuery = true)
    Long getCharacterCountByUser(Long characterId, Long userId);

    @Modifying
    @Query(value = "update character set dt_reference = null where dt_reference is not null", nativeQuery = true)
    void removeAllDtReferenceMark();

    @Query(value = "select * from character where dt_reference is not null order by id limit 1", nativeQuery = true)
    Optional<Character> getReferenceCharacter();
}
