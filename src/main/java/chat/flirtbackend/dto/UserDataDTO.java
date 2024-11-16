package chat.flirtbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDataDTO {
    private TokenInfoDTO tokenInfo;
    private UserInfoDTO userInfo;
}
