package chat.flirtbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SystemServiceTest {

    private final SystemService systemService;

    @Autowired
    public SystemServiceTest(SystemService systemService) {
        this.systemService = systemService;
    }

    @Test
    void isProdProfile() {
        boolean isProdProfile = systemService.isProdProfile();
        Assertions.assertNotNull(isProdProfile);
    }
}