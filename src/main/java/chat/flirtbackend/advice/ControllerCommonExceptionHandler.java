package chat.flirtbackend.advice;

import chat.flirtbackend.dto.ExceptionResponse;
import chat.flirtbackend.exception.ApiException;
import chat.flirtbackend.util.UtError;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ControllerCommonExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleCustomException(Exception exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage());
        return ResponseEntity.badRequest().body(exceptionResponse);
    }
}
