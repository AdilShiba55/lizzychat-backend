package chat.flirtbackend.repository;

import chat.flirtbackend.entity.BodyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodyTypeRepository extends JpaRepository<BodyType, Long> {
}
