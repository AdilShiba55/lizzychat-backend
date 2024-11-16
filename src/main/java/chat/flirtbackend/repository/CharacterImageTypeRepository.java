package chat.flirtbackend.repository;

import chat.flirtbackend.entity.CharacterImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterImageTypeRepository extends JpaRepository<CharacterImageType, Long> {
}
