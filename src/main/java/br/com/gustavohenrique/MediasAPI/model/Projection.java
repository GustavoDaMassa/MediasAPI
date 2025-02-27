package br.com.gustavohenrique.MediasAPI.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projection")
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

    public void setAssessment(Assessment assessment) {
        this.assessment.add(assessment);
    }


}
