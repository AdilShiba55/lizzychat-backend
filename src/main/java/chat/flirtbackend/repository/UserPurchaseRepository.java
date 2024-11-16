package chat.flirtbackend.repository;

import chat.flirtbackend.entity.UserPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPurchaseRepository extends JpaRepository<UserPurchase, Long> {
    @Query(value = "" +
            "select * from usr_purchase up\n" +
            "where up.user_id = :userId and up.character_id = :characterId and up.type_id = :typeId", nativeQuery = true)
    Optional<UserPurchase> getPurchase(Long userId, Long characterId, Long typeId);

}
