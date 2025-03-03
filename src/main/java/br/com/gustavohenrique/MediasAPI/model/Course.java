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
@Table(name = "course", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "user_id"})})
public class Course {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_course_user"
                ,foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"))
        private Users user;

        @NotBlank
        private String name;

        @NotBlank
        private String averageMethod;

        private double cutOffGrade = 6.00;
}
