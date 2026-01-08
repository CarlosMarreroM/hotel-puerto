package org.docencia.hotel.web.rest;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja IllegalArgumentException lanzando un 400 Bad Request
     * 
     * @param ex la excepción IllegalArgumentException lanzada
     * @param req la solicitud HTTP que causó la excepción
     * @return una respuesta HTTP con el error formateado
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex, HttpServletRequest req) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(400, "Bad Request", ex.getMessage(), req.getRequestURI()));
    }

    /**
     * Maneja IllegalStateException lanzando un 409 Conflict
     * 
     * @param ex la excepción IllegalStateException lanzada
     * @param req la solicitud HTTP que causó la excepción
     * @return una respuesta HTTP con el error formateado
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleConflict(IllegalStateException ex, HttpServletRequest req) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiError.of(409, "Conflict", ex.getMessage(), req.getRequestURI()));
    }

    /**
     * Maneja NullPointerException lanzando un 400 Bad Request
     * 
     * @param ex la excepción NullPointerException lanzada
     * @param req la solicitud HTTP que causó la excepción
     * @return una respuesta HTTP con el error formateado
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiError> handleNull(NullPointerException ex, HttpServletRequest req) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(400, "Bad Request", ex.getMessage(), req.getRequestURI()));
    }

    /**
     * Maneja cualquier otra excepción lanzando un 500 Internal Server Error
     * 
     * @param ex la excepción lanzada
     * @param req la solicitud HTTP que causó la excepción
     * @return una respuesta HTTP con el error formateado
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(500, "Internal Server Error", "Unexpected error", req.getRequestURI()));
    }

    /**
     * Registro de error API para respuestas de error estandarizadas
     */
    public record ApiError(
            Instant timestamp,
            int status,
            String error,
            String message,
            String path
    ) {
        public static ApiError of(int status, String error, String message, String path) {
            return new ApiError(Instant.now(), status, error, message, path);
        }
    }
}
