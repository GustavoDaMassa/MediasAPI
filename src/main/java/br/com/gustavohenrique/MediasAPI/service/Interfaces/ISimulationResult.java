package br.com.gustavohenrique.MediasAPI.service.Interfaces;

import br.com.gustavohenrique.MediasAPI.model.Projection;

import java.util.ArrayList;
import java.util.Set;

public interface ISimulationResult {
    double simulate(double requiredGrade, Projection projection, ArrayList<String> polishNotation);

    double simulateWithMaxed(double requiredGrade, Projection projection,
                             ArrayList<String> polishNotation,
                             Set<String> maxedIdentifiers);
}
