package br.com.gustavohenrique.MediasAPI.exception;

public class AssessmentNotFoundException extends NotFoundArgumentException {

    public AssessmentNotFoundException(Long id, Long projectionId) {
        super("Assessment id " + id + " not found for the Projection id " + projectionId);
    }
}
