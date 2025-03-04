package br.com.gustavohenrique.MediasAPI.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectionDTO {

    private Long id;
    private String name;
    private List<AssessmentDTO> assessment;
    private double finalGrade;
    private String courseName;
}