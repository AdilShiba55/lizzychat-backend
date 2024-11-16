package chat.flirtbackend.controller;

import chat.flirtbackend.dto.*;
import chat.flirtbackend.service.AuthService;
import chat.flirtbackend.service.SystemService;
import chat.flirtbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final SystemService systemService;

    public AuthController(AuthService authService, UserService userService, SystemService systemService) {
        this.authService = authService;
        this.userService = userService;
        this.systemService = systemService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDataDTO> register(@RequestBody @Valid RegisterFormDTO registerForm) {
        TokenInfoDTO tokenInfo = authService.register(registerForm);
        UserInfoDTO userInfo = userService.getUserInfo(tokenInfo.getId());
        return ResponseEntity.ok(new UserDataDTO(tokenInfo, userInfo));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDataDTO> login(@RequestBody @Valid LoginFormDTO loginForm) {
        TokenInfoDTO tokenInfo = authService.login(loginForm);
        UserInfoDTO userInfo = userService.getUserInfo(tokenInfo.getId());
        return ResponseEntity.ok(new UserDataDTO(tokenInfo, userInfo));
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserDataDTO> refresh() {
        Long id = systemService.getTokenInfo().getId();
        TokenInfoDTO tokenInfo = authService.refresh(id);
        UserInfoDTO userInfo = userService.getUserInfo(tokenInfo.getId());
        return ResponseEntity.ok(new UserDataDTO(tokenInfo, userInfo));
    }
}
