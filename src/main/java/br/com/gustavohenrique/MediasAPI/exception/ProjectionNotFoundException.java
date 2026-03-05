package br.com.gustavohenrique.MediasAPI.exception;

public class ProjectionNotFoundException extends NotFoundArgumentException {

    public ProjectionNotFoundException(Long id, Long courseId) {
        super("Projection id " + id + " not found for the course id " + courseId);
    }

    public ProjectionNotFoundException(Long id) {
        super("Projection Id " + id + " not found");
    }
}
