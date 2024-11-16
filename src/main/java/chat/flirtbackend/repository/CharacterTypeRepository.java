package chat.flirtbackend.repository;

import chat.flirtbackend.entity.CharacterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterTypeRepository extends JpaRepository<CharacterType, Long> {
}
