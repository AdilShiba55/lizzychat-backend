package chat.flirtbackend.repository;

import chat.flirtbackend.entity.Clothes;
import chat.flirtbackend.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesRepository extends JpaRepository<Clothes, Long> {
}
