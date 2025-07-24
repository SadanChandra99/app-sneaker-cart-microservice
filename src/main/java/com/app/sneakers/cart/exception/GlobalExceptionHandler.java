package com.app.sneakers.cart.exception;

import com.app.sneakers.cart.exception.model.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                    HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        return buildErrorResponse("VALIDATION_ERROR",
                "Validation failed for request",
                fieldErrors.toString(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex,
                                                              HttpServletRequest request) {
        return buildErrorResponse("ENTITY_NOT_FOUND",
                ex.getMessage(),
                "Requested entity does not exist",
                request.getRequestURI(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
                                                               HttpServletRequest request) {
        return buildErrorResponse("ILLEGAL_ARGUMENT",
                ex.getMessage(),
                "Invalid input provided",
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaught(Exception ex, HttpServletRequest request) {
        return buildErrorResponse("INTERNAL_SERVER_ERROR",
                ex.getMessage(),
                "Unexpected server error occurred",
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String errorCode,
                                                             String errorMessage,
                                                             String reason,
                                                             String path,
                                                             HttpStatus status) {
        ErrorResponse error = ErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .reason(reason)
                .status(status.value())
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, status);
    }
}
