package com.lamnd.zerotohero.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lamnd.zerotohero.dto.reponse.APIResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleOtherException(Exception ex) {
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        APIResponse response = new APIResponse();

        response.setCode(errorCode.getCode());
        response.setMessage(ex.getMessage());

        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<APIResponse> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        APIResponse response = new APIResponse();

        response.setCode(errorCode.getCode());
        response.setMessage(ex.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        APIResponse response = new APIResponse();

        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        APIResponse<?> response =
                APIResponse.builder().code(404).message(ex.getMessage()).build();

        return new ResponseEntity<APIResponse<?>>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceExistedException.class)
    public ResponseEntity<APIResponse<?>> handleResourceExistedException(ResourceExistedException ex) {
        APIResponse<?> response =
                APIResponse.builder().code(422).message(ex.getMessage()).build();

        return new ResponseEntity<APIResponse<?>>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Map<String, String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        APIResponse<Map<String, String>> response = new APIResponse<>();

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        ErrorCode error = ErrorCode.INVALID_DATA;

        response.setCode(error.getCode());
        response.setMessage(error.getMessage());
        response.setData(errors);

        return ResponseEntity.status(error.getHttpStatusCode()).body(response);
    }
}
