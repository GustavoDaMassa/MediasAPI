package br.com.gustavohenrique.MediasAPI.exception;

public class CourseNotFoundException extends NotFoundArgumentException {

    public CourseNotFoundException(Long id, Long userId) {
        super("Course id " + id + " not found for UserId " + userId);
    }

    public CourseNotFoundException(Long id) {
        super("Course id " + id + " not found");
    }
}
