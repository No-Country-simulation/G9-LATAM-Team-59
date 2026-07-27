package com.financeai.config;

import com.financeai.config.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaExcepcionDTO> manejarValidacion(MethodArgumentNotValidException excepcion) {
        String mensaje = excepcion.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Error de validación");
        
        RespuestaExcepcionDTO respuestaExcepcionDTO = new RespuestaExcepcionDTO(
                mensaje,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                LocalDateTime.now());

        return new ResponseEntity<>(respuestaExcepcionDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExcepcionNegocio.class)
    public ResponseEntity<RespuestaExcepcionDTO> manejarExcepcionNegocio(ExcepcionNegocio excepcionNegocio) {

        HttpStatus estado = excepcionNegocio.getStatus();

        RespuestaExcepcionDTO respuestaExcepcionDTO = new RespuestaExcepcionDTO(
                excepcionNegocio.getMessage(),
                estado.getReasonPhrase(),
                LocalDateTime.now());

        return new ResponseEntity<>(respuestaExcepcionDTO, estado);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<RespuestaExcepcionDTO> manejarRuntimeException(RuntimeException excepcion) {

        HttpStatus estado = HttpStatus.INTERNAL_SERVER_ERROR;

        RespuestaExcepcionDTO respuestaExcepcionDTO = new RespuestaExcepcionDTO(
                "Error del servidor: " + excepcion.getMessage(),
                estado.getReasonPhrase(),
                LocalDateTime.now());

        return new ResponseEntity<>(respuestaExcepcionDTO, estado);
    }
}
