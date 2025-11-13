package br.com.gustavohenrique.MediasAPI.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundArgumentException.class)
    public ResponseEntity<StandardError> notFound(NotFoundArgumentException e, HttpServletRequest request){
        logger.error("Resource not found exception: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(),request.getRequestURI()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> illegalArgument (IllegalArgumentException e, HttpServletRequest request){
        logger.error("Illegal argument exception: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(),request.getRequestURI()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<StandardError> noSuchElement(NoSuchElementException e, HttpServletRequest request){
        logger.error("No such element exception: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardError(HttpStatus.BAD_REQUEST.value(),
                "The equation has operators without arguments",request.getRequestURI() ));
    }

    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<StandardError> DataIntegrity(DataIntegrityException e, HttpServletRequest request){
        logger.error("Data integrity exception: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<StandardError> Dividebyzero(HttpServerErrorException.InternalServerError e, HttpServletRequest request){
        logger.error("Internal server error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"possible division by zero detected", request.getRequestURI()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StandardError> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        logger.error("Constraint violation exception: {}", errorMessage, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new StandardError(HttpStatus.BAD_REQUEST.value(), errorMessage, request.getRequestURI()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> accessDenied(AccessDeniedException e, HttpServletRequest request){
        logger.error("Access denied exception: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new StandardError(HttpStatus.FORBIDDEN.value(), "Access Denied: You do not have permission to access this resource.", request.getRequestURI()));
    }
}
