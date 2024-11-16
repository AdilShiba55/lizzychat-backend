package chat.flirtbackend.repository;

import chat.flirtbackend.entity.Memory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {
    @Modifying
    @Query(value = "delete from memory where embedding_id in (:ids) and user_id = :userId", nativeQuery = true)
    void deleteByIdAndUserId(List<UUID> ids, Long userId);
}
