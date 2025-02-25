package br.com.gustavohenrique.MediasAPI.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "assessment")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    String identifier;

    double grade = 0.00;

    @NotNull
    double maxValue = 10.00;

    double requiredGrade;

    private boolean fixed = false;

    @ManyToOne
    @JoinColumn(name = "projection_id", nullable = false)
    @JsonBackReference
    Projection projection;
    // -------------------------------------------------------


    public Assessment(String identifier, double grade, double maxValue, Projection projection) {

        this.identifier = identifier;
        this.grade = grade;
        this.maxValue = maxValue;
        this.projection = projection;
    }

    public Assessment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getRequiredGrade() {
        return requiredGrade;
    }

    public void setRequiredGrade(double requiredGrade) {
        this.requiredGrade = requiredGrade;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public Projection getProjection() {
        return projection;
    }

    public void setProjection(Projection projection) {
        this.projection = projection;
    }
}
