package br.com.gustavohenrique.MediasAPI.service.Interfaces;

import br.com.gustavohenrique.MediasAPI.model.Projection;

public interface ICalculateFinalGrade {
    void calculateResult(Projection projection, String averageMethod);
}
