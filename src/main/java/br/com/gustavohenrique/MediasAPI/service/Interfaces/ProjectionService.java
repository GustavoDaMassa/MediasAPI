package br.com.gustavohenrique.MediasAPI.service.Interfaces;

import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.model.dtos.StringRequestDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface ProjectionService {
    Projection createProjection(Long courseId, @Valid StringRequestDTO projectionName);

    List<Projection> listProjection(Long courseId);

    Projection updateProjectionName(Long courseId, Long id, StringRequestDTO newProjectionName);

    Projection deleteProjection(Long courseId, Long id);

    void deleteAllProjections(Long courseId);

    List<Projection> listAllProjection(Long userId);
}
