package br.com.gustavohenrique.MediasAPI.controller.rest.v1.mapper;

import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.dtos.AssessmentDTO;
import br.com.gustavohenrique.MediasAPI.dtos.ProjectionDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MapProjectionDTO implements MapDTO{

    private final ModelMapper modelMapper;
    @Autowired
    public MapProjectionDTO(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProjectionDTO projectionDTO(Projection projection){
        return new ProjectionDTO(projection.getId(), projection.getName(), projection.getAssessment()
                .stream().map(assessment -> modelMapper.map(assessment, AssessmentDTO.class))
                .collect(Collectors.toList()),projection.getFinalGrade(),projection.getCourse().getName());
    }
}
