package chat.flirtbackend.repository;

import chat.flirtbackend.entity.PlaygroundImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaygroundImageRepository extends JpaRepository<PlaygroundImage, Long> {
    @Query(value = "select * from playground_image order by id desc", nativeQuery = true)
    List<PlaygroundImage> findAllIdDesc();
}
