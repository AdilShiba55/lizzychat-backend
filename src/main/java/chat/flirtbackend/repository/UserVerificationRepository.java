package chat.flirtbackend.repository;

import chat.flirtbackend.entity.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
    @Query(value = "select * from usr_verification where user_id = :userId order by dt_create desc limit 1", nativeQuery = true)
    Optional<UserVerification> findLast(Long userId);

    @Modifying
    @Query(value = "delete from usr_verification where user_id = :userId", nativeQuery = true)
    void removeAll(Long userId);
}
