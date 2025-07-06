package com.jfuente040.springsecurity002.config;

import com.jfuente040.springsecurity002.controller.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, 
                                                                        HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Usuario no encontrado",
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURL().toString(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex, 
                                                                      HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Credenciales inválidas",
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURL().toString(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, 
                                                                        HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Argumento inválido",
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURL().toString(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, 
                                                                   HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Error de validación",
                ex.getBindingResult().getFieldErrors().toString(),
                request.getMethod(),
                request.getRequestURL().toString(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Error interno del servidor",
                ex.getMessage(),
                request.getMethod(),
                request.getRequestURL().toString(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
