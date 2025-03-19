package br.com.gustavohenrique.MediasAPI.dtos;

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

    public ProjectionDTO(Long id, String name, List<AssessmentDTO> assessment, double finalGrade) {
        this.id = id;
        this.name = name;
        this.assessment = assessment;
        this.finalGrade = finalGrade;
    }
}