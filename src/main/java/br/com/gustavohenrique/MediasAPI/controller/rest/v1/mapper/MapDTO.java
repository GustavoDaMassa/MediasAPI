package br.com.gustavohenrique.MediasAPI.controller.rest.v1.mapper;

import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.dtos.ProjectionDTO;

public interface MapDTO {
    ProjectionDTO projectionDTO(Projection projection);
}
