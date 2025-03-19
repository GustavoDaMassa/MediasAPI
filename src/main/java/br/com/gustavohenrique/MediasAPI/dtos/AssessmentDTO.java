package br.com.gustavohenrique.MediasAPI.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentDTO {

    private Long id;
    private String identifier;
    private double grade;
    private double requiredGrade;

}
