package chat.flirtbackend.repository;

import chat.flirtbackend.entity.HairStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HairStyleRepository extends JpaRepository<HairStyle, Long> {
}
