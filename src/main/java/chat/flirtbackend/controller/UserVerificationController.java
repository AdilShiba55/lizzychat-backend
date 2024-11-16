package chat.flirtbackend.controller;

import chat.flirtbackend.dto.TokenInfoDTO;
import chat.flirtbackend.dto.UserDataDTO;
import chat.flirtbackend.dto.UserInfoDTO;
import chat.flirtbackend.dto.VerifyEmailFormDTO;
import chat.flirtbackend.service.SystemService;
import chat.flirtbackend.service.UserService;
import chat.flirtbackend.service.UserVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/verification")
public class UserVerificationController {

    private final UserVerificationService userVerificationService;
    private final UserService userService;
    private final SystemService systemService;

    public UserVerificationController(UserVerificationService userVerificationService, UserService userService, SystemService systemService) {
        this.userVerificationService = userVerificationService;
        this.userService = userService;
        this.systemService = systemService;
    }

    @PostMapping("/code")
    public ResponseEntity<HttpStatus> sendVerificationCode() {
        Long id = systemService.getTokenInfo().getId();
        userVerificationService.sendVerificationCode(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<UserDataDTO> verify(@RequestBody @Valid VerifyEmailFormDTO form) {
        Long id = systemService.getTokenInfo().getId();
        TokenInfoDTO tokenInfo = userVerificationService.verifyEmail(form.getCode(), id);
        UserInfoDTO userInfo = userService.getUserInfo(id);
        return ResponseEntity.ok(new UserDataDTO(tokenInfo, userInfo));
    }
}
