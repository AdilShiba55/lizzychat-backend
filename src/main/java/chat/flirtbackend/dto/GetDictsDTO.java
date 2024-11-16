package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class GetDictsDTO {
    @NotNull
    @Size(min = 1)
    private List<String> dictNames;
}
