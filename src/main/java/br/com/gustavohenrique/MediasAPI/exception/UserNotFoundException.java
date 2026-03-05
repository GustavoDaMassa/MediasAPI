package br.com.gustavohenrique.MediasAPI.exception;

public class UserNotFoundException extends NotFoundArgumentException {

    public UserNotFoundException(Long id) {
        super("User Id " + id + " not found");
    }

    public UserNotFoundException(String email) {
        super("User email " + email + " not found");
    }
}
