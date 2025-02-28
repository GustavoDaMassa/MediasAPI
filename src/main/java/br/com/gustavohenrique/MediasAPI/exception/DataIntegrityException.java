package br.com.gustavohenrique.MediasAPI.exception;

public class DataIntegrityException extends  RuntimeException{
    public DataIntegrityException(String attribute) {
        super("The attribute "+attribute+" already exist for this context");
    }
}
