package chat.flirtbackend.service;

import chat.flirtbackend.dto.UserInfoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    void getUserInfo() {
        UserInfoDTO userInfo = userService.getUserInfo(1L);
        Assertions.assertNotNull(userInfo);
    }

    @Test
    void isUserBlocked() {
        boolean isUserBlocked = userService.isUserBlocked(1L);
        Assertions.assertNotNull(isUserBlocked);
    }
}