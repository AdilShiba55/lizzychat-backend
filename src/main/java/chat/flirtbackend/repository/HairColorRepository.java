package chat.flirtbackend.repository;

import chat.flirtbackend.entity.HairColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HairColorRepository extends JpaRepository<HairColor, Long> {
}
