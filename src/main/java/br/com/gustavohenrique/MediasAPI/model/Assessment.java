package br.com.gustavohenrique.MediasAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "assessment")
public class Assessment {

    @ManyToOne
    @JoinColumn(name = "projection_id", nullable = false)
    Projection projection;

    @Id
    @NotBlank
    String identifier;

    double grade;

    double maxValue = 10.00;

    double requiredGrade;

    boolean done = false;

    // -------------------------------------------------------


    public Assessment(Projection projection, String identifier, double grade, double maxValue, double requiredGrade, boolean done) {
        this.projection = projection;
        this.identifier = identifier;
        this.grade = grade;
        this.maxValue = maxValue;
        this.requiredGrade = requiredGrade;
        this.done = done;
    }

    public double getGrade() {
        return grade;
    }

    public Projection getProjection() {
        return projection;
    }

    public String getIdentifier() {
        return identifier;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getRequiredGrade() {
        return requiredGrade;
    }

    public boolean isDone() {
        return done;
    }

    public void setProjection(Projection projection) {
        this.projection = projection;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setRequiredGrade(double requiredGrade) {
        this.requiredGrade = requiredGrade;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
