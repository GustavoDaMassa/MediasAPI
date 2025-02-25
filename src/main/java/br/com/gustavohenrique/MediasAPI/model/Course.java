package br.com.gustavohenrique.MediasAPI.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

        double maxGrade = 10.00;

        public Course(double cutOffGrade, String averageMethod, String name, Long userId, Long id) {
                this.cutOffGrade = cutOffGrade;
                this.averageMethod = averageMethod;
                this.name = name;
                this.userId = userId;
                this.id = id;
        }

        public Course() {
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public Long getUser() {
                return userId;
        }

        public void setUser(Long userId) {
                this.userId = userId;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getAverageMethod() {
                return averageMethod;
        }

        public void setAverageMethod(String averageMethod) {
                this.averageMethod = averageMethod;
        }

        public double getCutOffGrade() {
                return cutOffGrade;
        }

        public void setCutOffGrade(double cutOffGrade) {
                this.cutOffGrade = cutOffGrade;
        }
}
