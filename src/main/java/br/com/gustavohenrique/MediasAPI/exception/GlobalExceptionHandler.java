package br.com.gustavohenrique.MediasAPI.exception;

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

    @ExceptionHandler(NotFoundArgumentException.class)
    public ResponseEntity<StandardError> notFound(NotFoundArgumentException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(),request.getRequestURI()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> illegalArgument (IllegalArgumentException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(),request.getRequestURI()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<StandardError> noSuchElement(NoSuchElementException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardError(HttpStatus.BAD_REQUEST.value(),
                "The equation has operators without arguments",request.getRequestURI() ));
    }

    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<StandardError> DataIntegrity(DataIntegrityException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<StandardError> Dividebyzero(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"possible division by zero detected", request.getRequestURI()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StandardError> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new StandardError(HttpStatus.BAD_REQUEST.value(), errorMessage, request.getRequestURI()));
    }
}
