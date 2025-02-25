package br.com.gustavohenrique.MediasAPI.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projection")
public class Projection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @OneToMany(mappedBy = "projection", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Assessment> assessment = new ArrayList<>();

    @NotBlank
    private String name;

    private double finalGrade;

    //----------------------------------------------------------

    public Projection(Long courseId, String name) {
        this.courseId = courseId;
        this.name = name;
    }

    public Projection() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(double finalGrade) {
        this.finalGrade = finalGrade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

     public Assessment getAssessment() {
        return new Assessment();
    }

    public void setAssessment(Assessment assessment) {
        this.assessment.add(assessment);
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
