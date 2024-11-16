package chat.flirtbackend.controller;

import chat.flirtbackend.dto.SearchResponse;
import chat.flirtbackend.dto.UserAdminEditDTO;
import chat.flirtbackend.dto.UserInfoDTO;
import chat.flirtbackend.dto.UserAdminSearchDTO;
import chat.flirtbackend.entity.User;
import chat.flirtbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/user")
public class UserAdminController {

    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Secured("ROLE_admin")
    public ResponseEntity<SearchResponse<User>> search(@ModelAttribute UserAdminSearchDTO filter) {
        SearchResponse<User> result = userService.search(filter);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Secured("ROLE_admin")
    public ResponseEntity<UserInfoDTO> findById(@PathVariable Long id) {
        UserInfoDTO userInfo = userService.getUserInfo(id);
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping
    @Secured("ROLE_admin")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid UserAdminEditDTO form) {
        userService.update(form);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/avatar")
    @Secured("ROLE_admin")
    public ResponseEntity<HttpStatus> uploadAvatar(@RequestParam Long id, @RequestParam MultipartFile multipartFile) {
        userService.uploadAvatar(multipartFile, id);
        return ResponseEntity.ok().build();
    }
}
