package com.ridelink.account_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalStateException(IllegalStateException ex) {
        String message = ex.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if ("Account not found".equals(message)) {
            status = HttpStatus.NOT_FOUND;
        } else if ("Invalid email or password".equals(message)) {
            status = HttpStatus.UNAUTHORIZED;
        } else if ("Account is not active".equals(message)) {
            status = HttpStatus.FORBIDDEN;
        } else if ("Email already registered".equals(message)) {
            status = HttpStatus.BAD_REQUEST;
        }

        ApiError apiError = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                message,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = "Validation failed";
        if (ex.getBindingResult().hasErrors()) {
            errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        }
        
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError apiError = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                errorMessage,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError apiError = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                "Invalid request parameter",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError apiError = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                "Invalid request body",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, status);
    }
}
