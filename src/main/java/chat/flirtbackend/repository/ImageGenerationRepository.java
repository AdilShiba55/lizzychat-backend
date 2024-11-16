package chat.flirtbackend.repository;

import chat.flirtbackend.entity.ImageGeneration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ImageGenerationRepository extends JpaRepository<ImageGeneration, Long> {

    @Query(value = "" +
            "select count(*) from image_generation\n" +
            "where user_id = :userId and dt_create between :from and :to", nativeQuery = true)
    Long messageCountAfter(Long userId, Date from, Date to);
}
