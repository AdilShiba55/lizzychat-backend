package chat.flirtbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ResizeImageDTO {
    @JsonProperty("id")
    private Long playgroundImageId;
    private String name;
    private byte[] content;
    @NotNull
    private Long target;
}
