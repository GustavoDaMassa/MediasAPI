package br.com.gustavohenrique.MediasAPI.model.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CourseDTO {
    @NotBlank
    String name;

    @NotBlank
    String averageMethod;

    double cutOffGrade = 6.00;
}
