package br.com.gustavohenrique.MediasAPI.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundArgumentException.class)
    public ResponseEntity<StandardError> notFound(NotFoundArgumentException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StandardError(LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(), e.getMessage(),request.getRequestURI()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> illegalArgument (IllegalArgumentException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( new StandardError(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(), e.getMessage(),request.getRequestURI()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<StandardError> noSuchElement(NoSuchElementException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardError(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "The equation has operators without arguments",request.getRequestURI() ));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(FailedException.class)
    public ResponseEntity<String> failed(FailedException e){
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<StandardError> DataIntegrity(DataIntegrityException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardError(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI()));
    }
}
