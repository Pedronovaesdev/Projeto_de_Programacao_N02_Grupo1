package com.grupo1.controller.exception;

import com.grupo1.dto.ErrorResponseDTO;
import org.hibernate.PropertyValueException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Erro de integridade dos dados. Verifique os campos obrigatórios.";

        Throwable cause = ex.getCause();
        if (cause instanceof PropertyValueException pve) {
            message = "O campo '" + pve.getPropertyName() + "' não pode ser nulo ou vazio.";
        }

        ErrorResponseDTO error = new ErrorResponseDTO(message, LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException ex) {

        if (ex.getMessage() != null && ex.getMessage().contains("email informado já está em uso")) {
            ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage(), LocalDateTime.now());
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }

        ErrorResponseDTO error = new ErrorResponseDTO("Erro interno inesperado no servidor.", LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
