package chat.flirtbackend.repository;

import chat.flirtbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select * from usr where username = :usernameOrEmail or email = :usernameOrEmail", nativeQuery = true)
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);

    @Query(value = "select * from usr where email = :email", nativeQuery = true)
    Optional<User> findByEmail(String email);

    @Query(value = "select count(*) from usr where username = :username", nativeQuery = true)
    Long findCountByUsername(String username);

    @Query(value = "select count(*) from usr where username = :username and id != :userId", nativeQuery = true)
    Long findByUsernameAndNotUserId(String username, Long userId);

    @Query(value = "select count(*) from usr where email = :email", nativeQuery = true)
    Long findCountByEmail(String email);

    @Query(value = "select email from usr where id = :id", nativeQuery = true)
    Optional<String> findEmailById(Long id);

    @Query(value = "select id from usr where email = :email", nativeQuery = true)
    Optional<Long> findUserIdByEmail(String email);

    @Query(value = "select case when count(*) > 0 then true else false end from usr where dt_blocked is not null and id = :userId", nativeQuery = true)
    Boolean isBlocked(Long userId);

    @Modifying
    @Query(value = "update usr set avatar_path = :avatarName where id = :userId", nativeQuery = true)
    void setAvatar(String avatarName, Long userId);
}
