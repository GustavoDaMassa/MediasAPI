package br.com.gustavohenrique.MediasAPI.service.Interfaces;

import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Projection;

public interface ICalculateRequiredGradeMaxNear {
    void calculateRequiredGradeMaxNear(Projection projection, Course course);
}
