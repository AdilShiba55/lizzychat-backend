package chat.flirtbackend.controller;

import chat.flirtbackend.dto.*;
import chat.flirtbackend.service.UserPasswordResetService;
import chat.flirtbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user-password")
public class UserPasswordController {

    private final UserPasswordResetService userPasswordResetService;
    private final UserService userService;

    public UserPasswordController(UserPasswordResetService userPasswordResetService, UserService userService) {
        this.userPasswordResetService = userPasswordResetService;
        this.userService = userService;
    }

    @PostMapping("/code")
    public ResponseEntity<HttpStatus> sendPasswordResetCode(@RequestBody @Valid SendResetPasswordCodeDTO sendResetPasswordCodeDTO) {
        userPasswordResetService.sendResetCode(sendResetPasswordCodeDTO.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<UserDataDTO> resetPassword(@RequestBody @Valid ResetPasswordDTO resetPassword) {
        TokenInfoDTO tokenInfo = userPasswordResetService.changePassword(resetPassword);
        UserInfoDTO userInfo = userService.getUserInfo(tokenInfo.getId());
        return ResponseEntity.ok(new UserDataDTO(tokenInfo, userInfo));
    }
}
