package br.com.gustavohenrique.MediasAPI.exception;

public class FailedException extends  RuntimeException{
    public FailedException(String message) {
        super(message);
    }
}
