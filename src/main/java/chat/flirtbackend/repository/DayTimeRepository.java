package chat.flirtbackend.repository;

import chat.flirtbackend.entity.DayTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayTimeRepository extends JpaRepository<DayTime, Long> {
}
