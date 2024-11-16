package chat.flirtbackend.service;

import chat.flirtbackend.dto.UserPlanInfoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserPlanServiceTest {

    private final UserPlanService userPlanService;

    @Autowired
    public UserPlanServiceTest(UserPlanService userPlanService) {
        this.userPlanService = userPlanService;
    }
}