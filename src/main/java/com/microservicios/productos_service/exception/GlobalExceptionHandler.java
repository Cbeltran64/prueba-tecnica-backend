package com.microservicios.productos_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<Map<String, Object>> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            Map<String, Object> err = new HashMap<>();
            err.put("status", "400");
            err.put("title", "Error de validación");
            err.put("detail", error.getDefaultMessage());
            err.put("source", Map.of("pointer", "/data/attributes/" + error.getField()));
            errors.add(err);
        });

        return ResponseEntity.badRequest().body(Map.of("errors", errors));
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Object> handleNotFound(RecursoNoEncontradoException ex) {
        Map<String, Object> error = Map.of(
                "errors", List.of(Map.of(
                        "status", "404",
                        "title", "Recurso no encontrado",
                        "detail", ex.getMessage()
                ))
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralError(Exception ex) {
        Map<String, Object> error = Map.of(
                "errors", List.of(Map.of(
                        "status", "500",
                        "title", "Error interno",
                        "detail", ex.getMessage()
                ))
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
