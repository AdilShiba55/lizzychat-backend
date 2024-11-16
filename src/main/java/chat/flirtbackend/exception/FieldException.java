package chat.flirtbackend.exception;

import chat.flirtbackend.dto.FieldErrorDTO;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FieldException extends RuntimeException {
    private final List<FieldErrorDTO> fieldErrors = new ArrayList<>();
    public FieldException(String fieldName, String error) {
        fieldErrors.add(new FieldErrorDTO(fieldName, error));
    }

    public FieldException(List<FieldErrorDTO> errorFields) {
        fieldErrors.addAll(errorFields);
    }
}
