package chat.flirtbackend.util;

import chat.flirtbackend.dto.FieldErrorDTO;
import chat.flirtbackend.exception.FieldException;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class UtError {
    public static Map<String, List<String>> getStrMap(List<String> errors) {
        Map<String, List<String>> map = new HashMap<>();
        map.put("errors", errors);
        return map;
    }

    public static Map<String, List<FieldErrorDTO>> getFieldMap(List<FieldErrorDTO> errors) {
        Map<String, List<FieldErrorDTO>> map = new HashMap<>();
        map.put("errors", errors);
        return map;
    }

    public static Map<String, List<String>> getMap(String error) {
        return getStrMap(Collections.singletonList(error));
    }

    @SneakyThrows
    public static void throwIf(Boolean condition, Exception exception) {
        if(condition) {
            throw exception;
        }
    }

    public static void throwIf(FieldErrorDTO... fieldErrors) {
        List<FieldErrorDTO> matchingFieldErrors = Arrays.stream(fieldErrors)
                .filter(FieldErrorDTO::getCondition)
                .collect(Collectors.toList());
        if(!matchingFieldErrors.isEmpty()) {
            throw new FieldException(matchingFieldErrors);
        }
    }
}
