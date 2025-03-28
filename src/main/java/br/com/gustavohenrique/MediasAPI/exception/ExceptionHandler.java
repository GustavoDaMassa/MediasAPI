package br.com.gustavohenrique.MediasAPI.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundArgumentException.class)
    public ResponseEntity<StandardError> notFound(NotFoundArgumentException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(),request.getRequestURI()));
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> illegalArgument (IllegalArgumentException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body( new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(),request.getRequestURI()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<StandardError> noSuchElement(NoSuchElementException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardError(HttpStatus.BAD_REQUEST.value(),
                "The equation has operators without arguments",request.getRequestURI() ));
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<StandardError> DataIntegrity(DataIntegrityException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI()));
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<StandardError> Dividebyzero(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"possible division by zero detected", request.getRequestURI()));
    }
}
