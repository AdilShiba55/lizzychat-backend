package chat.flirtbackend.repository;

import chat.flirtbackend.entity.ClothesColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesColorRepository extends JpaRepository<ClothesColor, Long> {
}
