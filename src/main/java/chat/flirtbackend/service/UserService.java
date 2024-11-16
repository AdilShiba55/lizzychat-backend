package chat.flirtbackend.service;

import chat.flirtbackend.dto.*;
import chat.flirtbackend.entity.Role;
import chat.flirtbackend.entity.User;
import chat.flirtbackend.entity.UserPlan;
import chat.flirtbackend.exception.ApiException;
import chat.flirtbackend.exception.FieldException;
import chat.flirtbackend.exception.UserNotFoundException;
import chat.flirtbackend.repository.UserRepository;
import chat.flirtbackend.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Value("${app.avatar-size-limit}")
    private Integer avatarSizeLimit;
    private final UserRepository userRepository;
    private final UserSearchService userSearchService;
    private final RoleService roleService;
    private final UserPlanService userPlanService;
    private final UserCrystalService userCrystalService;
    private final CharacterService characterService;
    private final MessageService messageService;
    private final ImageGenerationService imageGenerationService;
    private final MinioService minioService;
    private final ObjectMapper objectMapper;

    public UserService(UserRepository userRepository, UserSearchService userSearchService, RoleService roleService, UserPlanService userPlanService, UserCrystalService userCrystalService, CharacterService characterService, MessageService messageService, ImageGenerationService imageGenerationService, MinioService minioService, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.userSearchService = userSearchService;
        this.roleService = roleService;
        this.userPlanService = userPlanService;
        this.userCrystalService = userCrystalService;
        this.characterService = characterService;
        this.messageService = messageService;
        this.imageGenerationService = imageGenerationService;
        this.minioService = minioService;
        this.objectMapper = objectMapper;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void update(Long userId, UserPublicEditDTO form) {
        boolean isUsernameBusy = userRepository.findByUsernameAndNotUserId(form.getUsername(), userId) > 0;
        UtError.throwIf(isUsernameBusy, new FieldException("username", "This username is already in use"));
        User user = findById(userId);
        user.setUsername(form.getUsername());
        save(user);
    }

    public void update(UserAdminEditDTO form) {
        User user = findById(form.getId());
        user.setUsername(form.getUsername());
        Boolean blocked = form.getBlocked();
        if(blocked && user.getDtBlocked() == null) {
            user.setDtBlocked(new Date());
        } else if(!blocked) {
            user.setDtBlocked(null);
        }
        save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
    public User findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new FieldException("usernameOrEmail", "user with this name or email not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new FieldException("email", "user with this email not found"));
    }

    public UserInfoDTO getUserInfo(Long id) {
        User user = findById(id);
        UserInfoDTO userInfo = objectMapper.convertValue(user, UserInfoDTO.class);
        List<Role> roles = roleService.getRolesByUserId(id);
        userInfo.setRoles(roles);
        String avatarPath = userInfo.getAvatarPath();
        if(avatarPath != null && !avatarPath.isBlank()) {
            String avatarUrl = minioService.getObjectFromUserBucket(avatarPath);
            userInfo.setAvatarUrl(avatarUrl);
        }
        Long currentCrystalCount = userCrystalService.getCurrentCrystalCount(id);
        userInfo.setBalance(currentCrystalCount);
        List<Long> unlockedCharacterIds = characterService.getUnlockedCharacterIds(id);
        userInfo.setUnlockedCharacterIds(unlockedCharacterIds);

        UserPlanInfoDTO currentPlan = userPlanService.getCurrentPlan(id);
        if(currentPlan.getPlanId().equals(UtPlan.PREMIUM)) {
            userInfo.setPremiumDtEnd(currentPlan.getDtEnd());
        }
        userInfo.setTextMessageLimit(currentPlan.getTextMessageLimit());
        userInfo.setImageMessageLimit(currentPlan.getImageMessageLimit());

        long userLastMonthTextMessageCount = messageService.getUserLastDaysTextMessageCount(id);
        long userLastMonthImageMessageCount = imageGenerationService.getUserLastDaysImageMessageCount(id);
        userInfo.setUserLastMonthTextMessageCount(userLastMonthTextMessageCount);
        userInfo.setUserLastMonthImageMessageCount(userLastMonthImageMessageCount);
        return userInfo;
    }

    public boolean existByUsername(String username) {
        return userRepository.findCountByUsername(username) > 0;
    }

    public boolean existByEmail(String email) {
        return userRepository.findCountByEmail(email) > 0;
    }

    public String findEmailById(Long id) {
        return userRepository.findEmailById(id)
                .orElseThrow(() -> new FieldException("email", "email by this ID not found"));
    }

    public Long findUserIdByEmail(String email) {
        return userRepository.findUserIdByEmail(email)
                .orElseThrow(() -> new FieldException("email", "user with this email not found"));
    }

    public boolean isUserBlocked(Long id) {
        return userRepository.isBlocked(id);
    }

    @SneakyThrows
    @Transactional
    public void uploadAvatar(MultipartFile multipartFile, Long userId) {
        long maxFileSize = avatarSizeLimit * 1024 * 1024;
        UtError.throwIf(multipartFile.getSize() > maxFileSize, new ApiException("Avatar size is too large"));
        String path = userId + "/" + UUID.randomUUID();
        byte[] processedImageByteArray = UtImage.getProcessedImageByteArray(multipartFile, UtImage.IMAGE_TARGET_SIZE);
        minioService.putObjectToUserBucket(processedImageByteArray, multipartFile.getContentType(), path);
        userRepository.setAvatar(path, userId);
    }

    public SearchResponse<User> search(UserAdminSearchDTO filter) {
        return userSearchService.search(filter);
    }

}
