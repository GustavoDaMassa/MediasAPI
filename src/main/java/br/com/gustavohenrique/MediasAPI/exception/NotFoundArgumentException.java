package br.com.gustavohenrique.MediasAPI.exception;

public class NotFoundArgumentException extends RuntimeException {

    public NotFoundArgumentException(String message) {
        super(message);
    }
}
