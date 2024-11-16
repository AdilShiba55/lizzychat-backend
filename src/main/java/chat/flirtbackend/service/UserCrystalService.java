package chat.flirtbackend.service;

import chat.flirtbackend.entity.UserCrystal;
import chat.flirtbackend.repository.UserCrystalRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserCrystalService {

    private final UserCrystalRepository userCrystalRepository;

    public UserCrystalService(UserCrystalRepository userCrystalRepository) {
        this.userCrystalRepository = userCrystalRepository;
    }

    public void create(Long userId, Long count, Long myPaymentId) {
        UserCrystal userCrystal = new UserCrystal(userId, count, myPaymentId, new Date());
        userCrystalRepository.save(userCrystal);
    }

    public Long getCurrentCrystalCount(Long userId) {
        return userCrystalRepository.getCurrentCrystalCount(userId);
    }
}
