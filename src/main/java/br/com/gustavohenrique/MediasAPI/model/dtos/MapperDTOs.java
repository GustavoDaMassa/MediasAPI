package br.com.gustavohenrique.MediasAPI.model.dtos;

import br.com.gustavohenrique.MediasAPI.model.Projection;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MapperDTOs {

    @Autowired
    private ModelMapper modelMapper;

    public ProjectionDTO projectionDTO(Projection projection){
        return new ProjectionDTO(projection.getId(), projection.getName(), projection.getAssessment()
                .stream().map(assessment -> modelMapper.map(assessment, AssessmentDTO.class))
                .collect(Collectors.toList()),projection.getFinalGrade(),projection.getCourse().getName());
    }
}
