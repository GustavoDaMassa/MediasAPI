package br.com.gustavohenrique.MediasAPI.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<StandardError>invalidArgument(InvalidArgumentException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StandardError(LocalDateTime.now(),HttpStatus.BAD_REQUEST.value(), e.getMessage(),request.getRequestURI()));
    }
}
