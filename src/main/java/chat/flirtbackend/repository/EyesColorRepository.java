package chat.flirtbackend.repository;

import chat.flirtbackend.entity.EyesColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EyesColorRepository extends JpaRepository<EyesColor, Long> {
}
