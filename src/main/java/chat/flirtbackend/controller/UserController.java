package chat.flirtbackend.controller;

import chat.flirtbackend.dto.UserPublicEditDTO;
import chat.flirtbackend.dto.UserInfoDTO;
import chat.flirtbackend.service.SystemService;
import chat.flirtbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final SystemService systemService;
    private final UserService userService;

    public UserController(SystemService systemService, UserService userService) {
        this.systemService = systemService;
        this.userService = userService;
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        Boolean exists = userService.existByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoDTO> getUserInfo() {
        Long id = systemService.getTokenInfo().getId();
        UserInfoDTO userInfo = userService.getUserInfo(id);
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping
    public ResponseEntity<UserInfoDTO> update(@RequestBody @Valid UserPublicEditDTO form) {
        Long id = systemService.getTokenInfo().getId();
        userService.update(id, form);
        UserInfoDTO userInfo = userService.getUserInfo(id);
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/avatar")
    public ResponseEntity<HttpStatus> uploadAvatar(@RequestParam MultipartFile multipartFile) {
        Long id = systemService.getTokenInfo().getId();
        userService.uploadAvatar(multipartFile, id);
        return ResponseEntity.ok().build();
    }

}
