package chat.flirtbackend.advice;

import chat.flirtbackend.dto.ExceptionResponse;
import chat.flirtbackend.dto.FieldErrorDTO;
import chat.flirtbackend.dto.MessageCountLimitException;
import chat.flirtbackend.exception.ApiException;
import chat.flirtbackend.exception.CharacterNotUnlockedException;
import chat.flirtbackend.exception.FieldException;
import chat.flirtbackend.util.UtError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerSpecificExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponse> handleValidationErrors(BindException exception) {
        List<FieldErrorDTO> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(new ExceptionResponse(errors));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationErrors(MethodArgumentNotValidException exception) {
        List<FieldErrorDTO> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(new ExceptionResponse(errors));
    }

    @ExceptionHandler(FieldException.class)
    public ResponseEntity<ExceptionResponse> handleCustomException(FieldException fieldException) {
        List<FieldErrorDTO> errors = fieldException.getFieldErrors();
        return ResponseEntity.badRequest().body(new ExceptionResponse(errors));
    }

    @ExceptionHandler(CharacterNotUnlockedException.class)
    public ResponseEntity<ExceptionResponse> handleCharacterNotUnlocked(CharacterNotUnlockedException characterNotUnlockedException) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(characterNotUnlockedException.getMessage());
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

}
