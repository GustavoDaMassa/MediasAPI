package br.com.gustavohenrique.MediasAPI.model.dtos;


public class AssessmentDTO {
    private String identifier;
    private double grade;
    private double requiredGrade;


    public AssessmentDTO() {
    }

    public AssessmentDTO(String identifier, double grade, double requiredGrade) {
        this.identifier = identifier;
        this.grade = grade;
        this.requiredGrade = requiredGrade;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public double getRequiredGrade() {
        return requiredGrade;
    }

    public void setRequiredGrade(double requiredGrade) {
        this.requiredGrade = requiredGrade;
    }
}
