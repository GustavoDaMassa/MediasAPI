package br.com.gustavohenrique.MediasAPI.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class StandardError {

    private LocalDateTime timestamp;
    private Integer statusCode;
    private String error;
    private String path;
}
