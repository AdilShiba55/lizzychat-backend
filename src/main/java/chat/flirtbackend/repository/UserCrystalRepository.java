package chat.flirtbackend.repository;

import chat.flirtbackend.entity.UserCrystal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCrystalRepository extends JpaRepository<UserCrystal, Long> {
    @Query(value = "" +
            "select (\n" +
            "    (select coalesce(sum(uc.count), 0) from usr_crystal uc where uc.user_id = :userId) - (select coalesce(sum(up.cost), 0) from usr_purchase up where up.user_id = :userId)\n" +
            ") balance", nativeQuery = true)
    Long getCurrentCrystalCount(Long userId);
}
