package chat.flirtbackend.dto;

import chat.flirtbackend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    private Long id;
    private String username;
    private String email;
    private String avatarPath;
    private String avatarUrl;
    private Date dtCreate;
    private Date dtVerified;
    private Date dtBlocked;
    private List<Role> roles;
    private Date premiumDtEnd;
    private Long balance;
    private List<Long> unlockedCharacterIds;
    private Long textMessageLimit;
    private Long imageMessageLimit;
    private Long userLastMonthTextMessageCount;
    private Long userLastMonthImageMessageCount;
}
