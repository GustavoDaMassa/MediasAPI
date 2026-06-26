package br.com.gustavohenrique.MediasAPI.service.Interfaces;

import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectionService extends OwnershipValidator {
    Projection createProjection(Long courseId, @Valid StringRequestDTO projectionName);

    Page<Projection> listProjection(Long courseId, Pageable pageable);

    Projection updateProjectionName(Long courseId, Long id, StringRequestDTO newProjectionName);

    Projection deleteProjection(Long courseId, Long id);

    void deleteAllProjections(Long courseId, Long userId);

    Page<Projection> listAllProjection(Long userId, Pageable pageable);

    Projection resetProjection(Long courseId, Long projectionId);

}
