package br.com.gustavohenrique.MediasAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotBlank
    private String name;

    @Setter
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @Setter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Course> course;

    @Setter
    @NotBlank
    private String password;

    @Setter
    @Enumerated(EnumType.STRING)
    private Role role;

    //----------------------------------------------------------



}

