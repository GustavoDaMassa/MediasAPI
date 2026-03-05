package br.com.gustavohenrique.MediasAPI.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "assessment", uniqueConstraints = {@UniqueConstraint(columnNames = {"identifier","projection_id"})})
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(updatable = false)
    private String identifier;

    private double grade = 0.00;

    @NotNull
    @Column(updatable = false)
    private double maxValue = 10.00;

    @Setter
    private double requiredGrade;

    private boolean fixed = false;

    @Setter
    @ManyToOne
    @JoinColumn(name = "projection_id", nullable = false, foreignKey = @ForeignKey(name = "fk_assessment_projection",
            foreignKeyDefinition = "FOREIGN KEY (projection_id) REFERENCES projection(id) ON DELETE CASCADE"))
    private Projection projection;

    protected Assessment() {}

    public Assessment(String identifier, double maxValue, Projection projection) {
        this.identifier = identifier;
        this.maxValue = maxValue;
        this.projection = projection;
    }

    public Assessment(String identifier, double grade, double maxValue, Projection projection) {
        this.identifier = identifier;
        this.grade = grade;
        this.maxValue = maxValue;
        this.projection = projection;
    }

    public void applyGrade(double grade) {
        if (grade > this.maxValue)
            throw new IllegalArgumentException("It is not allowed to enter a grade higher than " + this.maxValue);
        this.grade = grade;
        this.fixed = true;
    }
}
