package chat.flirtbackend.repository;

import chat.flirtbackend.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "select * from message where user_id = :userId and character_id = :characterId order by id desc", nativeQuery = true)
    Page<Message> getMessages(Long userId, Long characterId, Pageable pageable);

    @Query(value = "select dt_create from message where user_id = :userId order by id desc limit 1", nativeQuery = true)
    Optional<Date> getLastMessageDate(Long userId);

    @Query(value = "" +
            "select count(*) from message\n" +
            "where user_id = :userId and type_id in :messageTypes and dt_create between :from and :to", nativeQuery = true)
    Long messageCountAfter(Long userId, Date from, Date to, List<Long> messageTypes);
}
