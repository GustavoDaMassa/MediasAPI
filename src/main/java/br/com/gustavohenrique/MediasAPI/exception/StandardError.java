package br.com.gustavohenrique.MediasAPI.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class StandardError {

    private LocalDateTime timestamp = LocalDateTime.now();
    private Integer statusCode;
    private String error;
    private String path;

    public StandardError(Integer statusCode, String error,String path){
        this.statusCode = statusCode;
        this.error = error;
        this.path = path;
    }
}
