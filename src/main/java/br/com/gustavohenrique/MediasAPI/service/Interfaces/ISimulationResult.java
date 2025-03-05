package br.com.gustavohenrique.MediasAPI.service.Interfaces;

import br.com.gustavohenrique.MediasAPI.model.Projection;

import java.util.ArrayList;

public interface ISimulationResult {
    double simulate(double requiredGrade, Projection projection, ArrayList<String> polishNotation);
}
