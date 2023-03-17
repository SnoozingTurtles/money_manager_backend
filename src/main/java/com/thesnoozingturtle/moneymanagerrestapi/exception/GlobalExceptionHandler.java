package com.thesnoozingturtle.moneymanagerrestapi.exception;

import com.thesnoozingturtle.moneymanagerrestapi.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Catches the user not found exception
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> userNotFoundExceptionHandler(EntityNotFoundException exception) {
        String message = exception.getMessage();
        ApiResponse apiResponse = new ApiResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    //Catches the validation exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        Map<String, String> response = new HashMap<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(fieldError -> response.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Catches the Login exception
    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ApiResponse> loginExceptionHandler(LoginException exception) {
        String message = exception.getMessage();
        ApiResponse apiResponse = new ApiResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    //Catches the Refresh token exceptions
    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ApiResponse> refreshTokenExceptionHandler(RefreshTokenException refreshTokenException) {
        String message = refreshTokenException.getMessage();
        ApiResponse apiResponse = new ApiResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    //Catches the file not found exception
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ApiResponse> fileNotFoundExceptionHandler(FileNotFoundException exception) {
        String message = exception.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse
                                .builder()
                                .success(false)
                                .message(message)
                                .build()
                );
    }
}
