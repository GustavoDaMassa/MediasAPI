package br.com.gustavohenrique.MediasAPI.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "course")
public record Course(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id,

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        User user,

        @NotBlank
        String name,

        @NotBlank
        String averageMethod,

        double cutOffGrade
) {
}
