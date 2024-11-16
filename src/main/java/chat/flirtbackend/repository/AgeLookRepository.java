package chat.flirtbackend.repository;

import chat.flirtbackend.entity.AgeLook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgeLookRepository extends JpaRepository<AgeLook, Long> {
}
