package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ContactRequestInfoDTO {
    private Long id;
    private String text;
    private Date dtCreate;
    private Date dtArchive;
    private Long userId;
    private String username;
    private String userEmail;
}
