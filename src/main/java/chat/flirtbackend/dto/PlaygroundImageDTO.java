package chat.flirtbackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class PlaygroundImageDTO {
    private Long id;
    private String path;
    private String url;
}
