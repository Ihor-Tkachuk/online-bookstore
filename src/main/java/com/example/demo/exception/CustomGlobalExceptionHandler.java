package com.example.demo.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String TIMESTAMP = "timestamp";
    private static final String ERRORS = "errors";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        List<Object> errors = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList();
        return buildResponseEntity(errors, headers, BAD_REQUEST);
    }

    private Object getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            return ((FieldError) e).getField() + " " + e.getDefaultMessage();
        }
        return e.getDefaultMessage();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildResponseEntity(ex.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(DataProcessingException.class)
    public ResponseEntity<Object> handleDataProcessingException(DataProcessingException ex) {
        return buildResponseEntity(ex.getMessage(), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Object> handleRegistrationException(RegistrationException ex) {
        return buildResponseEntity(ex.getMessage(), CONFLICT);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Object> usernameNotFoundException(AuthorizationDeniedException ex) {
        return buildResponseEntity(ex.getMessage(), FORBIDDEN);
    }

    private ResponseEntity<Object> buildResponseEntity(Object errors, HttpStatusCode status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(ERRORS, errors);
        return new ResponseEntity<>(body, status);
    }

    private ResponseEntity<Object> buildResponseEntity(Object errors, HttpHeaders headers,
                                                       HttpStatusCode status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(ERRORS, errors);
        return new ResponseEntity<>(body, headers, status);
    }
}
