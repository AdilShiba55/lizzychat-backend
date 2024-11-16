package chat.flirtbackend.repository;

import chat.flirtbackend.entity.UserPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPlanRepository extends JpaRepository<UserPlan, Long> {
    @Modifying
    @Query(value = "update usr_plan set dt_end = now() where user_id = :userId and dt_end > now()", nativeQuery = true)
    void finishPreviousPlans(Long userId);
}
