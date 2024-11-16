package chat.flirtbackend.repository;

import chat.flirtbackend.entity.CharacterGender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterGenderRepository extends JpaRepository<CharacterGender, Long> {
}
