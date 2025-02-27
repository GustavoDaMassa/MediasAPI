package br.com.gustavohenrique.MediasAPI.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
public class Course {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;

        @Column(name = "user_id", nullable = false)
        Long userId;

        @NotBlank
        String name;

        @NotBlank
        String averageMethod;

        double cutOffGrade = 6.00;
}
