package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAdminSearchDTO {
    private String text;
    private Boolean blocked;
    private Integer pageNum;
    private Integer pageSize;
}
