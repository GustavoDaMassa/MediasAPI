package br.com.gustavohenrique.MediasAPI.dtos;

import jakarta.validation.constraints.NotBlank;

public record RequestCourseDto(@NotBlank String name, @NotBlank String averageMethod, double cutOffGrade ) {
}
