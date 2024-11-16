package chat.flirtbackend.service;

import chat.flirtbackend.entity.Role;
import chat.flirtbackend.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getRolesByUserId(Long userId) {
        return roleRepository.getRolesByByUserId(userId);
    }
}
