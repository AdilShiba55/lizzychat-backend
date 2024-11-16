package chat.flirtbackend.service;

import chat.flirtbackend.dto.GoogleUserInfoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GoogleAuthServiceTest {

    private final GoogleAuthService googleAuthService;

    @Autowired
    public GoogleAuthServiceTest(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @Test
    void getGoogleUserInfo() {
        GoogleUserInfoDTO googleUserInfo = googleAuthService.getGoogleUserInfo("ya29.a0AXooCgvSdlVVdfsaahZw3g335cUXV0HUmmV8ZcE28gHsvvLTvE_l99EL7ehfNk8oL-NLl2ofUXUBMgUuiW1M75QGyOaTDQ6bw63-yZo4urZzVRlQNMrcX6pjI41PLEF0C5sIHZ52PBBqs1CCqZatNNYVUTCUScJ8m8IaCgYKAWkSARISFQHGX2Mig6l0uHZSvC3MuCl_3ZLSnw0170");
        Assertions.assertNotNull(googleUserInfo);
    }
}