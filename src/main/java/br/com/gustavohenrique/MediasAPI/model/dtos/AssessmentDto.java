package br.com.gustavohenrique.MediasAPI.controller.dtos;

public record AssessmentDto(String identifier,double grade, double requiredGrade, Long idProjection) {
}
