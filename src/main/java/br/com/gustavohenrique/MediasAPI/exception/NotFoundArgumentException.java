package br.com.gustavohenrique.MediasAPI.exception;

public abstract class NotFoundArgumentException extends RuntimeException {

    protected NotFoundArgumentException(String message) {
        super(message);
    }
}
