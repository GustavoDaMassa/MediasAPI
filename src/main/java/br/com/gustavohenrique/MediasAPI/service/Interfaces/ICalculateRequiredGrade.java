package br.com.gustavohenrique.MediasAPI.service.Interfaces;

import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Projection;

public interface ICalculateRequiredGrade {
    void calculateRequiredGrade(Projection projection, Course course);
}
