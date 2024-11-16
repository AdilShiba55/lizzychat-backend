package chat.flirtbackend.repository;

import chat.flirtbackend.entity.ContactRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRequestRepository extends JpaRepository<ContactRequest, Long> {
    @Modifying
    @Query(value = "update contact_request set dt_archive = now()", nativeQuery = true)
    void archive(Long contactRequestId);
}
