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
    String identifier;

    double grade = 0.00;

    @NotNull
    double maxValue = 10.00;

    double requiredGrade;

    @Setter
    private boolean fixed = false;

    @Setter
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

}
