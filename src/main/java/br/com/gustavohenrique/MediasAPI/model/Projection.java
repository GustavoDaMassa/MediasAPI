package br.com.gustavohenrique.MediasAPI.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projection", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "course_id"})})
public class Projection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Getter
    @OneToMany(mappedBy = "projection", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Assessment> assessment = new ArrayList<>();

    @NotBlank
    private String name;

    private double finalGrade;

    //----------------------------------------------------------

    public Projection(Long courseId, String name) {
        this.courseId = courseId;
        this.name = name;
    }

    public void addAssessment(Assessment assessment) {
        this.assessment.add(assessment);
    }


}
