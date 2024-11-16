package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetImageResponse {

    private String image;
    private Long seed;
    private Double cost;
}
