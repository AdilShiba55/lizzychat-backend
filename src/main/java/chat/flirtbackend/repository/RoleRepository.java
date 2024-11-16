package chat.flirtbackend.repository;

import chat.flirtbackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "" +
            "select role.* from role\n" +
            "join usr_role ur on ur.role_id = role.id\n" +
            "where ur.role_id = :userId", nativeQuery = true)
    List<Role> getRolesByByUserId(Long userId);
}
