package chat.flirtbackend.repository;

import chat.flirtbackend.entity.BreastSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreastSizeRepository extends JpaRepository<BreastSize, Long> {
}
