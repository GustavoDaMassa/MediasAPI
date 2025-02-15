package br.com.gustavohenrique.MediasAPI.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "projection")
public class Projection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    Course course;

    @NotBlank
    String name;

    double finalGrade;

    //----------------------------------------------------------

    public Projection(Long id, String name, Course course, double finalGrade) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.finalGrade = finalGrade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(double finalGrade) {
        this.finalGrade = finalGrade;
    }
}
