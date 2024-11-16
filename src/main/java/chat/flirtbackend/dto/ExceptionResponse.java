package chat.flirtbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExceptionResponse {
    private String message;
    private List<FieldErrorDTO> errors;

    public ExceptionResponse(String message) {
        this.message = message;
    }

    public ExceptionResponse(List<FieldErrorDTO> errors) {
        this.errors = errors;
    }
}
