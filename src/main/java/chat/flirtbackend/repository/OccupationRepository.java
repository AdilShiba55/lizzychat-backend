package chat.flirtbackend.repository;

import chat.flirtbackend.entity.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OccupationRepository extends JpaRepository<Occupation, Long> {

    @Query(value = "" +
            "select * from occupation\n" +
            "join character_occupation co on co.character_id = :characterId\n", nativeQuery = true)
    List<Occupation> findOccupationsByCharacter(Long characterId);

    @Query(value = "" +
            "select occupation.id from occupation\n" +
            "join character_occupation co on co.occupation_id = occupation.id and co.character_id = :characterId\n", nativeQuery = true)
    List<Long> findOccupationIdsByCharacter(Long characterId);

    @Modifying
    @Query(value = "delete from character_occupation where character_id = :characterId", nativeQuery = true)
    void deleteByCharacterId(Long characterId);
}
