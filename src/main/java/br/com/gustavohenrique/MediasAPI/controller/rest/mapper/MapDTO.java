package br.com.gustavohenrique.MediasAPI.controller.rest.mapper;

import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.model.dtos.ProjectionDTO;

public interface MapDTO {
    ProjectionDTO projectionDTO(Projection projection);
}
