package chat.flirtbackend.controller;

import chat.flirtbackend.dto.*;
import chat.flirtbackend.service.GoogleAuthService;
import chat.flirtbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/google/auth")
public class GoogleAuthController {

    private final GoogleAuthService googleAuthService;
    private final UserService userService;

    public GoogleAuthController(GoogleAuthService googleAuthService, UserService userService) {
        this.googleAuthService = googleAuthService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDataDTO> register(@RequestBody @Valid GoogleRegisterDTO googleRegister) {
        TokenInfoDTO tokenInfo = googleAuthService.register(googleRegister);
        UserInfoDTO userInfo = userService.getUserInfo(tokenInfo.getId());
        return ResponseEntity.ok(new UserDataDTO(tokenInfo, userInfo));
    }


    @PostMapping("/login")
    public ResponseEntity<UserDataDTO> login(@RequestBody @Valid GoogleLoginDTO googleLogin) {
        TokenInfoDTO tokenInfo = googleAuthService.login(googleLogin.getAccessToken());
        UserInfoDTO userInfo = userService.getUserInfo(tokenInfo.getId());
        return ResponseEntity.ok(new UserDataDTO(tokenInfo, userInfo));
    }

}
